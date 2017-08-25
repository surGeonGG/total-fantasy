package Main;

import java.util.Random;

public class BiomeGenerator {
    public static final int GRASSLAND = 0;
    public static final int DESERT = 1;
    public static final int FOREST = 2;
    public static final int TROPICAL_FOREST = 3;
    public static final int SWAMPLAND = 4;
    public static final int BADLANDS = 5;
    public static final int STONY = 6;
    public static final int OCEAN = 7;
    public static final int DEEP_FOREST = 8;
    public static final int SNOW = 9;

    public static final float TREE_LINE = 0.30f;
    public static final float OCEAN_LINE = 0.1f;


    public static int generateBiome(float height, float moisture, float special) {
        Random random = new Random();

        if (random.nextInt(100) == 0) {
            return 2;
        }
        if (height < OCEAN_LINE) return OCEAN;
        else if (height < TREE_LINE) {
            if (moisture < 0.3f) {
                return DESERT;
            } else if (moisture < 0.9f) {
                return GRASSLAND;
            } else {
                return SWAMPLAND;
            }
        } else if (height < SimplexNoiseGenerator.FLATLAND_LIMIT) {
            if (moisture < 0.15f) {
                return DESERT;
            } else if (moisture < 0.4f) {
                return GRASSLAND;
            } else if (moisture < 0.6f) {
                return FOREST;
            } else {
                return DEEP_FOREST;
            }
        } else if (height < SimplexNoiseGenerator.MOUNTAIN_LIMIT) {
            if (moisture < 0.2f) {
                return STONY;
            }
            if (moisture < 0.4f) {
                return FOREST;
            } else {
                return DEEP_FOREST;
            }
        } else if (height < SimplexNoiseGenerator.TALL_MOUNTAIN_LIMIT) {
            if (moisture < 0.4f) {
                return STONY;
            }
            if (moisture < 0.7f) {
                return FOREST;
            } else {
                return DEEP_FOREST;
            }
        } else {
            return SNOW;
        }
    }
}
