package Terrains;

import Main.*;
import Tests.NoiseMapWindow;
import Utils.ArrayManipulation;
import Utils.DiverseUtilities;
import org.joml.Vector3f;

import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class TerrainTile {
    private static float[][] heightMap;
    private static float[][] specialMap;
    private static float[][] moistureMap;
    private static int[][] biomeMap;
    private static Texture biomes;
    private static Texture permTexture;
    private static Texture simplexTexture;
    private TerrainSquare[][] squares;
    private static SimplexNoiseGenerator simplexNoiseGenerator;
    private static Loader loader;
    private RawModel rawModel;
    private Vector3f position = new Vector3f(0,0,0);
    private float rx = 0, ry = 0, rz = 0, scale = 1f;
    private int tileX, tileZ;
    private float[] vertices;
    private int[] indices;
    private float[] textureCoords;
    private float[] normals;
    private Random random = new Random();
    private boolean showLines = false;
    private static int[] perm = {151,160,137,91,90,15,
            131,13,201,95,96,53,194,233,7,225,140,36,103,30,69,142,8,99,37,240,21,10,23,
            190, 6,148,247,120,234,75,0,26,197,62,94,252,219,203,117,35,11,32,57,177,33,
            88,237,149,56,87,174,20,125,136,171,168, 68,175,74,165,71,134,139,48,27,166,
            77,146,158,231,83,111,229,122,60,211,133,230,220,105,92,41,55,46,245,40,244,
            102,143,54, 65,25,63,161, 1,216,80,73,209,76,132,187,208, 89,18,169,200,196,
            135,130,116,188,159,86,164,100,109,198,173,186, 3,64,52,217,226,250,124,123,
            5,202,38,147,118,126,255,82,85,212,207,206,59,227,47,16,58,17,182,189,28,42,
            223,183,170,213,119,248,152, 2,44,154,163, 70,221,153,101,155,167, 43,172,9,
            129,22,39,253, 19,98,108,110,79,113,224,232,178,185, 112,104,218,246,97,228,
            251,34,242,193,238,210,144,12,191,179,162,241, 81,51,145,235,249,14,239,107,
            49,192,214, 31,181,199,106,157,184, 84,204,176,115,121,50,45,127, 4,150,254,
            138,236,205,93,222,114,67,29,24,72,243,141,128,195,78,66,215,61,156,180};
    private static int[][] grad3 = {{0,1,1},{0,1,-1},{0,-1,1},{0,-1,-1},
            {1,0,1},{1,0,-1},{-1,0,1},{-1,0,-1},
            {1,1,0},{1,-1,0},{-1,1,0},{-1,-1,0},
            {1,0,-1},{-1,0,-1},{0,-1,1},{0,1,1}};
    private static int[] simplex4 = {0,64,128,192,0,64,192,128,0,0,0,0,
            0,128,192,64,0,0,0,0,0,0,0,0,0,0,0,0,64,128,192,0,
            0,128,64,192,0,0,0,0,0,192,64,128,0,192,128,64,
            0,0,0,0,0,0,0,0,0,0,0,0,64,192,128,0,
            0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,
            0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,
            64,128,0,192,0,0,0,0,64,192,0,128,0,0,0,0,
            0,0,0,0,0,0,0,0,128,192,0,64,128,192,64,0,
            64,0,128,192,64,0,192,128,0,0,0,0,0,0,0,0,
            0,0,0,0,128,0,192,64,0,0,0,0,128,64,192,0,
            0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,
            0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,
            128,0,64,192,0,0,0,0,0,0,0,0,0,0,0,0,
            192,0,64,128,192,0,128,64,0,0,0,0,192,64,128,0,
            128,64,0,192,0,0,0,0,0,0,0,0,0,0,0,0,
            192,64,0,128,0,0,0,0,192,128,0,64,192,128,64,0};

    public TerrainTile(int tileX, int tileZ) {
        this.tileX = tileX;
        this.tileZ = tileZ;
        position = new Vector3f(0, 0, 0);
        loader = new Loader();
        squares = new TerrainSquare[Game.X_SQUARES_PER_TILE][Game.Z_SQUARES_PER_TILE];


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
        biomeMap = new int[heightMap.length][heightMap[0].length];
        int c = 0;
        for (int i = 0; i < heightMap[0].length; i++) {
            for (int j = 0; j < heightMap.length; j++) {
                int biome = BiomeGenerator.generateBiome(heightMap[j][i], moistureMap[j][i], specialMap[j][i]);
                biomeMap[j][i] = biome;
                squares[j][i] = new TerrainSquare(heightMap[j][i], j * 5,
                        i * 5, biome, 20*c++);
            }
        }
    }




    public void generateModel() {
        int c = 0;
        for (int i = 0; i < squares[0].length; i++) {
            for (int j = 0; j < squares.length; j++) {
                if (squares[j][i].getHeight() > 0f) {
                    c++;
                }
            }
        }
        vertices = new float[c * 60];
        textureCoords = new float[c * 40];
        normals = new float[c * 60];
        indices = new int[c * 30];
        c = 0;
        int sum = 0;
        for (int i = 0; i < squares[0].length; i++) {
            for (int j = 0; j < squares.length; j++) {
                if (squares[j][i].getHeight() > 0f) {
                    float[] squareVertices = squares[j][i].getVertices();
                    float[] squareTexCoords = squares[j][i].getTextureCoords();
                    float[] squareNormals = squares[j][i].getNormals();
                    int[] squareIndices = squares[j][i].getIndices();
                    DiverseUtilities.addArrayToArray(squareVertices, vertices, c * Game.VERTICES_PER_SQUARE * 3);
                    DiverseUtilities.addArrayToArray(squareTexCoords, textureCoords, c * Game.VERTICES_PER_SQUARE * 2);
                    DiverseUtilities.addArrayToArray(squareNormals, normals, c * Game.VERTICES_PER_SQUARE * 3);
                    DiverseUtilities.addArrayToArray(squareIndices, indices, c * 30);
                    c++;
                    sum++;
                }
            }
        }
        System.out.println(sum + " of " + squares[0].length * squares.length + " rendered");
        System.out.println(c + " of " + squares[0].length * squares.length + " rendered");
        System.out.println(vertices.length/60);
        System.out.println(textureCoords.length/40);
        System.out.println(normals.length/60);
        System.out.println(indices.length/30);
        ByteBuffer texByteBuffer = loader.loadArrayToByteBuffer(biomeMap);
        biomes = loader.create2DTextureFromByteBuffer(texByteBuffer, heightMap.length, heightMap[0].length);
        texByteBuffer = loader.initPermTexture(perm, grad3);
        permTexture = loader.create2DTextureFromByteBuffer(texByteBuffer, 256, 256);
        texByteBuffer = loader.loadArrayToByteBuffer(simplex4);
        simplexTexture = loader.create1DTextureFromByteBuffer(texByteBuffer, simplex4.length / 4);

        rawModel = loader.createRawModel(vertices, indices, textureCoords, normals);
    }

    public float getHeightFromMapCoords(int x, int z) {
        try {
            return squares[x / 5][z / 5].getHeight();
        } catch (ArrayIndexOutOfBoundsException e) {
            return 0;
        }
    }

//    public float getHeightFromVertexOrder(int x, int z) {
//        try {
//            return getSquareFromVertexOrder(x, z).getHeightAbsCoords(x, z);
//        } catch (IllegalArgumentException e) {
//            return 0;
//        }
//    }

    public RawModel getRawModel() {
        return rawModel;
    }

    public void printPosition() {
        System.out.println("TerrainTile position: " + position.x + " " + position.y + " " + position.z);
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

    public static Texture getPermTexture() {
        return permTexture;
    }

    public static Texture getSimplexTexture() {
        return simplexTexture;
    }

    public RawModel getSquareOutlines() {
        int vcount = squares[0][0].getOutline().length;
        float[] outlines = new float[vcount * Game.SQUARES_PER_TILE];
        int i = 0;
        for (TerrainSquare[] squares2 : squares) {
            for (TerrainSquare square : squares2) {
                for (int j = 0; j < vcount; j++) {
                    outlines[i++] = square.getOutline()[j];
                }
            }
        }
        return loader.createRawModel(outlines, 3);
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
