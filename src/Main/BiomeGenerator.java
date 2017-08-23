package Main;

public class BiomeGenerator {
    public static final int GRASSLAND = 0;
    public static final int DESERT = 1;
    public static final int FOREST = 2;
    public static final int JUNGLE = 3;
    public static final int SWAMPLAND = 4;
    public static final int BADLANDS = 5;
    public static final int STONY = 6;
    public static final int OCEAN = 7;
    public static final int DEEP_FOREST = 8;


    public static int generateBiome(float height, float moisture, float special) {

        if (height < SimplexNoiseGenerator.OCEAN_LIMIT) return OCEAN;
        else if (height < SimplexNoiseGenerator.FLATLAND_LIMIT) {
            if (special < 0.5f) {
                if (moisture < 0.4f) {
                    return DESERT;
                }
                if (moisture < 0.8f) {
                    return GRASSLAND;
                } else {
                    return FOREST;
                }
            } else {
                if (moisture < 0.4f) {
                    return BADLANDS;
                } else {
                    return GRASSLAND;
                }
            }
        } else if (height < SimplexNoiseGenerator.MOUNTAIN_LIMIT) {
            if (special < 0.5f) {
                if (moisture < 0.4f) {
                    return STONY;
                }
                if (moisture < 0.6f) {
                    return FOREST;
                } else {
                    return DEEP_FOREST;
                }
            } else {
                if (moisture < 0.4f) {
                    return STONY;
                } else {
                    return BADLANDS;
                }
            }
        } else {
            if (special < 0.5f) {
                if (moisture < 0.4f) {
                    return STONY;
                }
                if (moisture < 0.5f) {
                    return FOREST;
                } else {
                    return DEEP_FOREST;
                }
            } else {
                if (moisture < 0.4f) {
                    return DESERT;
                } else {
                    return BADLANDS;
                }
            }
        }





    }


}
