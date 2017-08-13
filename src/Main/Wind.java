package Main;

import java.util.Random;

public class Wind {

    public static float[] getRandomWind(int direction) {

        if (direction == 0) return new float[] {
                2, 1, 0,
                1, 0, 0,
                0, 0, 0,
        };
        if (direction == 1) return new float[] {
                1, 2, 1,
                0, 0, 0,
                0, 0, 0,
        };
        if (direction == 2) return new float[] {
                0, 1, 2,
                0, 0, 1,
                0, 0, 0,
        };
        if (direction == 3) return new float[] {
                0, 0, 1,
                0, 0, 2,
                0, 0, 1,
        };
        if (direction == 4) return new float[] {
                0, 0, 0,
                0, 0, 1,
                0, 1, 2,
        };
        if (direction == 5) return new float[] {
                0, 0, 0,
                0, 0, 0,
                1, 2, 1,
        };
        if (direction == 6) return new float[] {
                0, 0, 0,
                1, 0, 0,
                2, 1, 0,
        };
        if (direction == 7) return new float[] {
                1, 0, 0,
                2, 0, 0,
                1, 0, 0,
        };

        return null;
    }
    
    public static float[][] spreadMoisture(float[][] moisture, float[][] ocean, float x, float y) {
        Random rand = new Random();
        int windX = (int) (x*(moisture.length / (rand.nextInt(20)+10)));
        int windY = (int) (y*(moisture.length / (rand.nextInt(20)+10)));
        /*int windX = (int) x* (moisture.length / 17);
        int windY = (int) y* (moisture.length / 23);*/
        float evaporation = 1;
        float[][] result = new float[moisture.length][moisture[0].length];
        for (int i = 0; i < moisture[0].length; i++) {
            for (int j = 0; j < moisture.length; j++) {
                if (ocean[j][i] > 0f) {
                    result[j][i] = 1f; // ocean
                }
                result[j][i] += moisture[j][i] - evaporation/255;
                // Dampen the randomness
//                float wx = (float) ((1000.0 + Math.random() + Math.random()) / 1001.0);
//                float wy = (float) ((1000.0 + Math.random() + Math.random()) / 1001.0);
//                int i2 = i + (int) (windX * wx);
//                int j2 = j + (int) (windY * wy);
                int i2 = i + (int) (windY);
                int j2 = j + (int) (windX);
//                i2 %= moisture.length;
//                j2 %= moisture.length;
                if (j2 < 0 || j2 > moisture.length-1) continue;
                if (i2 < 0 || i2 > moisture.length-1) continue;
                if (i != i2 && j != j2) {
                    float transfer = moisture[j][i] / 9;
                    float speed = (float) ((30.0 + ocean[j][i]) / (30.0 + ocean[j2][i2]));
                    if (speed > 1.0) speed = 1.0f;
                    /* speed is lower if going uphill */
                    transfer = transfer * speed;

                    result[j][i] -= transfer;
                    result[j2][i2] += transfer;
                }
            }
        }
        return result;
    }

//    public static float[][] spreadCold(float[][] temperature, float[][] moisture, float[][] elevation, float x, float y) {
//        Random rand = new Random();
//
//        int windX = (int) x*(temperature.length / (rand.nextInt(15)));
//        int windY = (int) y*(temperature.length / (rand.nextInt(15)));
//        /*int windX = (int) x* (temperature.length / 17);
//        int windY = (int) y* (temperature.length / 23);*/
//        float evaporation = 1;
//
//        float[][] result = new float[temperature.length][temperature[0].length];
//        for (int i = 0; i < temperature.length; i++) {
//            for (int j = 0; j < temperature.length; j++) {
//                if (elevation[i][j] < BiomeGenerator.SNOW_LEVEL && moisture[i][j] > BiomeGenerator.MOISTURE_SNOW_LEVEL && temperature[i][j] < ) {
//                    result[i][j] += j/(temperature.length*2); // snow
//                }
//
//                result[i][j] += temperature[i][j] - evaporation/255;
//
//                // Dampen the randomness
//                float wx = (float) ((20.0 + Math.random() + Math.random()) / 21.0);
//                float wy = (float) ((20.0 + Math.random() + Math.random()) / 21.0);
//                int i2 = i + (int) (windX * wx);
//                int j2 = j + (int) (windY * wy);
////                i2 %= moisture.length;
////                j2 %= moisture.length;
//                if (j2 < 0 || j2 > temperature.length-1) continue;
//                if (i2 < 0 || i2 > temperature.length-1) continue;
//                if (i != i2 && j != j2) {
//                    float transfer = temperature[i][j] / 9;
//                    float speed = (float) ((30.0 + elevation[i][j]) / (30.0 + elevation[i2][j2]));
//                    if (speed > 1.0) speed = 1.0f;
//                    /* speed is lower if going uphill */
//                    transfer = transfer * speed;
//
//                    result[i][j] -= transfer;
//                    result[i2][j2] += transfer;
//                }
//            }
//        }
//        return result;
//    }

    public static float[][] convolutionFilterf(float[][] array, float[] kernel, float[][] elevation) {

        float[][] tempArray = array;

        float x = (float) (Math.random());

        for (int i = 1; i < tempArray.length-1; i++) {
            for (int j = 1; j < tempArray[i].length-1; j++) {
                if (array[i][j] < 0.9f)
                    tempArray[i][j] += i * Math.abs(elevation[i][j]-1f) *
                                   (array[i-1][j-1] *   kernel[0] +
                                    array[i][j-1] *     kernel[1] +
                                    array[i+1][j-1] *   kernel[2] +
                                    array[i-1][j] *     kernel[3] +
                                    array[i][j] *       kernel[4] +
                                    array[i+1][j] *     kernel[5] +
                                    array[i-1][j+1] *   kernel[6] +
                                    array[i][j+1] *     kernel[7] +
                                    array[i+1][j+1] *   kernel[8])
                                    / (kernel[0]+kernel[1]+kernel[2]+kernel[3]+kernel[4]+kernel[5]+kernel[6]+kernel[7]+kernel[8]);
            }
        }
        return tempArray;
    }
}
