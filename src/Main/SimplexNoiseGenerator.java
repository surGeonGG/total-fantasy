package Main;


import Utils.DiverseUtilities;
import com.sun.org.apache.xpath.internal.operations.Div;

import java.util.Random;

public class SimplexNoiseGenerator {

    public static final float OCEAN_LIMIT = 0.4f;
    public static final float FLATLAND_LIMIT = 0.6f;
    public static final float MOUNTAIN_LIMIT = 0.8f;
    public static final float HIGH_MOUNTAIN_LIMIT = 0.9f;
//    public static final float MOUNTAIN_LIMIT = -0.1f;

    private static int min = -10, max = 10;
    private static Random rand = new Random();
    private static float MOISTURE_SCATTERING = 0.02f, ELEVATION_SCATTERING = 0.00f;


    public SimplexNoiseGenerator() {

    }


    public float[][] buildBiome(int width, int height, float biome) {
        float[][] islandShape;
        if (biome < OCEAN_LIMIT) {
            islandShape = generateOcean(rand.nextInt(), width, height);
        }
        else if (biome < FLATLAND_LIMIT) {
            islandShape = generateLand(rand.nextInt(), width, height);
        } else if (biome < MOUNTAIN_LIMIT) {
            islandShape = generateMountain(rand.nextInt(), width, height);
        } else {
            islandShape = generateHighMountain(rand.nextInt(), width, height);
        }
        return islandShape;
    }

    public float[][] generateOcean(int seed, int width, int height) {
        float[][] tempMap = new float[width][height];
        for (int x = 0; x < width; x++) {
            for (int y = 0; y < height; y++) {
                tempMap[x][y] = 0.2f;
            }
        }
        return tempMap;
    }

    public float[][] generateLand(int seed, int width, int height) {
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
                        + e10 * SimplexNoise.noise(nx * 1024 * freq, ny * 1024 * freq)) / 10 + 0.1f;
                tempMap[x][y] = DiverseUtilities.clamp(tempMap[x][y], OCEAN_LIMIT + 0.05f, 99f);
                tempMap[x][y] = adjust(tempMap[x][y]);
            }
        }
        return tempMap;
    }

    public float[][] generateMountain(int seed, int width, int height) {
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
//                float nx = (float) x / width, ny = (float) y / height;
//                tempMap[x][y] = (float) (e0 * SimplexNoise.noise(nx * freq, ny * freq)
//                        + e1 * SimplexNoise.noise(nx * 2 * freq, ny * 2 * freq)
//                        + e2 * SimplexNoise.noise(nx * 4 * freq, ny * 4 * freq)
//                        + e3 * SimplexNoise.noise(nx * 8 * freq, ny * 8 * freq)
//                        + e4 * SimplexNoise.noise(nx * 16 * freq, ny * 16 * freq)
//                        + e5 * SimplexNoise.noise(nx * 32 * freq, ny * 32 * freq)
//                        + e6 * SimplexNoise.noise(nx * 64 * freq, ny * 64 * freq)
//                        + e7 * SimplexNoise.noise(nx * 128 * freq, ny * 128 * freq)
//                        + e8 * SimplexNoise.noise(nx * 256 * freq, ny * 256 * freq)
//                        + e9 * SimplexNoise.noise(nx * 512 * freq, ny * 512 * freq)
//                        + e10 * SimplexNoise.noise(nx * 1024 * freq, ny * 1024 * freq) * 6);
//                tempMap[x][y] = DiverseUtilities.clamp(tempMap[x][y], OCEAN_LIMIT+0.1f, 99f);
//                tempMap[x][y] = adjust(tempMap[x][y]);
                tempMap[x][y] = MOUNTAIN_LIMIT-0.01f;
            }
        }
        return tempMap;
    }

    private float[][] generateHighMountain(int i, int width, int height) {
        float[][] tempMap = new float[width][height];
        for (int x = 0; x < width; x++) {
            for (int y = 0; y < height; y++) {
                tempMap[x][y] = HIGH_MOUNTAIN_LIMIT-0.01f;
            }
        }
        return tempMap;
    }

    public float[][] generateElevation(int seed, int width, int height) {
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
                        + e10 * SimplexNoise.noise(nx * 1024 * freq, ny * 1024 * freq));
//                tempMap[x][y] = DiverseUtilities.clamp(tempMap[x][y], -1f, 1);
                tempMap[x][y] = adjust(tempMap[x][y]);
            }
        }
        return tempMap;
    }




    //    public float[][] generateElevation(int seed, int width, int height) {
    private static float adjust(float val) {
        return (val + 1) / 2;
    }

    //    }
//        return tempMap;
//        }
//            }
//
//                tempMap[x][y] *= 2;
//                }
//                    tempMap[x][y] = (float) Math.pow(tempMap[x][y], 2);
//                if (tempMap[x][y] > 0) {
//                tempMap[x][y] -= Math.pow(dist+0.5f, 3);
//                float dist = (float)(Math.sqrt(Math.pow(Math.abs(xDist - 0.5f),2) + Math.pow(Math.abs(yDist - 0.5f),2)));
//                float yDist = (float) y / height;
//                float xDist = (float) x / width;
//                //distance from center
////
//
//                    tempMap[x][y] = tempMap[x][y] + (rand.nextFloat() - 0.5f) * ELEVATION_SCATTERING;
//                if (tempMap[x][y] > BiomeGenerator.OCEAN_LEVEL+0.05f)
//                tempMap[x][y] = (float) Math.pow(tempMap[x][y], 2);
//                tempMap[x][y] = adjust(tempMap[x][y]);
//                tempMap[x][y] = tempMap[x][y] + 0.4f;
//                tempMap[x][y] = tempMap[x][y] / (e0+e1+e2+e3+e4+e5+e6+e7+e8+e9+e10);
//
//
//
//
////                tempMap[x][y] = DiverseUtilities.clamp(tempMap[x][y], min, 0.25f);
//////                if (tempMap[x][y] <= 0) tempMap[x][y] = -10;
////                tempMap[x][y] /= 3;
////                tempMap[x][y] -= Math.pow(dist+0.5f, 3);
////                float dist = (float)(Math.sqrt(Math.pow(Math.abs(xDist - 0.5f),2) + Math.pow(Math.abs(yDist - 0.5f),2)));
////                float yDist = (float) y / height;
////                float xDist = (float) x / width;
////                //distance from center
////
////                    tempMap[x][y] = tempMap[x][y] + (rand.nextFloat() - 0.5f) * ELEVATION_SCATTERING;
////                if (tempMap[x][y] > BiomeGenerator.OCEAN_LEVEL+0.05f)
////                tempMap[x][y] = (float) Math.pow(tempMap[x][y], 2);
////                tempMap[x][y] = adjust(tempMap[x][y]);
////                tempMap[x][y] = tempMap[x][y] + 0.4f;
////                tempMap[x][y] = tempMap[x][y] / (e0+e1+e2+e3+e4+e5+e6+e7+e8+e9+e10);
//                        + e10 * SimplexNoise.noise(nx * 1024 * freq, ny * 1024 * freq));
//                        + e9 * SimplexNoise.noise(nx * 512 * freq, ny * 512 * freq)
//                        + e8 * SimplexNoise.noise(nx * 256 * freq, ny * 256 * freq)
//                        + e7 * SimplexNoise.noise(nx * 128 * freq, ny * 128 * freq)
//                        + e6 * SimplexNoise.noise(nx * 64 * freq, ny * 64 * freq)
//                        + e5 * SimplexNoise.noise(nx * 32 * freq, ny * 32 * freq)
//                        + e4 * SimplexNoise.noise(nx * 16 * freq, ny * 16 * freq)
//                        + e3 * SimplexNoise.noise(nx * 8 * freq, ny * 8 * freq)
//                        + e2 * SimplexNoise.noise(nx * 4 * freq, ny * 4 * freq)
//                        + e1 * SimplexNoise.noise(nx * 2 * freq, ny * 2 * freq)
//                tempMap[x][y] = (float) (e0 * SimplexNoise.noise(nx * freq, ny * freq)
//                float nx = (float) x/width, ny = (float) y/height;
//            for(int y = 0; y < height; y++) {
//        for(int x = 0; x < width; x++) {
//        float e10 = (float) (weight * Math.pow(0.5, 10));
//        float e9 = (float) (weight * Math.pow(0.5, 9));
//        float e8 = (float) (weight * Math.pow(0.5, 8));
//        float e7 = (float) (weight * Math.pow(0.5, 7));
//        float e6 = (float) (weight * Math.pow(0.5, 6));
//        float e5 = (float) (weight * Math.pow(0.5, 5));
//        float e4 = (float) (weight * Math.pow(0.5, 4));
//        float e3 = (float) (weight * Math.pow(0.5, 3));
//        float e2 = (float) (weight * Math.pow(0.5, 2));
//        float e1 = 0.5f;
//        float e0 = 1f;
//        float weight = 1f;
//        float freq = 5f;
//        /*float freq = 1.5f;*/
//        float[][] tempMap = new float[width][height];
//        System.out.println(seed);
//        new SimplexNoise(seed);


    //    public float[][] buildNoise(int width, int height) {
//        Random rand = new Random();
//        float[][] islandShape = generateElevation(rand.nextInt(), width, height);
//        //builds mountains
//        for (int i = 0; i < 300; i++) {
//            int x = rand.nextInt(islandShape.length);
//            int y = rand.nextInt(islandShape.length);
//            if (islandShape[x][y] > 0.01f) {
//                int range = rand.nextInt(100) + 10;
//                int size = range * 2;
//                boolean direction = rand.nextBoolean();
//                int plusOrMinus = rand.nextInt(2) * 2 - 1;
//                float a = (float) (Math.random() - 0.5f) / 7;
//                float b = (float) (Math.random() - 0.5f) / 7;
//                float c = (float) (Math.random() - 0.5f) / 7;
//                float d = (float) (Math.random() - 0.5f) / 7;
//                for (int j = 0; j < size; j++) {
//                    int randomizer = rand.nextInt(range) - range / 2; //Where along length of mountain range. 0 if no shift in x axis
//                    int x2 = x + randomizer;
//                    int y2 = rand.nextInt(10) - 5 + y + (int) (Math.pow(d * randomizer, 4) + Math.pow(a * randomizer, 3)
//                            + Math.pow(b * randomizer, 2) + c * randomizer * Math.sin(Math.random()*100)) * plusOrMinus;
//                    if (direction) {
//                        y2 = y + randomizer;
//                        x2 = rand.nextInt(10) - 5 + x + (int) (Math.pow(d * randomizer, 4) + Math.pow(a * randomizer, 3)
//                                + Math.pow(b * randomizer, 2) + c * randomizer * Math.sin(Math.random()*100)) * plusOrMinus;
//                    }
//                    if (x2 > 1 && y2 > 1 && x2 < islandShape.length - 1 && y2 < islandShape.length - 1) {
//                        float increase = 0.01f;
//                        if (islandShape[x2][y2] > 0.01f) {
//                            islandShape[x2][y2] += 2 * increase;
//                            islandShape[x2 + 1][y2 + 1] += increase;
//                            islandShape[x2 - 1][y2 + 1] += increase;
//                            islandShape[x2 + 1][y2 - 1] += increase;
//                            islandShape[x2 - 1][y2 - 1] += increase;
//                            islandShape[x2][y2 + 1] += increase;
//                            islandShape[x2][y2 - 1] += increase;
//                            islandShape[x2 - 1][y2] += increase;
//                            islandShape[x2 + 1][y2] += increase;
//                        }
//                    }
//                }
//            }
//        }
//        islandShape = DiverseUtilities.clamp(islandShape, min, max);
//        return islandShape;
//    }
    public float[][] buildNoise(int width, int height) {
        float[][] islandShape = generateElevation(rand.nextInt(), width, height);
        return islandShape;
    }
}
