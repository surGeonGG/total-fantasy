package Terrains;

import Main.*;
import Utils.ArrayManipulation;
import Utils.DiverseUtilities;
import org.joml.Vector3f;

import java.nio.ByteBuffer;
import java.util.Random;

public class Terrain {

    private static final int TEX_TO_MODEL_RATIO = 1;

    private static Texture mountainTexture;
    private static Texture grassTexture;
    private static Texture forestTexture;
    private static Texture waterTexture;
    private static Texture desertTexture;
    private static Texture snowTexture;
    private static Texture heightMoisture;
    private static Texture permTexture;
    private static Texture simplexTexture;
    private static float[][] heightMap;
    private static float[][] moistureMap;
    private static float[][] mountainMap;
    private static float[][] snowMap;
    private static float[][] forestMap;
    private static float[][] grassMap;
    private static float[][] desertMap;
    private static float[][] waterMap;
    private static SimplexNoiseGenerator simplexNoiseGenerator;
    private static Loader loader;
    private static float season = 1f;

    private RawModel rawModel;
    private Renderer renderer;
    private Vector3f position = new Vector3f(0,0,0);
    private float rx = 0, ry = 0, rz = 0, scale = 1f;
    private int tileX, tileZ;
    private float[] vertices;
    private int[] indices;
    private float[] textureCoords;
    private float[] normals;
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

    public Terrain(Renderer renderer, int tileX, int tileZ) {
        this.tileX = tileX;
        this.tileZ = tileZ;
        this.renderer = renderer;
        generateModel();
    }

    public static void generateTerrain() {
        simplexNoiseGenerator = new SimplexNoiseGenerator();
        loader = new Loader();
        heightMap = simplexNoiseGenerator.buildNoise(Game.WIDTH*TEX_TO_MODEL_RATIO, Game.HEIGHT*TEX_TO_MODEL_RATIO);
        moistureMap = new float[heightMap.length][heightMap[0].length];
        grassMap = new float[heightMap.length][heightMap[0].length];
        desertMap = new float[heightMap.length][heightMap[0].length];
        mountainMap = new float[heightMap.length][heightMap[0].length];
        snowMap = new float[heightMap.length][heightMap[0].length];
        waterMap = new float[heightMap.length][heightMap[0].length];
        forestMap = new float[heightMap.length][heightMap[0].length];
        Random random = new Random();
        for (int i = 0; i < heightMap.length; i++) {
            for (int j = 0; j < heightMap[i].length; j++) {
                moistureMap[j][i] = 0;
                if (heightMap[j][i] <= -0.05)
                    moistureMap[j][i] = 1f;
            }
        }
        float[][] ocean = moistureMap;
        int r1 = random.nextInt(2) * 2 - 1;
        for (int i = 0; i < 60; i++) {
            moistureMap = Wind.spreadMoisture(moistureMap, ocean, -1, -1);
            moistureMap = ArrayManipulation.clampf(moistureMap, 1f, 0f);
        }
        moistureMap = Wind.spreadMoisture(moistureMap, ocean, 1, -1);
        moistureMap = Wind.spreadMoisture(moistureMap, ocean, 1, -1);
        moistureMap = ArrayManipulation.clampf(moistureMap, 1f, 0f);
        ArrayManipulation.gaussianBlur2Df(moistureMap);
        BiomeGenerator biomeGenerator = new BiomeGenerator();
        biomeGenerator.generateBiome(heightMap, moistureMap, grassMap, desertMap, mountainMap, snowMap, waterMap, forestMap);
        float[][] mask = ArrayManipulation.getMask2Df(heightMap, 0.25f);
//        for (int i = 0; i < 20; i++) {
//            ArrayManipulation.maskedGaussianBlur2Df(grassMap, mask);
//            ArrayManipulation.maskedGaussianBlur2Df(desertMap, mask);
//            ArrayManipulation.maskedGaussianBlur2Df(snowMap, mask);
//            ArrayManipulation.maskedGaussianBlur2Df(forestMap, mask);
//        }
        mountainTexture = loader.createTextureFromImageFile("Rock1.png", 1024, 1024);
        grassTexture = loader.createTextureFromImageFile("grassland.png", 1024, 1024);
        forestTexture = loader.createTextureFromImageFile("forest.png", 1024, 1024);
        waterTexture = loader.createTextureFromImageFile("ocean.png", 1024, 1024);
        desertTexture = loader.createTextureFromImageFile("desert.png", 1024, 1024);
        snowTexture = loader.createTextureFromImageFile("snow.png", 512, 512);
//        ByteBuffer texByteBuffer = loader.loadArrayToByteBuffer(grassMap, desertMap, mountainMap, snowMap);
        float[][] heightMapCopy = DiverseUtilities.clampAndCopy(heightMap, 0f, 1f);
        ByteBuffer texByteBuffer = loader.loadArrayToByteBuffer(heightMapCopy, moistureMap);
        heightMoisture = loader.create2DTextureFromByteBuffer(texByteBuffer, heightMap.length, heightMap[0].length);
//        texByteBuffer = loader.loadArrayToByteBuffer(waterMap, forestMap);
//        waterForestMapTexture = loader.create2DTextureFromByteBuffer(texByteBuffer, heightMap.length, heightMap[0].length);
        texByteBuffer = loader.initPermTexture(perm, grad3);
        permTexture = loader.create2DTextureFromByteBuffer(texByteBuffer, 256, 256);
        texByteBuffer = loader.loadArrayToByteBuffer(simplex4);
        simplexTexture = loader.create1DTextureFromByteBuffer(texByteBuffer, simplex4.length/4);
    }

    public void generateModel() {
        int lengthX = (Game.WIDTH / Game.NUMBER_OF_TILES_X);
        int lengthZ = (Game.HEIGHT / Game.NUMBER_OF_TILES_Y);
        int numberOfVertices = lengthX * lengthZ;
        int numberOfIndices = (lengthX - 1) * (lengthZ - 1) * 6; //lengthX/Z - 1 = number of quads in axis
        vertices = new float[numberOfVertices * 3];
        textureCoords = new float[numberOfVertices * 2];
        normals = new float[numberOfVertices * 3];
        indices = new int[numberOfIndices];
        int vertexPointer = 0;
        int texCoordPointer = 0;
        int normalsPointer = 0;
        int x = (lengthX - 1) * tileX; // offset based on tile number
        int z = (lengthZ - 1) * tileZ;
        for (int i = x; i < x+lengthX; i++) {
            for (int j = z; j < z+lengthZ; j++) {
                vertices[vertexPointer++] = j;
                vertices[vertexPointer++] = heightMap[j*TEX_TO_MODEL_RATIO][i*TEX_TO_MODEL_RATIO] * Game.HEIGHT_MULTIPLIER;
                vertices[vertexPointer++] = i;
                textureCoords[texCoordPointer++] = ((float) j / ((float) heightMap[0].length - 1));
                textureCoords[texCoordPointer++] = ((float) i / ((float) heightMap.length - 1));
                Vector3f normal = calculateNormal(j, i, heightMap);
                normals[normalsPointer++] = normal.x;
                normals[normalsPointer++] = normal.y;
                normals[normalsPointer++] = normal.z;
            }
        }
        int indicesPointer = 0;
        for (int gz = 0; gz < lengthX - 1; gz++) {
            for (int gx = 0; gx < lengthZ - 1; gx++) {
                int topLeft = (gz * lengthX) + gx;
                int topRight = topLeft + 1;
                int bottomLeft = ((gz + 1) * lengthX) + gx;
                int bottomRight = bottomLeft + 1;
                indices[indicesPointer++] = topLeft;
                indices[indicesPointer++] = bottomLeft;
                indices[indicesPointer++] = topRight;
                indices[indicesPointer++] = topRight;
                indices[indicesPointer++] = bottomLeft;
                indices[indicesPointer++] = bottomRight;
            }
        }
        rawModel = loader.createRawModel(vertices, indices, textureCoords, normals);
    }

    private Vector3f calculateNormal(int x, int z, float[][] heightMap) {
        float heightL = getHeight(x - 1, z);
        float heightR = getHeight(x + 1, z);
        float heightD = getHeight(x, z - 1);
        float heightU = getHeight(x, z + 1);
        Vector3f normal = new Vector3f(heightL - heightR, 2f, heightD - heightU);
        normal.normalize();
        return normal;
    }

    public void rebuild() {
        generateTerrain();
        generateModel();
    }


    public static float getHeight(int x, int z) {
        if (x*TEX_TO_MODEL_RATIO < 0 || x*TEX_TO_MODEL_RATIO >= heightMap.length || z*TEX_TO_MODEL_RATIO < 0 || z*TEX_TO_MODEL_RATIO >= heightMap[0].length) {
            return 0;
        }
        return heightMap[x*TEX_TO_MODEL_RATIO][z*TEX_TO_MODEL_RATIO] * Game.HEIGHT_MULTIPLIER;
    }

    public RawModel getRawModel() {
        return rawModel;
    }

    public Texture getMountainTexture() {
        return mountainTexture;
    }

    public Texture getGrassTexture() {
        return grassTexture;
    }

    public Texture getWaterTexture() {
        return waterTexture;
    }

    public static Texture getForestTexture() {
        return forestTexture;
    }

    public Texture getDesertTexture() {
        return desertTexture;
    }

    public static Texture getSnowTexture() {
        return snowTexture;
    }

    public Texture getGrassDesertMountainSnowMapTexture() {
        return heightMoisture;
    }

    public static Texture getHeightMoisture() {
        return heightMoisture;
    }

    public static Texture getPermTexture() {
        return permTexture;
    }

    public static Texture getSimplexTexture() {
        return simplexTexture;
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

    public float getSeason() {
        return (float) (Math.sin(season) + 1) / 2;
    }

    public void changeSeason(float season) {
        this.season += season;
    }
}
