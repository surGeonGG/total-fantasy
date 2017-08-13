package Main;

public class BiomeGenerator {

    public static final float OCEAN_LEVEL = 0.4f, SNOW_LEVEL = 0.95f, MOISTURE_SNOW_LEVEL = 0.2f;
    public static final float OCEAN = 0.1f;
    public static final float LAND = 0.2f;
    public static final float DESERT = 0.1f;
    public static final float MOUNTAIN = 0.3f;
    public static final float SNOW = 0.5f;

    public static String generateBiome(float height, float moisture) {

        if (height < SimplexNoiseGenerator.OCEAN_LIMIT) return "OCEAN";
        else if (height < SimplexNoiseGenerator.FLATLAND_LIMIT) {
            if (moisture < 0.4f) {
                return "DESERT";
            } else return "GRASSLAND";
        } else {
            if (moisture < 0.4f) {
                return "DESERT_MOUNTAIN";
            } else return "MOUNTAIN";
        }
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
