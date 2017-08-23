package Terrains;

import Main.*;
import Utils.ArrayManipulation;
import org.joml.Vector3f;

import java.nio.ByteBuffer;
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
        vertices = new float[Game.VERTICES_PER_TILE * 3];
        textureCoords = new float[Game.VERTICES_PER_TILE * 2];
        normals = new float[Game.VERTICES_PER_TILE * 3];
        simplexNoiseGenerator = new SimplexNoiseGenerator();
        buildHeightMap();
        buildMoistureMap();
        buildSpecialMap();
        buildSquares();
        generateModel();
    }

    private void buildMoistureMap() {
        moistureMap = new float[Game.X_SQUARES_PER_TILE][Game.Z_SQUARES_PER_TILE];
        for (int i = 0; i < heightMap[0].length; i++) {
            for (int j = 0; j < heightMap.length; j++) {
                moistureMap[j][i] = 0f;
                if (heightMap[j][i] < SimplexNoiseGenerator.OCEAN_LIMIT) {
                    moistureMap[j][i] = 1f;
                }
            }
        }
        float[][] ocean = moistureMap;
//        int r1 = random.nextInt(2) * 2 - 1;
        for (int i = 0; i < 25; i++) {
            moistureMap = Wind.spreadMoisture(moistureMap, ocean, -1, -1);
            moistureMap = ArrayManipulation.clampf(moistureMap, 1f, 0f);
        }
        moistureMap = Wind.spreadMoisture(moistureMap, ocean, 1, -1);
        moistureMap = Wind.spreadMoisture(moistureMap, ocean, 1, -1);
        moistureMap = ArrayManipulation.clampf(moistureMap, 1f, 0f);
    }

    private void buildHeightMap() {
        heightMap = simplexNoiseGenerator.buildHeightMap(Game.X_SQUARES_PER_TILE,
                Game.Z_SQUARES_PER_TILE);

//        heightMap = simplexNoiseGenerator.generateForest(random.nextInt(9999), heightMap, moistureMap);
    }

    private void buildSpecialMap() {
        specialMap = simplexNoiseGenerator.generateNoise(random.nextInt(), Game.X_SQUARES_PER_TILE,
                Game.Z_SQUARES_PER_TILE, 6, 1);
    }

    private void buildSquares() {
        biomeMap = new int[heightMap.length][heightMap[0].length];
        for (int i = 0; i < heightMap[0].length; i++) {
            for (int j = 0; j < heightMap.length; j++) {
                float[][] localHeightMap = simplexNoiseGenerator.buildMapHeight(Game.X_VERTICES_PER_SQUARE,
                        Game.Z_VERTICES_PER_SQUARE, heightMap[j][i]);
                int biome = BiomeGenerator.generateBiome(heightMap[j][i], moistureMap[j][i], specialMap[j][i]);
                biomeMap[j][i] = biome;
                System.out.println(biome);
                squares[j][i] = new TerrainSquare(localHeightMap, j, i, biome);
            }
        }
    }




    public void generateModel() {

        int vertexPointer = 0;
        int texCoordPointer = 0;
        int normalsPointer = 0;
        for (int i = 0; i < Game.Z_VERTICES_PER_TILE; i++) {
            int z = convertZToMapCoord(i);
            for (int j = 0; j < Game.X_VERTICES_PER_TILE; j++) {
                int x = convertXToMapCoord(j);
                vertices[vertexPointer++] = x;
                vertices[vertexPointer++] = getSquareFromVertexOrder(j, i).getHeightAbsCoords(j, i);
                vertices[vertexPointer++] = z;
                textureCoords[texCoordPointer++] = ((float) x / ((float) Game.TILE_WIDTH));
                textureCoords[texCoordPointer++] = ((float) z / ((float) Game.TILE_HEIGHT));
                Vector3f normal = calculateNormal(j, i);
                normals[normalsPointer++] = normal.x;
                normals[normalsPointer++] = normal.y;
                normals[normalsPointer++] = normal.z;
            }
        }
        int numberOfIndices = (Game.Z_VERTICES_PER_TILE - 1) * (Game.X_VERTICES_PER_TILE - 1) * 6; //xPerSquare/Z - 1 = number of quads in axis
        indices = new int[numberOfIndices];
        int indicesPointer = 0;
        for (int i = 0; i < Game.Z_VERTICES_PER_TILE - 1; i++) {
            for (int j = 0; j < Game.X_VERTICES_PER_TILE - 1; j++) {
                int topLeft = getSquareFromVertexOrder(j, i).getIndex(j, i);
                int topRight = getSquareFromVertexOrder(j + 1, i).getIndex(j + 1, i);
                int bottomLeft = getSquareFromVertexOrder(j, i + 1).getIndex(j, i + 1);
                int bottomRight = getSquareFromVertexOrder(j + 1, i + 1).getIndex(j + 1, i + 1);
                indices[indicesPointer++] = topLeft;
                indices[indicesPointer++] = bottomLeft;
                indices[indicesPointer++] = topRight;
                indices[indicesPointer++] = topRight;
                indices[indicesPointer++] = bottomLeft;
                indices[indicesPointer++] = bottomRight;
            }
        }
        ByteBuffer texByteBuffer = loader.loadArrayToByteBuffer(biomeMap);
        biomes = loader.create2DTextureFromByteBuffer(texByteBuffer, heightMap.length, heightMap[0].length);
        texByteBuffer = loader.initPermTexture(perm, grad3);
        permTexture = loader.create2DTextureFromByteBuffer(texByteBuffer, 256, 256);
        texByteBuffer = loader.loadArrayToByteBuffer(simplex4);
        simplexTexture = loader.create1DTextureFromByteBuffer(texByteBuffer, simplex4.length / 4);

        rawModel = loader.createRawModel(vertices, indices, textureCoords, normals);
    }


    private Vector3f calculateNormal(int x, int z) {
        float heightL = getHeightFromVertexOrder(x - 1, z);
        float heightR = getHeightFromVertexOrder(x + 1, z);
        float heightD = getHeightFromVertexOrder(x, z - 1);
        float heightU = getHeightFromVertexOrder(x, z + 1);
        Vector3f normal = new Vector3f(heightL - heightR, 2f, heightD - heightU);
        normal.normalize();
        return normal;
    }

    public float getHeightFromMapCoords(int x, int z) {
        try {
            return getSquareFromVertexOrder(convertXToVertexOrder(x), convertZToVertexOrder(z)).getHeightAbsCoords(x, z);
        } catch (IllegalArgumentException e) {
            return 0;
        }
    }

    public float getHeightFromVertexOrder(int x, int z) {
        try {
            return getSquareFromVertexOrder(x, z).getHeightAbsCoords(x, z);
        } catch (IllegalArgumentException e) {
            return 0;
        }
    }

    public TerrainSquare getSquareFromVertexOrder(int x, int z) {
        if (x < 0 || x >= Game.X_VERTICES_PER_TILE || z < 0 || z >= Game.Z_VERTICES_PER_TILE) {
            throw new IllegalArgumentException("x was " + x + " and z was " + z + ". They must be between 0 and " + Game.TILE_WIDTH);
        }
        int i = Math.floorDiv(x, Game.X_VERTICES_PER_SQUARE);
        int j = Math.floorDiv(z, Game.Z_VERTICES_PER_SQUARE);
        return squares[i][j];
    }

    public int convertXToMapCoord(int x) {
        int xOffset = Math.floorDiv(x, Game.X_VERTICES_PER_SQUARE);
        return x - xOffset;
    }

    public int convertZToMapCoord(int z) {
        int zOffset = Math.floorDiv(z, Game.Z_VERTICES_PER_SQUARE);
        return z - zOffset;
    }

    public int convertXToVertexOrder(int x) {
        int xOffset = Math.floorDiv(x, Game.X_VERTICES_PER_SQUARE_M1);
        return x + xOffset;
    }

    public int convertZToVertexOrder(int z) {
        int zOffset = Math.floorDiv(z, Game.Z_VERTICES_PER_SQUARE_M1);
        return z + zOffset;
    }

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
}
