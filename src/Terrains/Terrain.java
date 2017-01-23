package Terrains;

import Main.*;
import Utils.ArrayManipulation;
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
    private static Texture grassDesertMountainSnowMapTexture;
    private static Texture waterForestMapTexture;
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

    private RawModel rawModel;
    private Renderer renderer;
    private Vector3f position = new Vector3f(0,0,0);
    private float rx = 0, ry = 0, rz = 0, scale = 1f;
    private int tileX, tileZ;
    private float[] vertices;
    private int[] indices;
    private float[] textureCoords;
    private float[] normals;

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
                if (heightMap[j][i] <= 0f)
                    moistureMap[j][i] = 1f;
            }
        }
        float[][] ocean = moistureMap;
        int r1 = random.nextInt(2) * 2 - 1;
        for (int i = 0; i < 60; i++) {
            moistureMap = Wind.spreadMoisture(moistureMap, ocean, r1, -1);
            moistureMap = ArrayManipulation.clampf(moistureMap, 1f, 0f);
        }
        moistureMap = Wind.spreadMoisture(moistureMap, ocean, -r1, -1);
        moistureMap = ArrayManipulation.clampf(moistureMap, 1f, 0f);
        ArrayManipulation.gaussianBlur2Df(moistureMap);
        BiomeGenerator biomeGenerator = new BiomeGenerator();
        biomeGenerator.generateBiome(heightMap, moistureMap, grassMap, desertMap, mountainMap, snowMap, waterMap, forestMap);
        for (int i = 0; i < 15; i++) {
            ArrayManipulation.gaussianBlur2Df(grassMap);
            ArrayManipulation.gaussianBlur2Df(desertMap);
            ArrayManipulation.gaussianBlur2Df(waterMap);
            ArrayManipulation.gaussianBlur2Df(forestMap);
            ArrayManipulation.gaussianBlur2Df(snowMap);
        }
        ArrayManipulation.gaussianBlur2Df(mountainMap);

        mountainTexture = loader.createTextureFromImageFile("Rock1.png", 1024, 1024);
        grassTexture = loader.createTextureFromImageFile("grassland.png", 1024, 1024);
        forestTexture = loader.createTextureFromImageFile("forest.png", 1024, 1024);
        waterTexture = loader.createTextureFromImageFile("ocean.png", 1024, 1024);
        desertTexture = loader.createTextureFromImageFile("desert.png", 1024, 1024);
        snowTexture = loader.createTextureFromImageFile("snow.png", 512, 512);
        ByteBuffer texByteBuffer = loader.loadArrayToByteBuffer(grassMap, desertMap, mountainMap, snowMap);
        grassDesertMountainSnowMapTexture = loader.createTextureFromByteBuffer(texByteBuffer, heightMap.length, heightMap[0].length);
        texByteBuffer = loader.loadArrayToByteBuffer(waterMap, forestMap);
        waterForestMapTexture = loader.createTextureFromByteBuffer(texByteBuffer, heightMap.length, heightMap[0].length);
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
        return grassDesertMountainSnowMapTexture;
    }

    public static Texture getWaterForestMapTexture() {
        return waterForestMapTexture;
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


}
