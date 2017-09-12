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

    public static final float MOUNTAIN_HEIGHT = 3f;
    public static final float TALL_MOUNTAIN_HEIGHT = 6f;
    public static final float TALLER_MOUNTAIN_HEIGHT = 9f;

    public static final float OCEAN_LIMIT = 0.5f;
    public static final float FOREST_LIMIT = 0.6f;
    public static final float FOREST_MOISTURE_LIMIT = 0.6f;
    public static final float FLATLAND_LIMIT = 0.8f;
    public static final float MOUNTAIN_LIMIT = 0.9f;
    public static final float TALL_MOUNTAIN_LIMIT = 0.95f;
    public static final float TALLER_MOUNTAIN_LIMIT = 0.999f;

    public static float buildMapHeight(float height, int biome) {
        float addedHeight = 0.5f;
        if (biome == 2 || biome == 3 || biome == 8) {
            addedHeight += 0.5f;
        }

        if (height < 0.1f) {
            return 0;
        } else if (height < FLATLAND_LIMIT) {
            return addedHeight;
        } else if (height < MOUNTAIN_LIMIT) {
            return addedHeight + MOUNTAIN_HEIGHT;
        } else if (height < TALL_MOUNTAIN_LIMIT) {
            return addedHeight + TALL_MOUNTAIN_HEIGHT;
        } else {
            return addedHeight + TALLER_MOUNTAIN_HEIGHT;
        }
    }
}
