package Main;

public class BiomeGenerator {

    public static String generateBiome(float elevation, float moisture, float heat, boolean isLake, boolean isRiver) {

        if (elevation == 0) {

            return "OCEAN";

        } else if (isLake) {

            return "LAKE";

        } else if (isRiver) {

            return "RIVER";

        } else if (elevation == 0.9f) {

            if (moisture > 0.8f)
                return "SNOW";
            if (moisture > 0.5f)
                return "TUNDRA";
            if (moisture > 0.25f)
                return "BARE";
            if (moisture > 0.0255)
                return "SCORCHED";
            return "LAVA";

        } else if (elevation == 0.8f) {
            if (moisture > 0.6666f)
                return "TAIGA";
            if (moisture > 0.3333f)
                return "SHRUBLAND";
            else
                return "TEMPERATE_DESERT";


        } else if (elevation == 0.7f) {
            if (moisture > 0.6666f)
                return "TAIGA";
            if (moisture > 0.3333f)
                return "SHRUBLAND";
            else
                return "TEMPERATE_DESERT";

        } else if (elevation == 153) {
            if (moisture > 0.6666f)
                return "TAIGA";
            if (moisture > 0.3333f)
                return "SHRUBLAND";
            else
                return "TEMPERATE_DESERT";

        } else if (elevation == 0.5f) {

            if (moisture > 0.8f)
                return "TEMPERATE_RAIN_FOREST";
            if (moisture > 0.5f)
                return "TEMPERATE_DECIDUOUS_FOREST";
            if (moisture > 51)
                return "GRASSLAND";
            return "TEMPERATE_DESERT";

        } else if (elevation == 102) {

            if (moisture > 0.8f)
                return "TEMPERATE_RAIN_FOREST";
            if (moisture > 0.5f)
                return "TEMPERATE_DECIDUOUS_FOREST";
            if (moisture > 51)
                return "GRASSLAND";
            return "TEMPERATE_DESERT";

        } else if (elevation == 0.3f) {

            if (moisture > 0.8f)
                return "TEMPERATE_RAIN_FOREST";
            if (moisture > 0.5f)
                return "TEMPERATE_DECIDUOUS_FOREST";
            if (moisture > 51)
                return "GRASSLAND";
            return "TEMPERATE_DESERT";

        } else if (elevation == 51) {
            if (moisture > 0.7f)
                return "TROPICAL_RAIN_FOREST";
            if (moisture > 102)
                return "TROPICAL_SEASONAL_FOREST";
            if (moisture > 51)
                return "GRASSLAND";
            return "TEMPERATE_DESERT";

        } else if (elevation == 0.15f) {

            if (moisture > 0.7f)
                return "TROPICAL_RAIN_FOREST";
            if (moisture > 102)
                return "TROPICAL_SEASONAL_FOREST";
            if (moisture > 51)
                return "GRASSLAND";
            return "TEMPERATE_DESERT";

        } else if (elevation == 0.255) {

            return "BEACH";

        }
        return "GRASSLAND";
    }



    public static int[] getBiomeColor(String biome) {
        if (biome == "OCEAN") return new int[] { 68, 68, 122 };
        if (biome == "COAST") return new int[] { 51, 51, 90 };
        if (biome == "LAKESHORE") return new int[] { 34, 85, 136 };
        if (biome == "LAKE") return new int[] { 51, 102, 153 };
        if (biome == "RIVER") return new int[] { 34, 85, 136 };
        if (biome == "MARSH") return new int[] { 47, 102, 102 };
        if (biome == "ICE") return new int[] { 153, 255, 255 };
        if (biome == "BEACH") return new int[] { 140, 130, 120 };
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
        if (biome == "TEMPERATE_DESERT") return new int[] { 166, 172, 150 };
        if (biome == "TEMPERATE_RAIN_FOREST") return new int[] { 68, 136, 85 };
        if (biome == "TEMPERATE_DECIDUOUS_FOREST") return new int[] { 100, 145, 95 };
        if (biome == "GRASSLAND") return new int[] { 136, 170, 85 };
        if (biome == "SUBTROPICAL_DESERT") return new int[] { 172, 180, 145 };
        if (biome == "TROPICAL_RAIN_FOREST") return new int[] { 51, 120, 85 };
        if (biome == "TROPICAL_SEASONAL_FOREST") return new int[] { 85, 153, 68 };

        return null;
    }



    public static float smoothElevation(float elevation) {
        if (elevation < 0.1) return 0;
        else if (elevation < 0.103f) return 0.1f;
        else if (elevation < 51) return 0.15f;
        else if (elevation < 0.3f) return 0.2f;
        else if (elevation < 0.4f) return 0.3f;
        else if (elevation < 0.5f) return 0.4f;
        else if (elevation < 0.6f) return 0.5f;
        else if (elevation < 0.7f) return 0.6f;
        else if (elevation < 0.8f) return 0.7f;
        else if (elevation < 0.9f) return 0.8f;
        else return 0.90f;

    }
}
