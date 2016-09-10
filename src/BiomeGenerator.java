public class BiomeGenerator {

    public static String generateBiome(float elevation, float moisture, float heat, boolean isWater, boolean isCoast) {

        if (elevation < 0) {
            /*if (heat > 0.99 && elevation > -0.4) return "ICE";*/
            return "OCEAN";
        } else if (isWater) {
            if (elevation < 0.1) return "MARSH";
            if (elevation > 0.8) return "ICE";
            return "LAKE";
        } else if (isCoast) {
            return "BEACH";
        /*} else if (heat > 0.90) {
            return "SNOW";*/
        } else if (heat < 0.3 && moisture < 0.2) {
            return "SUBTROPICAL_DESERT";
        } else if (elevation > 0.95 && moisture > 0.50 && heat > 0.2) {
            return "SNOW";
        } else if (elevation > 0.8) {
            if (moisture > 0.33) return "TUNDRA";
            else if (moisture > 0.16) return "BARE";
            else return "SCORCHED";
        } else if (elevation > 0.6) {
            if (moisture > 0.66) return "TAIGA";
            else if (moisture > 0.33) return "SHRUBLAND";
            else return "TEMPERATE_DESERT";
        } else if (elevation > 0.3) {
            if (moisture > 0.83) return "TEMPERATE_RAIN_FOREST";
            else if (moisture > 0.50) return "TEMPERATE_DECIDUOUS_FOREST";
            else if (moisture > 0.16) return "GRASSLAND";
            else return "TEMPERATE_DESERT";
        } else {
            if (moisture > 0.66) return "TROPICAL_RAIN_FOREST";
            else if (moisture > 0.33) return "TROPICAL_SEASONAL_FOREST";
            else if (moisture > 0.16) return "GRASSLAND";
            else return "GRASSLAND";
        }
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
}
