package Terrains;

import Main.*;
import Utils.ArrayManipulation;
import org.joml.Vector3f;

import java.nio.ByteBuffer;
import java.util.Random;

public class TerrainTile {
    private static float[][] heightMap;
    private static float[][] moistureMap;
    private static Texture heightMoisture;
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
        generateModel();
    }

    public void generateModel() {
        simplexNoiseGenerator = new SimplexNoiseGenerator();
        loader = new Loader();
        heightMap = simplexNoiseGenerator.buildNoise(Game.X_SQUARES_PER_TILE * Game.NUMBER_OF_TILES_X,
                Game.Z_SQUARES_PER_TILE * Game.NUMBER_OF_TILES_Y);
        moistureMap = new float[Game.X_SQUARES_PER_TILE][Game.Z_SQUARES_PER_TILE];
        squares = new TerrainSquare[Game.X_SQUARES_PER_TILE][Game.Z_SQUARES_PER_TILE];
        vertices = new float[Game.VERTICES_PER_TILE * 3];
        textureCoords = new float[Game.VERTICES_PER_TILE * 2];
        normals = new float[Game.VERTICES_PER_TILE * 3];
        int numberOfIndices = (Game.TILE_HEIGHT - 1) * (Game.TILE_WIDTH - 1) * 6; //xPerSquare/Z - 1 = number of quads in axis
        indices = new int[numberOfIndices];
        for (int i = 0; i < heightMap[0].length; i++) {
            for (int j = 0; j < heightMap.length; j++) {
                moistureMap[j][i] = 0f;
                if (heightMap[j][i] < SimplexNoiseGenerator.OCEAN_LIMIT)
                    moistureMap[j][i] = 1f;
            }
        }
        float[][] ocean = moistureMap;
        int r1 = random.nextInt(2) * 2 - 1;
//        for (int i = 0; i < 0; i++) {
//            moistureMap = Wind.spreadMoisture(moistureMap, ocean, -1, -1);
//            moistureMap = ArrayManipulation.clampf(moistureMap, 1f, 0f);
//        }
//        moistureMap = Wind.spreadMoisture(moistureMap, ocean, 1, -1);
//        moistureMap = Wind.spreadMoisture(moistureMap, ocean, 1, -1);
//        moistureMap = ArrayManipulation.clampf(moistureMap, 1f, 0f);
        for (int i = 0; i < heightMap[0].length; i++) {
            for (int j = 0; j < heightMap.length; j++) {
                float[][] localHeightMap = simplexNoiseGenerator.buildBiome(Game.X_VERTICES_PER_SQUARE,
                        Game.Z_VERTICES_PER_SQUARE, heightMap[j][i]);
                float heightMultiplier = 1;
                String biome = BiomeGenerator.generateBiome(heightMap[j][i], moistureMap[j][i]);
                squares[j][i] = new TerrainSquare(localHeightMap, heightMultiplier, j, i, biome);
            }
        }
        int vertexPointer = 0;
        int texCoordPointer = 0;
        int normalsPointer = 0;
        for (int i = 0; i < Game.TILE_HEIGHT; i++) {
            int z = i;
            for (int j = 0; j < Game.TILE_WIDTH; j++) {
                int x = j;
                vertices[vertexPointer++] = x;
                vertices[vertexPointer++] = getSquare(x, z).getHeightAbsCoords(x, z);
                vertices[vertexPointer++] = z;
                textureCoords[texCoordPointer++] = ((float) x / ((float) Game.TILE_WIDTH - 1));
                textureCoords[texCoordPointer++] = ((float) z / ((float) Game.TILE_HEIGHT - 1));
                Vector3f normal = calculateNormal(x, z);
                normals[normalsPointer++] = normal.x;
                normals[normalsPointer++] = normal.y;
                normals[normalsPointer++] = normal.z;
            }
        }
        int indicesPointer = 0;
        for (int i = 0; i < Game.TILE_HEIGHT - 1; i++) {
            for (int j = 0; j < Game.TILE_WIDTH - 1; j++) {
                int topLeft = getSquare(j, i).getIndex(j, i);
                int topRight = getSquare(j+1, i).getIndex(j+1, i);
                int bottomLeft = getSquare(j, i+1).getIndex(j, i+1);
                int bottomRight = getSquare(j+1, i+1).getIndex(j+1, i+1);
                indices[indicesPointer++] = topLeft;
                indices[indicesPointer++] = bottomLeft;
                indices[indicesPointer++] = topRight;
                indices[indicesPointer++] = topRight;
                indices[indicesPointer++] = bottomLeft;
                indices[indicesPointer++] = bottomRight;
            }
        }

        ByteBuffer texByteBuffer = loader.loadArrayToByteBuffer(heightMap, moistureMap);
        heightMoisture = loader.create2DTextureFromByteBuffer(texByteBuffer, heightMap.length, heightMap[0].length);
        texByteBuffer = loader.initPermTexture(perm, grad3);
        permTexture = loader.create2DTextureFromByteBuffer(texByteBuffer, 256, 256);
        texByteBuffer = loader.loadArrayToByteBuffer(simplex4);
        simplexTexture = loader.create1DTextureFromByteBuffer(texByteBuffer, simplex4.length/4);

        rawModel = loader.createRawModel(vertices, indices, textureCoords, normals);
    }

    private Vector3f calculateNormal(int x, int z) {
        float heightL = getHeight(x - 1, z);
        float heightR = getHeight(x + 1, z);
        float heightD = getHeight(x, z - 1);
        float heightU = getHeight(x, z + 1);
        Vector3f normal = new Vector3f(heightL - heightR, 2f, heightD - heightU);
        normal.normalize();
        return normal;
    }

    public float getHeight(int x, int z) {
        try {
            return getSquare(x, z).getHeightAbsCoords(x, z);
        } catch (IllegalArgumentException e) {
            return 0;
        }
    }

    public TerrainSquare getSquare(int x, int z) {
        if (x < 0 || x >= Game.TILE_WIDTH || z < 0 || z >= Game.TILE_HEIGHT) {
            throw new IllegalArgumentException("x was " + x + " and z was " + z + ". They must be between 0 and " + Game.TILE_WIDTH);
        }
        int i = Math.floorDiv(x, Game.X_VERTICES_PER_SQUARE);
        int j = Math.floorDiv(z, Game.Z_VERTICES_PER_SQUARE);
        return squares[i][j];
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

    public Texture getHeightMoisture() {
        return heightMoisture;
    }
}
