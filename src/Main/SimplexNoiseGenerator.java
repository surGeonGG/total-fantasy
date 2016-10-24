package Main;


import java.util.Random;

public class SimplexNoiseGenerator {

    private static int XCORNERS, YCORNERS;
    private static int min = 0, max = 1;
    private static Random rand = new Random();
    private static float MOISTURE_SCATTERING = 0.02f, ELEVATION_SCATTERING = 0.00f;


    public SimplexNoiseGenerator(int XCORNERS, int YCORNERS) {
        this.XCORNERS = XCORNERS;
        this.YCORNERS = YCORNERS;
    }



    public float[][] generateElevation(int seed) {
        new SimplexNoise(seed);
        System.out.println(seed);
        float[][] tempMap = new float[XCORNERS][YCORNERS];


        /*float freq = 1.5f;*/
        float freq = 1.5f;

        float weight = 1f;

        float e0 = 1f;
        float e1 = 0.5f;
        float e2 = (float) (weight * Math.pow(0.5, 2));
        float e3 = (float) (weight * Math.pow(0.5, 3));
        float e4 = (float) (weight * Math.pow(0.5, 4));
        float e5 = (float) (weight * Math.pow(0.5, 5));
        float e6 = (float) (weight * Math.pow(0.5, 6));
        float e7 = (float) (weight * Math.pow(0.5, 7));
        float e8 = (float) (weight * Math.pow(0.5, 8));
        float e9 = (float) (weight * Math.pow(0.5, 9));
        float e10 = (float) (weight * Math.pow(0.5, 10));


        for(int x = 0; x < XCORNERS; x++) {
            for(int y = 0; y < YCORNERS; y++) {
                float nx = (float) x/XCORNERS, ny = (float) y/YCORNERS;
                tempMap[x][y] = (float) (e0 * SimplexNoise.noise(nx * freq, ny * freq)
                        + e1 * SimplexNoise.noise(nx * 2 * freq, ny * 2 * freq)
                        + e2 * SimplexNoise.noise(nx * 4 * freq, ny * 4 * freq)
                        + e3 * SimplexNoise.noise(nx * 8 * freq, ny * 8 * freq)
                        + e4 * SimplexNoise.noise(nx * 16 * freq, ny * 16 * freq)
                        + e5 * SimplexNoise.noise(nx * 32 * freq, ny * 32 * freq)
                        + e6 * SimplexNoise.noise(nx * 64 * freq, ny * 64 * freq)
                        + e7 * SimplexNoise.noise(nx * 128 * freq, ny * 128 * freq)
                        + e8 * SimplexNoise.noise(nx * 256 * freq, ny * 256 * freq)
                        + e9 * SimplexNoise.noise(nx * 512 * freq, ny * 512 * freq)
                        + e10 * SimplexNoise.noise(nx * 1024 * freq, ny * 1024 * freq));

                tempMap[x][y] = tempMap[x][y] / (e0+e1+e2+e3+e4+e5+e6+e7+e8+e9+e10);
                tempMap[x][y] = tempMap[x][y] + 0.4f;

                tempMap[x][y] = adjust(tempMap[x][y]);


                tempMap[x][y] = (float) Math.pow(tempMap[x][y], 3);

                if (tempMap[x][y] > BiomeGenerator.OCEAN_LEVEL+0.05f)
                    tempMap[x][y] = tempMap[x][y] + (rand.nextFloat() - 0.5f) * ELEVATION_SCATTERING;

                tempMap[x][y] = clamp(tempMap[x][y]);

            }
        }
        return tempMap;
    }




    public float[][] generateMoisture(int seed) {

        new SimplexNoise(seed);

        float[][] tempMap = new float[XCORNERS][YCORNERS];

        int freq = 4;

        float weight = 1f;

        float e0 = 1f;
        float e1 = 0.5f;
        float e2 = (float) (weight * Math.pow(0.5, 2));
        float e3 = (float) (weight * Math.pow(0.5, 3));
        float e4 = (float) (weight * Math.pow(0.5, 4));
        float e5 = (float) (weight * Math.pow(0.5, 5));
        float e6 = (float) (weight * Math.pow(0.5, 6));
        float e7 = (float) (weight * Math.pow(0.5, 7));
        float e8 = (float) (weight * Math.pow(0.5, 8));
        float e9 = (float) (weight * Math.pow(0.5, 9));
        float e10 = (float) (weight * Math.pow(0.5, 10));

        for(int x = 0; x < XCORNERS; x++) {
            for(int y = 0; y < YCORNERS; y++) {
                float nx = (float) x/XCORNERS, ny = (float) y/YCORNERS;
                tempMap[x][y] = (float) (e0 * SimplexNoise.noise(nx * freq, ny * freq)
                        + e1 * SimplexNoise.noise(nx * 2 * freq, ny * 2 * freq)
                        + e2 * SimplexNoise.noise(nx * 4 * freq, ny * 4 * freq)
                        + e3 * SimplexNoise.noise(nx * 8 * freq, ny * 8 * freq)
                        + e4 * SimplexNoise.noise(nx * 16 * freq, ny * 16 * freq)
                        + e5 * SimplexNoise.noise(nx * 32 * freq, ny * 32 * freq)
                        + e6 * SimplexNoise.noise(nx * 64 * freq, ny * 64 * freq)
                        + e7 * SimplexNoise.noise(nx * 128 * freq, ny * 128 * freq)
                        + e8 * SimplexNoise.noise(nx * 256 * freq, ny * 256 * freq)
                        + e9 * SimplexNoise.noise(nx * 512 * freq, ny * 512 * freq)
                        + e10 * SimplexNoise.noise(nx * 1024 * freq, ny * 1024 * freq));


                tempMap[x][y] = tempMap[x][y] / 1.2f;

                tempMap[x][y] = tempMap[x][y] + (rand.nextFloat() - 0.5f) * MOISTURE_SCATTERING;

                tempMap[x][y] = adjust(tempMap[x][y]);
                tempMap[x][y] = clamp(tempMap[x][y]);
            }
        }
        return tempMap;
    }



    private static float adjust(float val) {
        return (val + 1) / 2;
    }

    private float clamp(float val) {
        if (val > max) val = max;
        if (val < min) val = min;

        return val;

    }
}
