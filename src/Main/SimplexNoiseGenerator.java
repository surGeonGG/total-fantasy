package Main;


import Utils.DiverseUtilities;

import java.util.Random;

public class SimplexNoiseGenerator {

    public static final float OCEAN_LIMIT = 0.5f;
    public static final float FOREST_LIMIT = 0.6f;
    public static final float FOREST_MOISTURE_LIMIT = 0.6f;
    public static final float FLATLAND_LIMIT = 0.8f;
    public static final float MOUNTAIN_LIMIT = 0.9f;
    public static final float TALL_MOUNTAIN_LIMIT = 0.95f;
    public static final float TALLER_MOUNTAIN_LIMIT = 0.999f;

    public static final float OCEAN_HEIGHT_MULTIPLIER = 0f;
    public static final float FLATLAND_HEIGHT_MULTIPLIER = 1f;
    public static final float MOUNTAIN_HEIGHT = 3f;
    public static final float TALL_MOUNTAIN_HEIGHT = 6f;
    public static final float TALLER_MOUNTAIN_HEIGHT = 9f;

    private static int min = -10, max = 10;
    private static Random rand = new Random();
    private static float MOISTURE_SCATTERING = 0.02f, ELEVATION_SCATTERING = 0.2f;

    public SimplexNoiseGenerator() {

    }

    public float[][] buildHeightMap(int width, int height) {
        float[][] islandShape = generateIslandShape(rand.nextInt(), width, height);
        float[][] island = generateElevation(rand.nextInt(), islandShape, width, height);
        return island;
    }

    public float[][] generateIslandShape(int seed, int width, int height) {
        new SimplexNoise(seed);
        float[][] tempMap = new float[width][height];
        float freq = 3f;
        float weight = 1f;
        float e0 = 1f;
        float e1 = 0.5f;
        float e2 = (float) (weight * Math.pow(0.5, 2));
        float e3 = (float) (weight * Math.pow(0.5, 3));
        float e4 = (float) (weight * Math.pow(0.5, 4));
        float e5 = (float) (weight * Math.pow(0.5, 5));
        float e6 = (float) (weight * Math.pow(0.5, 6));
        float e7 = (float) (weight * Math.pow(0.5, 7));
        float e8 = (float) (weight * Math.pow(0.5, 8));
        float e9 = (float) (weight * Math.pow(0.5, 9));
        float e10 = (float) (weight * Math.pow(0.5, 10));
        for (int x = 0; x < width; x++) {
            for (int y = 0; y < height; y++) {
                float nx = (float) x / width, ny = (float) y / height;
                tempMap[x][y] = (float) (e0 * SimplexNoise.noise(nx * freq, ny * freq)
                        + e1 * SimplexNoise.noise(nx * 2 * freq, ny * 2 * freq)
                        + e2 * SimplexNoise.noise(nx * 4 * freq, ny * 4 * freq)
                        + e3 * SimplexNoise.noise(nx * 8 * freq, ny * 8 * freq)
                        + e4 * SimplexNoise.noise(nx * 16 * freq, ny * 16 * freq)
                        + e5 * SimplexNoise.noise(nx * 32 * freq, ny * 32 * freq)
                        + e6 * SimplexNoise.noise(nx * 64 * freq, ny * 64 * freq)
                        + e7 * SimplexNoise.noise(nx * 128 * freq, ny * 128 * freq)
                        + e8 * SimplexNoise.noise(nx * 256 * freq, ny * 256 * freq)
                        + e9 * SimplexNoise.noise(nx * 512 * freq, ny * 512 * freq)
                        + e10 * SimplexNoise.noise(nx * 1024 * freq, ny * 1024 * freq));
                tempMap[x][y] = tempMap[x][y] + (((float) Math.random() * 2 - 1) * 0.1f) + 0.6f;
                tempMap[x][y] = adjust(tempMap[x][y]);

                float halfWidth = width / 2;
                float halfHeight = height / 2;
                float xDist = Math.abs((x - halfWidth) / halfWidth);
                float yDist = Math.abs((y - halfHeight) / halfHeight);
                float dist = (float) Math.hypot(xDist, yDist);
                tempMap[x][y] -= Math.pow(dist, 3);

                if (tempMap[x][y] > OCEAN_LIMIT)
                    tempMap[x][y] = 0.6f;
                else
                    tempMap[x][y] = 0;
            }
        }
        return tempMap;
    }

    public float[][] generateElevation(int seed, float[][] islandShape, int width, int height) {
        new SimplexNoise(seed);
        float[][] tempMap = new float[width][height];
        float freq = 6f;
        float weight = 1f;
        float e0 = 1f;
        float e1 = 0.5f;
        float e2 = (float) (weight * Math.pow(0.5, 2));
        float e3 = (float) (weight * Math.pow(0.5, 3));
        float e4 = (float) (weight * Math.pow(0.5, 4));
        float e5 = (float) (weight * Math.pow(0.5, 5));
        float e6 = (float) (weight * Math.pow(0.5, 6));
        float e7 = (float) (weight * Math.pow(0.5, 7));
        float e8 = (float) (weight * Math.pow(0.5, 8));
        float e9 = (float) (weight * Math.pow(0.5, 9));
        float e10 = (float) (weight * Math.pow(0.5, 10));
        for (int x = 0; x < width; x++) {
            for (int y = 0; y < height; y++) {
                if (islandShape[x][y] < OCEAN_LIMIT) {
                    tempMap[x][y] = -1;
                    continue;
                }
                float nx = (float) x / width, ny = (float) y / height;
                tempMap[x][y] = (float) (e0 * SimplexNoise.noise(nx * freq, ny * freq)
                        + e1 * SimplexNoise.noise(nx * 2 * freq, ny * 2 * freq)
                        + e2 * SimplexNoise.noise(nx * 4 * freq, ny * 4 * freq)
                        + e3 * SimplexNoise.noise(nx * 8 * freq, ny * 8 * freq)
                        + e4 * SimplexNoise.noise(nx * 16 * freq, ny * 16 * freq)
                        + e5 * SimplexNoise.noise(nx * 32 * freq, ny * 32 * freq)
                        + e6 * SimplexNoise.noise(nx * 64 * freq, ny * 64 * freq)
                        + e7 * SimplexNoise.noise(nx * 128 * freq, ny * 128 * freq)
                        + e8 * SimplexNoise.noise(nx * 256 * freq, ny * 256 * freq)
                        + e9 * SimplexNoise.noise(nx * 512 * freq, ny * 512 * freq)
                        + e10 * SimplexNoise.noise(nx * 1024 * freq, ny * 1024 * freq));
                tempMap[x][y] += (((float) Math.random() * 2 - 1) * ELEVATION_SCATTERING);
                tempMap[x][y] = adjust(tempMap[x][y]);
                tempMap[x][y] = DiverseUtilities.clamp(tempMap[x][y], 0.1f, 1);
            }
        }
        return tempMap;
    }

    public float[][] buildMapHeight(int width, int height, float biome) {
        float[][] islandShape;
        if (biome < 0.1f) {
            islandShape = generateOcean(rand.nextInt(), width, height);
        } else if (biome < FLATLAND_LIMIT) {
            islandShape = generateTerrain(rand.nextInt(), width, height, 0.1f);
        } else if (biome < MOUNTAIN_LIMIT) {
            islandShape = generateTerrain(rand.nextInt(), width, height, MOUNTAIN_HEIGHT);
        } else if (biome < TALL_MOUNTAIN_LIMIT) {
            islandShape = generateTerrain(rand.nextInt(), width, height, TALL_MOUNTAIN_HEIGHT);
        } else {
            islandShape = generateTerrain(rand.nextInt(), width, height, TALLER_MOUNTAIN_HEIGHT);
        }
        return islandShape;
    }

    public float[][] generateOcean(int seed, int width, int height) {
        float[][] tempMap = new float[width][height];
        for (int x = 0; x < width; x++) {
            for (int y = 0; y < height; y++) {
                tempMap[x][y] = -1f;
            }
        }
        return tempMap;
    }

    public float[][] generateTerrain(int seed, int width, int height, float terrainHeight) {
        new SimplexNoise(seed);
        float[][] tempMap = new float[width][height];
        float freq = 2f;
        float weight = 1f;
        float e0 = 1f;
        float e1 = 0.5f;
        float e2 = (float) (weight * Math.pow(0.5, 2));
        float e3 = (float) (weight * Math.pow(0.5, 3));
        float e4 = (float) (weight * Math.pow(0.5, 4));
        float e5 = (float) (weight * Math.pow(0.5, 5));
        float e6 = (float) (weight * Math.pow(0.5, 6));
        float e7 = (float) (weight * Math.pow(0.5, 7));
        float e8 = (float) (weight * Math.pow(0.5, 8));
        float e9 = (float) (weight * Math.pow(0.5, 9));
        float e10 = (float) (weight * Math.pow(0.5, 10));
        for (int x = 0; x < width; x++) {
            for (int y = 0; y < height; y++) {
                float nx = (float) x / width, ny = (float) y / height;
                tempMap[x][y] = (float) (e0 * SimplexNoise.noise(nx * freq, ny * freq)
                        + e1 * SimplexNoise.noise(nx * 2 * freq, ny * 2 * freq)
                        + e2 * SimplexNoise.noise(nx * 4 * freq, ny * 4 * freq)
                        + e3 * SimplexNoise.noise(nx * 8 * freq, ny * 8 * freq)
                        + e4 * SimplexNoise.noise(nx * 16 * freq, ny * 16 * freq)
                        + e5 * SimplexNoise.noise(nx * 32 * freq, ny * 32 * freq)
                        + e6 * SimplexNoise.noise(nx * 64 * freq, ny * 64 * freq)
                        + e7 * SimplexNoise.noise(nx * 128 * freq, ny * 128 * freq)
                        + e8 * SimplexNoise.noise(nx * 256 * freq, ny * 256 * freq)
                        + e9 * SimplexNoise.noise(nx * 512 * freq, ny * 512 * freq)
                        + e10 * SimplexNoise.noise(nx * 1024 * freq, ny * 1024 * freq) * 6);
                tempMap[x][y] = DiverseUtilities.clamp(tempMap[x][y], OCEAN_LIMIT+0.1f, 99f);
                tempMap[x][y] = adjust(tempMap[x][y]);
                tempMap[x][y] = terrainHeight;
            }
        }
        return tempMap;
    }

    private static float adjust(float val) {
        return (val + 1) / 2;
    }




    public float[][] generateNoise(int seed, int width, int height, float freq, float weight) {
        new SimplexNoise(seed);
        float[][] tempMap = new float[width][height];

        float e0 = 1f;
        float e1 = 0.5f;
        float e2 = (float) (weight * Math.pow(0.5, 2));
        float e3 = (float) (weight * Math.pow(0.5, 3));
        float e4 = (float) (weight * Math.pow(0.5, 4));
        float e5 = (float) (weight * Math.pow(0.5, 5));
        float e6 = (float) (weight * Math.pow(0.5, 6));
        float e7 = (float) (weight * Math.pow(0.5, 7));
        float e8 = (float) (weight * Math.pow(0.5, 8));
        float e9 = (float) (weight * Math.pow(0.5, 9));
        float e10 = (float) (weight * Math.pow(0.5, 10));
        for (int x = 0; x < width; x++) {
            for (int y = 0; y < height; y++) {
                float nx = (float) x / width, ny = (float) y / height;
                tempMap[x][y] = (float) (e0 * SimplexNoise.noise(nx * freq, ny * freq)
                        + e1 * SimplexNoise.noise(nx * 2 * freq, ny * 2 * freq)
                        + e2 * SimplexNoise.noise(nx * 4 * freq, ny * 4 * freq)
                        + e3 * SimplexNoise.noise(nx * 8 * freq, ny * 8 * freq)
                        + e4 * SimplexNoise.noise(nx * 16 * freq, ny * 16 * freq)
                        + e5 * SimplexNoise.noise(nx * 32 * freq, ny * 32 * freq)
                        + e6 * SimplexNoise.noise(nx * 64 * freq, ny * 64 * freq)
                        + e7 * SimplexNoise.noise(nx * 128 * freq, ny * 128 * freq)
                        + e8 * SimplexNoise.noise(nx * 256 * freq, ny * 256 * freq)
                        + e9 * SimplexNoise.noise(nx * 512 * freq, ny * 512 * freq)
                        + e10 * SimplexNoise.noise(nx * 1024 * freq, ny * 1024 * freq));
                tempMap[x][y] = adjust(tempMap[x][y]);
                tempMap[x][y] = DiverseUtilities.clamp(tempMap[x][y], 0f, 1f);
            }
        }
        return tempMap;
    }

}
