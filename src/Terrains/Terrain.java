package Terrains;

import Main.*;
import Utils.DiverseUtilities;
import org.joml.Vector3f;

import java.nio.ByteBuffer;
import java.util.Queue;
import java.util.Random;
import java.util.concurrent.LinkedBlockingQueue;

public class Terrain {
    private static float[][] heightMap;
    private static float[][] specialMap;
    private static float[][] moistureMap;
    private static Texture biomes;
    private static Texture permTexture;
    private static Texture simplexTexture;
    private Tile[][] squares;
    private static SimplexNoiseGenerator simplexNoiseGenerator;
    private static Loader loader;
    private RawModel rawModel;
    private RawModel outlines;
    private Vector3f position = new Vector3f(0,0,0);
    private float rx = 0, ry = 0, rz = 0, scale = 1f;
    private int tileX, tileZ;
    private float[] vertices;
    private int[] indices;
    private float[] textureCoords;
    private float[] normals;
    private Random random = new Random();
    private boolean showLines = false;

    public Terrain(int tileX, int tileZ) {
        this.tileX = tileX;
        this.tileZ = tileZ;
        position = new Vector3f(0, 0, 0);
        loader = new Loader();
        squares = new Tile[Game.X_SQUARES_PER_TILE][Game.Z_SQUARES_PER_TILE];


        simplexNoiseGenerator = new SimplexNoiseGenerator();
        buildHeightMap();
        buildMoistureMap();
        buildSpecialMap();
        buildSquares();
        generateModel();
    }

    private void buildHeightMap() {
        heightMap = simplexNoiseGenerator.buildHeightMap(Game.X_SQUARES_PER_TILE,
                Game.Z_SQUARES_PER_TILE);
//        NoiseMapWindow noiseMapWindow = new NoiseMapWindow();
//        noiseMapWindow.loadImage(noiseMapWindow.loadNoiseMap(heightMap));
//        heightMap = simplexNoiseGenerator.generateForest(random.nextInt(9999), heightMap, moistureMap);
    }

    private void buildMoistureMap() {
        moistureMap = new float[Game.X_SQUARES_PER_TILE][Game.Z_SQUARES_PER_TILE];
        moistureMap = simplexNoiseGenerator.generateNoise(random.nextInt(9999),
                moistureMap.length, moistureMap[0].length, 3, 1);
//        NoiseMapWindow noiseMapWindow = new NoiseMapWindow();
//        noiseMapWindow.loadImage(noiseMapWindow.loadNoiseMap(moistureMap));
    }

    private void buildSpecialMap() {
        specialMap = simplexNoiseGenerator.generateNoise(random.nextInt(), Game.X_SQUARES_PER_TILE,
                Game.Z_SQUARES_PER_TILE, 20, 1);
    }

    private void buildSquares() {
        for (int i = 0; i < heightMap[0].length; i++) {
            for (int j = 0; j < heightMap.length; j++) {
                int biome = BiomeGenerator.generateBiome(heightMap[j][i], moistureMap[j][i], specialMap[j][i]);
                float height = BiomeGenerator.buildMapHeight(heightMap[j][i], biome);
                squares[j][i] = new Tile(height, biome, j, i);
            }
        }
        squares[0][0].setMap(squares);
        Queue<Tile> oceanQueue = new LinkedBlockingQueue<>();
        oceanQueue.add(squares[0][0].getEasternNeighbor(x -> true));
        while (!oceanQueue.isEmpty()) {
            Tile current = oceanQueue.remove();
            try {
                Tile n = current.getNorthernNeighbor(x -> !x.isOcean());
                n.setOcean(true);
                oceanQueue.add(n);
            } catch (ArrayIndexOutOfBoundsException e) {
//                System.out.println("ArrayIndexOutOfBoundsException");
            }
            try {
                Tile n = current.getWesternNeighbor(x -> !x.isOcean());
                n.setOcean(true);
                oceanQueue.add(n);
            } catch (ArrayIndexOutOfBoundsException e) {
//                System.out.println("ArrayIndexOutOfBoundsException");
            }
            try {
                Tile n = current.getEasternNeighbor(x -> !x.isOcean());
                n.setOcean(true);
                oceanQueue.add(n);
            } catch (ArrayIndexOutOfBoundsException e) {
//                System.out.println("ArrayIndexOutOfBoundsException");
            }
            try {
                Tile n = current.getSouthernNeighbor(x -> !x.isOcean());
                n.setOcean(true);
                oceanQueue.add(n);
            } catch (ArrayIndexOutOfBoundsException e) {
//                System.out.println("ArrayIndexOutOfBoundsException");
            }
        }

        int idx = 0;
        for (int i = 0; i < squares[0].length; i++) {
            for (int j = 0; j < squares.length; j++) {
                Tile current = squares[j][i];
                boolean[] neswIsLower = new boolean[] {
                        false, false, false, false
                };
                if (i > 0) {
                    Tile north = squares[j][i-1];
                    neswIsLower[0] = north.getHeight() < current.getHeight();
                }
                if (j < squares.length-1) {
                    Tile east = squares[j+1][i];
                    neswIsLower[1] = east.getHeight() < current.getHeight();
                }
                if (i < squares[0].length-1) {
                    Tile south = squares[j][i+1];
                    neswIsLower[2] = south.getHeight() < current.getHeight();
                }
                if (j > 0) {
                    Tile west = squares[j-1][i];
                    neswIsLower[3] = west.getHeight() < current.getHeight();
                }
                if (squares[j][i].getHeight() > 0f) {
                    idx += current.genData(idx, neswIsLower);
                }
                current.genLines();
            }
        }
        for (int i = 1; i < squares[0].length-1; i++) {
            for (int j = 1; j < squares.length-1; j++) {
                Tile current = squares[j][i];
                Tile north = squares[j][i-1];
                Tile south = squares[j][i+1];
                Tile west = squares[j-1][i];
                Tile east = squares[j+1][i];
                if (!current.isOcean() && current.isWater()) {
                    current.setLake(true);
                }
                if ((north.isWater() || south.isWater() || east.isWater() || west.isWater()) && !current.isWater()) {
                    current.setCoast(true);
                }
            }
        }
    }

    public void generateModel() {
        int c = 0;
        for (int i = 0; i < squares[0].length; i++) {
            for (int j = 0; j < squares.length; j++) {
                if (squares[j][i].getHeight() > 0f) {
                    c += squares[j][i].getVertices().length;
                }
            }
        }
        vertices = new float[c];
        textureCoords = new float[c * 2 / 3];
        normals = new float[c];
        indices = new int[c / 2];

        c = 0;
        for (int i = 0; i < squares[0].length; i++) {
            for (int j = 0; j < squares.length; j++) {
                if (squares[j][i].getHeight() > 0f) {
                    float[] squareVertices = squares[j][i].getVertices();
                    float[] squareTexCoords = squares[j][i].getTextureCoords();
                    float[] squareNormals = squares[j][i].getNormals();
                    int[] squareIndices = squares[j][i].getIndices();
                    vertices = DiverseUtilities.addArrayToArray(squareVertices, vertices, c);
                    textureCoords = DiverseUtilities.addArrayToArray(squareTexCoords, textureCoords, c * 2 / 3);
                    normals = DiverseUtilities.addArrayToArray(squareNormals, normals, c);
                    indices = DiverseUtilities.addArrayToArray(squareIndices, indices, c / 2);
                    c += squares[j][i].getVertices().length;
                }
            }
        }

        System.out.println(c + " of " + squares[0].length * squares.length + " rendered");
        System.out.println(vertices.length + " vertices rendered");

        int[][] biomeMap = new int[heightMap.length][heightMap[0].length];
        for (int i = 0; i < squares[0].length; i++) {
            for (int j = 0; j < squares.length; j++) {
                biomeMap[j][i] = squares[j][i].getBiome();
            }
        }
        ByteBuffer texByteBuffer = loader.load2DArrayToByteBuffer(biomeMap);
        biomes = loader.create2DTextureFromByteBuffer(texByteBuffer, heightMap.length, heightMap[0].length);
        rawModel = loader.createRawModel(vertices, indices, textureCoords, normals);

        float[] lineVertices = new float[51 * Game.SQUARES_PER_TILE];
        int i = 0;
        for (Tile[] squares2 : squares) {
            for (Tile square : squares2) {
                for (int j = 0; j < 51; j++) {
                    lineVertices[i++] = square.getOutline()[j];
                }
            }
        }
        outlines = loader.createRawModel(lineVertices, 3);
    }

    public float getHeightFromMapCoords(int x, int z) {
        try {
            return squares[x / 5][z / 5].getHeight();
        } catch (ArrayIndexOutOfBoundsException e) {
            return 0;
        }
    }

    public RawModel getRawModel() {
        return rawModel;
    }

    public void printPosition() {
        System.out.println("Terrain position: " + position.x + " " + position.y + " " + position.z);
    }

    public float getScale() {
        return scale;
    }

    public float getRz() {
        return rz;
    }

    public float getRy() {
        return ry;
    }

    public float getRx() {
        return rx;
    }

    public Vector3f getPosition() {
        return position;
    }

    public RawModel getSquareOutlines() {
        return outlines;
    }

    public Texture getBiomesTexture() {
        return biomes;
    }

    public boolean linesShown() {
        return showLines;
    }

    public void toggleLines() {
        showLines = !showLines;
    }

    public void reload() {
        buildHeightMap();
        buildMoistureMap();
        buildSpecialMap();
        buildSquares();
        generateModel();
    }

    public static float[][] getMoistureMap() {
        return moistureMap;
    }
}
