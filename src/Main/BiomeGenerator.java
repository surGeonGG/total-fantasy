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

            if (moisture > 0.6f)
                return "SNOW";
            if (moisture > 0.3f)
                return "BARE";
            if (moisture > 0.01f)
                return "SCORCHED";
            return "LAVA";

        } else if (elevation == 0.8f) {
            if (moisture > 0.6f)
                return "TUNDRA";
            if (moisture > 0.3f)
                return "BARE";
            return "SCORCHED";


        } else if (elevation == 0.7f) {
            if (moisture > 0.6f)
                return "TAIGA";
            if (moisture > 0.3f)
                return "TUNDRA";
            return "BARE";

        } else if (elevation == 0.6f) {
            if (moisture > 0.6f)
                return "TAIGA";
            if (moisture > 0.3f)
                return "TUNDRA";
            return "BARE";

        } else if (elevation == 0.5f) {

            if (moisture > 0.8f)
                return "TEMPERATE_DECIDUOUS_FOREST";
            if (moisture > 0.4f)
                return "SHRUBLAND";
            return "TAIGA";

        } else if (elevation == 0.4f) {

            if (moisture > 0.8f)
                return "TEMPERATE_RAIN_FOREST";
            if (moisture > 0.5f)
                return "TEMPERATE_DECIDUOUS_FOREST";
            return "SHRUBLAND";

        } else if (elevation == 0.3f) {

            if (moisture > 0.7f)
                return "TEMPERATE_RAIN_FOREST";
            if (moisture > 0.4f)
                return "TEMPERATE_DECIDUOUS_FOREST";
            return "GRASSLAND";

        } else if (elevation == 0.2f) {
            if (moisture > 0.95f)
                return "MARSH";
            if (moisture > 0.7f)
                return "TEMPERATE_RAIN_FOREST";
            if (moisture > 0.4f)
                return "TEMPERATE_DECIDUOUS_FOREST";
            if (moisture > 0.1f )
                return "GRASSLAND";
            return "TEMPERATE_DESERT";

        } else if (elevation == 0.15f) {

            if (moisture > 0.95f)
                return "MARSH";
            if (moisture > 0.7f)
                return "TEMPERATE_RAIN_FOREST";
            if (moisture > 0.4f)
                return "TEMPERATE_DECIDUOUS_FOREST";
            if (moisture > 0.1f)
                return "GRASSLAND";
            return "TEMPERATE_DESERT";

        } else if (elevation == 0.1f) {

            return "BEACH";

        }
        return "GRASSLAND";
    }



    public static float[] getBiomeColor(String biome) {
        if (biome == "OCEAN") return new float[] { 0.267f, 0.267f, 0.478f };
        if (biome == "COAST") return new float[] { 0.2f, 0.2f, 0.353f };
        if (biome == "LAKESHORE") return new float[] { 0.133f, 0.333f, 0.533f };
        if (biome == "LAKE") return new float[] { 0.2f, 0.4f, 0.6f };
        if (biome == "RIVER") return new float[] { 0.133f, 0.333f, 0.533f };
        if (biome == "MARSH") return new float[] { 0.184f, 0.4f, 0.4f };
        if (biome == "ICE") return new float[] { 0.6f, 1f, 1f };
        if (biome == "BEACH") return new float[] { 0.627f, 0.565f, 0.467f };
        if (biome == "ROAD1") return new float[] { 0.267f, 0.133f, 0.067f };
        if (biome == "ROAD2") return new float[] { 0.333f, 0.2f, 0.133f };
        if (biome == "ROAD3") return new float[] { 0.4f, 0.267f, 0.2f };
        if (biome == "BRIDGE") return new float[] { 0.408f, 0.408f, 0.376f };
        if (biome == "LAVA") return new float[] { 0.8f, 0.2f, 0.2f };
        if (biome == "SNOW") return new float[] { 1f, 1f, 1f };
        if (biome == "TUNDRA") return new float[] { 0.733f, 0.733f, 0.667f };
        if (biome == "BARE") return new float[] { 0.533f, 0.533f, 0.533f };
        if (biome == "SCORCHED") return new float[] { 0.333f, 0.333f, 0.333f };
        if (biome == "TAIGA") return new float[] { 0.6f, 0.667f, 0.467f };
        if (biome == "SHRUBLAND") return new float[] { 0.533f, 0.6f, 0.467f };
        if (biome == "TEMPERATE_DESERT") return new float[] { 0.788f, 0.824f, 0.608f };
        if (biome == "TEMPERATE_RAIN_FOREST") return new float[] { 0.267f, 0.533f, 0.333f };
        if (biome == "TEMPERATE_DECIDUOUS_FOREST") return new float[] { 0.404f, 0.58f, 0.349f };
        if (biome == "GRASSLAND") return new float[] { 0.533f, 0.667f, 0.333f };
        if (biome == "SUBTROPICAL_DESERT") return new float[] { 0.824f, 0.725f, 0.545f };
        if (biome == "TROPICAL_RAIN_FOREST") return new float[] { 0.2f, 0.467f, 0.333f };
        if (biome == "TROPICAL_SEASONAL_FOREST") return new float[] { 0.333f, 0.6f, 0.267f };

        return null;
    }



    public static float smoothElevation(float elevation) {
        if (elevation < 0.1) return 0;
        else if (elevation < 0.103f) return 0.1f;
        else if (elevation < 0.2f) return 0.15f;
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
