package Main;



public class BiomeGenerator {

    public static final float OCEAN_LEVEL = 0.4f;

    public static String generateBiome(float elevation, float moisture, boolean isRiver, boolean isMountainTop, boolean isMountainSide) {

        float altitudeLevel1 = OCEAN_LEVEL + ((1f - OCEAN_LEVEL)/5);
        float altitudeLevel2 = OCEAN_LEVEL + 2 * ((1f - OCEAN_LEVEL)/5);
        float altitudeLevel3 = OCEAN_LEVEL + 3 * ((1f - OCEAN_LEVEL)/5);
        float altitudeLevel4 = OCEAN_LEVEL + 4 * ((1f - OCEAN_LEVEL)/5);


        if (elevation < OCEAN_LEVEL) {

            return "OCEAN";

        } else if (isRiver) {

            return "RIVER";

        } else if (isMountainTop) {

            return "SNOW";

        } else if (isMountainSide) {

            return "MOUNTAIN_SIDE";

        } else if (elevation > altitudeLevel4) {

            if (moisture > 0.8f)
                return "DEEP_FOREST_0";
            if (moisture > 0.5f)
                return "FOREST_0";
            if (moisture > 0.2f)
                return "GRASSLAND_0";
            return "DESERT_0";

        } else if (elevation > altitudeLevel3) {

            if (moisture > 0.8f)
                return "DEEP_FOREST_0";
            if (moisture > 0.5f)
                return "FOREST_0";
            if (moisture > 0.2f)
                return "GRASSLAND_0";
            return "DESERT_0";

        } else if (elevation > altitudeLevel2) {

            if (moisture > 0.8f)
                return "DEEP_FOREST_0";
            if (moisture > 0.5f)
                return "FOREST_0";
            if (moisture > 0.2f)
                return "GRASSLAND_0";
            return "DESERT_0";

        } else if (elevation > altitudeLevel1) {
            if (moisture > 0.7f)
                return "TROPICAL_DEEP_FOREST_0";
            if (moisture > 0.4f)
                return "FOREST_0";
            if (moisture > 0.2f)
                return "GRASSLAND_0";
            return "DESERT_0";

        } else if (elevation > OCEAN_LEVEL+0.005f) {

            if (moisture > 0.7f)
                return "TROPICAL_DEEP_FOREST_0";
            if (moisture > 0.4f)
                return "FOREST_0";
            if (moisture > 0.2f)
                return "GRASSLAND_0";
            return "DESERT_0";

        } else if (elevation > OCEAN_LEVEL) {

            return "BEACH";

        }
        return "GRASSLAND";
    }



    public static int[] getBiomeColor(String biome) {

        if (biome == "OCEAN") return new int[] { 68, 68, 122 };
        if (biome == "RIVER") return new int[] { 34, 85, 136 };

        if (biome == "DEEP_FOREST_0") return new int[] { 68, 136, 85 };
        if (biome == "DEEP_FOREST_1") return new int[] { 80,136,94 };
        if (biome == "DEEP_FOREST_2") return new int[] { 92,136,103 };
        if (biome == "DEEP_FOREST_3") return new int[] { 105,136,113 };

        if (biome == "FOREST_0") return new int[] { 100, 145, 95 };
        if (biome == "FOREST_1") return new int[] { 110,145,106 };
        if (biome == "FOREST_2") return new int[] { 120,145,117 };
        if (biome == "FOREST_3") return new int[] { 128,145,126 };


        if (biome == "TROPICAL_DEEP_FOREST_0") return new int[] { 51, 120, 85 };
        if (biome == "TROPICAL_DEEP_FOREST_1") return new int[] { 65,120,92 };
        if (biome == "TROPICAL_DEEP_FOREST_2") return new int[] { 76,120,97 };
        if (biome == "TROPICAL_DEEP_FOREST_3") return new int[] { 89,120,104 };

        if (biome == "TROPICAL_FOREST_0") return new int[] { 85, 153, 68 };
        if (biome == "TROPICAL_FOREST_1") return new int[] { 98,153,84 };
        if (biome == "TROPICAL_FOREST_2") return new int[] { 109,153,98 };
        if (biome == "TROPICAL_FOREST_3") return new int[] { 120,153,112 };

        if (biome == "GRASSLAND_0") return new int[] { 136, 170, 85 };
        if (biome == "GRASSLAND_1") return new int[] { 142,170,100 };
        if (biome == "GRASSLAND_2") return new int[] { 148,170,116 };
        if (biome == "GRASSLAND_3") return new int[] { 154,170,131 };

        if (biome == "DESERT_0") return new int[] { 214, 187, 157 };
        if (biome == "DESERT_1") return new int[] { 214,192,167 };
        if (biome == "DESERT_2") return new int[] { 214,197,178 };
        if (biome == "DESERT_3") return new int[] { 214,202,188 };

        if (biome == "MOUNTAIN_SIDE") return new int[] { 130,126,90 };


        if (biome == "OCEAN") return new int[] { 68, 68, 122 };
        if (biome == "COAST") return new int[] { 51, 51, 90 };
        if (biome == "LAKESHORE") return new int[] { 34, 85, 136 };
        if (biome == "LAKE") return new int[] { 51, 102, 153 };
        if (biome == "RIVER") return new int[] { 34, 85, 136 };
        if (biome == "MARSH") return new int[] { 47, 102, 102 };
        if (biome == "ICE") return new int[] { 153, 255, 255 };
        if (biome == "BEACH") return new int[] { 160, 144, 119 };
        if (biome == "ROAD1") return new int[] { 85, 34, 9 };
        if (biome == "ROAD2") return new int[] { 85, 51, 34 };
        if (biome == "ROAD3") return new int[] { 102, 68, 51 };
        if (biome == "BRIDGE") return new int[] { 102, 102, 92 };
        if (biome == "LAVA") return new int[] { 204, 51, 51 };
        if (biome == "SNOW") return new int[] { 255, 255, 255 };
        if (biome == "TUNDRA") return new int[] { 187, 187, 170 };
        if (biome == "BARE") return new int[] { 136, 136, 136 };
        if (biome == "SCORCHED") return new int[] { 85, 85, 85 };
        if (biome == "TAIGA") return new int[] { 153, 170, 120 };
        if (biome == "SHRUBLAND") return new int[] { 136, 153, 120 };
        if (biome == "TEMPERATE_DESERT") return new int[] { 201, 210, 155 };
        if (biome == "TEMPERATE_RAIN_FOREST") return new int[] { 68, 136, 85 };
        if (biome == "TEMPERATE_DECIDUOUS_FOREST") return new int[] { 100, 145, 95 };
        if (biome == "GRASSLAND") return new int[] { 136, 170, 85 };

        if (biome == "TROPICAL_RAIN_FOREST") return new int[] { 51, 120, 85 };
        if (biome == "TROPICAL_SEASONAL_FOREST") return new int[] { 85, 153, 68 };
        if (biome == "RED") return new int[] { 255, 0, 0 };

        return null;
    }

    public static float[][] smoothElevation(float[][] elevation) {
        float[][] elevationCopy = new float[elevation.length][elevation[0].length];
        for (int i = 0; i < elevation.length; i++) {
            for (int j = 0; j < elevation[i].length; j++) {
                elevationCopy[i][j] = smoothElevationReturner(elevation[i][j]);
            }
        }
        return elevationCopy;
    }

    private static float smoothElevationReturner(float elevation) {

        if (elevation < OCEAN_LEVEL) return 0f;

        else if (elevation < 0.5f) return 0.4f;
        else if (elevation < 0.6f) return 0.5f;
        else if (elevation < 0.7f) return 0.6f;
        else if (elevation < 0.8f) return 0.7f;
        else if (elevation < 0.9f) return 0.8f;
        else return 0.9f;

    }
}
