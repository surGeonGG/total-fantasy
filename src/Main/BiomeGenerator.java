package Main;

public class BiomeGenerator {

    public static final float OCEAN_LEVEL = 0.4f, SNOW_LEVEL = 0.95f, MOISTURE_SNOW_LEVEL = 0.2f;

    public static String generateBiome(float elevation,
                                       float moisture,
                                       boolean isRiver,
                                       boolean isMountainTop,
                                       boolean isMountainSide,
                                       boolean isOcean,
                                       float latitude) {

        float mountainLevel = 0.3f;
        latitude = ((latitude-0.5f)*3)+0.5f;
        float latitude2 = -((latitude-0.5f))+0.5f;
        float snowLine = -(latitude-1)+0.2f;
        float desertLine = -(latitude-1)-0.5f;
//        float southernBiome = -(latitude2-1)-0.1f;
        float southernBiome = 0.1f;
        float northernBiome = -(latitude-1)-0.1f;

        if (isOcean) {
            return "OCEAN";
        }
        else if (isRiver) {
            return "RIVER";
        }
        else if (isMountainTop) {
            if (moisture > MOISTURE_SNOW_LEVEL && elevation > SNOW_LEVEL)
                return "SNOW";
            else if (moisture > 0.2f)
                return "MOUNTAIN";
            else
                return "MOUNTAIN";

        } else if (isMountainSide) {

            return "MOUNTAIN_SIDE";

//        } else if (elevation > altitudeLevel4) {
//
//            if (moisture > 0.8f)
//                return "DEEP_FOREST_0";
//            if (moisture > 0.5f)
//                return "FOREST_0";
//            if (moisture > 0.2f)
//                return "GRASSLAND_0";
//            return "DESERT_0";
//
//        } else if (elevation > altitudeLevel3) {
//
//            if (moisture > 0.8f)
//                return "DEEP_FOREST_0";
//            if (moisture > 0.5f)
//                return "FOREST_0";
//            if (moisture > 0.2f)
//                return "GRASSLAND_0";
//            return "DESERT_0";
//
//        } else if (elevation > altitudeLevel2) {
//
//            if (moisture > 0.8f)
//                return "DEEP_FOREST_0";
//            if (moisture > 0.5f)
//                return "FOREST_0";
//            if (moisture > 0.2f)
//                return "GRASSLAND_0";
//            return "DESERT_0";
//
        } else if (elevation >= snowLine) {
            return "SNOW";
        } else if (elevation >= mountainLevel) {
            return "MOUNTAIN";
        } else if (elevation >= northernBiome) {
            if (moisture > 0.7f)
                return "DEEP_FOREST_3";
            if (moisture > 0.4f)
                return "FOREST_3";
            if (moisture > 0.2f)
                return "GRASSLAND_3";
            if (moisture <= desertLine)
                return "GRASSLAND_3";
        } else {
            if (moisture > 0.7f)
                return "DEEP_FOREST_0";
            if (moisture > 0.4f)
                return "FOREST_0";
            if (moisture > 0.2f)
                return "GRASSLAND_0";
            if (moisture <= 0.2f)
                return "DESERT_0";
        }
        return "GRASSLAND_0";
    }



    public static int[] getBiomeColor(String biome) {

        if (biome == "OCEAN") return new int[] { 68, 68, 122, 255 };
        if (biome == "RIVER") return new int[] { 34, 85, 136, 255 };

        if (biome == "DEEP_FOREST_0") return new int[] { 68, 136, 85, 255 };
        if (biome == "DEEP_FOREST_1") return new int[] { 80,136,94, 255 };
        if (biome == "DEEP_FOREST_2") return new int[] { 92,136,103, 255 };
        if (biome == "DEEP_FOREST_3") return new int[] { 105,136,113, 255 };

        if (biome == "FOREST_0") return new int[] { 100, 145, 95, 255 };
        if (biome == "FOREST_1") return new int[] { 110,145,106, 255 };
        if (biome == "FOREST_2") return new int[] { 120,145,117, 255 };
        if (biome == "FOREST_3") return new int[] { 128,145,126, 255 };

        if (biome == "TROPICAL_DEEP_FOREST_0") return new int[] { 51, 120, 85, 255 };
        if (biome == "TROPICAL_DEEP_FOREST_1") return new int[] { 65,120,92, 255 };
        if (biome == "TROPICAL_DEEP_FOREST_2") return new int[] { 76,120,97, 255 };
        if (biome == "TROPICAL_DEEP_FOREST_3") return new int[] { 89,120,104, 255 };

        if (biome == "TROPICAL_FOREST_0") return new int[] { 85, 153, 68, 255 };
        if (biome == "TROPICAL_FOREST_1") return new int[] { 98,153,84, 255 };
        if (biome == "TROPICAL_FOREST_2") return new int[] { 109,153,98, 255 };
        if (biome == "TROPICAL_FOREST_3") return new int[] { 120,153,112, 255 };

        if (biome == "GRASSLAND_0") return new int[] { 136, 170, 85, 255 };
        if (biome == "GRASSLAND_1") return new int[] { 142,170,100, 255 };
        if (biome == "GRASSLAND_2") return new int[] { 148,170,116, 255 };
        if (biome == "GRASSLAND_3") return new int[] { 154,170,131, 255 };

        if (biome == "DESERT_0") return new int[] { 214, 187, 157, 255 };
        if (biome == "DESERT_1") return new int[] { 214,192,167, 255 };
        if (biome == "DESERT_2") return new int[] { 214,197,178, 255 };
        if (biome == "DESERT_3") return new int[] { 214,202,188, 255 };

        if (biome == "MOUNTAIN_SIDE") return new int[] { 130,126,90, 255 };
        if (biome == "FORESTED_MOUNTAIN_SIDE") return new int[] { 130,148,90, 255 };
        if (biome == "DESERT_MOUNTAIN_SIDE") return new int[] { 130,126,90, 255 };

        if (biome == "MOUNTAIN") return new int[] { 180,180,180, 255};
        if (biome == "DESERT_MOUNTAIN") return new int[] { 170,147,131, 255};
        if (biome == "SNOW") return new int[] { 220, 220,220, 255 };


        if (biome == "OCEAN") return new int[] { 68, 68, 122, 255 };
        if (biome == "COAST") return new int[] { 51, 51, 90, 255 };
        if (biome == "LAKESHORE") return new int[] { 34, 85, 136, 255 };
        if (biome == "LAKE") return new int[] { 51, 102, 153, 255 };
        if (biome == "RIVER") return new int[] { 34, 85, 136, 255 };
        if (biome == "MARSH") return new int[] { 47, 102, 102, 255 };
        if (biome == "ICE") return new int[] { 153, 255, 255, 255 };
        if (biome == "BEACH") return new int[] { 160, 144, 119, 255 };
        if (biome == "ROAD1") return new int[] { 85, 34, 9, 255 };
        if (biome == "ROAD2") return new int[] { 85, 51, 34, 255 };
        if (biome == "ROAD3") return new int[] { 102, 68, 51, 255 };
        if (biome == "BRIDGE") return new int[] { 102, 102, 92, 255 };
        if (biome == "LAVA") return new int[] { 204, 51, 51, 255 };

        if (biome == "TUNDRA") return new int[] { 187, 187, 170, 255 };

        if (biome == "SCORCHED") return new int[] { 85, 85, 85, 255 };
        if (biome == "TAIGA") return new int[] { 153, 170, 120, 255 };
        if (biome == "SHRUBLAND") return new int[] { 136, 153, 120, 255 };
        if (biome == "TEMPERATE_DESERT") return new int[] { 201, 210, 155, 255 };
        if (biome == "TEMPERATE_RAIN_FOREST") return new int[] { 68, 136, 85, 255 };
        if (biome == "TEMPERATE_DECIDUOUS_FOREST") return new int[] { 100, 145, 95, 255 };
        if (biome == "GRASSLAND") return new int[] { 136, 170, 85, 255 };

        if (biome == "TROPICAL_RAIN_FOREST") return new int[] { 51, 120, 85, 255 };
        if (biome == "TROPICAL_SEASONAL_FOREST") return new int[] { 85, 153, 68, 255 };
        if (biome == "RED") return new int[] { 255, 0, 0, 255 };

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
