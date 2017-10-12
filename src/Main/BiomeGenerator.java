package Main;

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

    public static final float BEACH_LINE = 0.0f;
    public static final float LAND_LINE = 0.12f;
    public static final float TREE_LINE = 0.30f;
    public static final float STONY_LINE = 0.8f;
    public static final float SNOW_LINE = 0.97f;


    public static int generateBiome(float height, float moisture, float special) {
        int biome = OCEAN;

        if (height > BEACH_LINE) {
            biome = OCEAN;
        }
        if (height > LAND_LINE) {
            biome = GRASSLAND;
        }
        if (height > TREE_LINE) {
            biome = FOREST;
        }
        if (height > STONY_LINE) {
            biome = STONY;
        }
        if (height > SNOW_LINE) {
            biome = SNOW;
        }
        return biome;
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
