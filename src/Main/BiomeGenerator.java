package Main;

public class BiomeGenerator {

    public static final float OCEAN_LEVEL = 0.4f, SNOW_LEVEL = 0.95f, MOISTURE_SNOW_LEVEL = 0.2f;
    public static final float OCEAN = 0f;
    public static final float DESERT = 0.1f;
    public static final float GRASSLAND = 0.2f;
    public static final float MOUNTAIN = 0.3f;
    public static final float SNOW = 0.5f;

    public static float[][] generateBiome(float[][] textureMap, float[][] heightMap, float[][] moistureMap ) {

//        float mountainLevel = 0.3f;
//        latitude = ((latitude-0.5f)*3)+0.5f;
////        float latitude2 = -((latitude-0.5f))+0.5f;
//        float snowLine = -(latitude-1)+0.2f;
//        float desertLine = -(latitude-1)-0.5f;
////        float southernBiome = -(latitude2-1)-0.1f;
//        float southernBiome = 0.1f;
//        float northernBiome = -(latitude-1)-0.1f;

        for (int i = 0; i < heightMap.length; i++) {
            for (int j = 0; j < heightMap[i].length; j++) {
                float latitude = (float) i/ (float) heightMap.length;
                textureMap[j][i] = evaluateTerrain(heightMap[j][i], moistureMap[j][i], latitude);
            }
        }
        return textureMap;
    }

    private static float evaluateTerrain(float height, float moisture, float latitude) {

        latitude = ((latitude-0.5f)*3)+0.5f;
        float snowLine = -(latitude-1)+0.2f;
//        System.out.printf("ltd %.3f snw %.3f \n", latitude, snowLine);
        if (height == 0f)
            return OCEAN;
        else if (height >= snowLine) {
            if (moisture > 0.5f)
                return SNOW;
        }
        else if (height >= 0.25f) {
            return MOUNTAIN;
        }
        else if (moisture > 0.5f) {
            return GRASSLAND;
        }
        return DESERT;
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
