package Main;


import java.util.Random;

public class SimplexNoiseGenerator {

    private static int XCORNERS, YCORNERS;
    private int min = -1, max = 1;


    public SimplexNoiseGenerator(int XCORNERS, int YCORNERS) {
        this.XCORNERS = XCORNERS;
        this.YCORNERS = YCORNERS;
    }



    public float[][] generateNoise(int seed, boolean adjust) {
        new SimplexNoise(seed);

        float[][] tempMap = new float[XCORNERS][YCORNERS];

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

        if (adjust) min = 0;



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

                if (adjust) tempMap[x][y] = adjust(tempMap[x][y]);
                tempMap[x][y] = clamp(tempMap[x][y]);

                tempMap[x][y] = (float) Math.pow(tempMap[x][y], 5);

            }
        }
        return tempMap;
    }




    public float[][] generateNoiseM(int seed, boolean adjust) {

        Random rand = new Random();

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

        if (adjust) min = 0;


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

                tempMap[x][y] = tempMap[x][y] + (rand.nextFloat() - 0.5f) / 2;

                //tempMap[x][y] = tempMap[x][y] / (e0+e1+e2+e3+e4+e5+e6+e7+e8+e9+e10);

                if (adjust) tempMap[x][y] = adjust(tempMap[x][y]);
                tempMap[x][y] = clamp(tempMap[x][y]);
            }
        }
        return tempMap;
    }




    public float[][] generateNoiseH(int seed, boolean adjust) {
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


        if (adjust)
            min = 0;

        new SimplexNoise(seed);
        float[][] tempMap = new float[XCORNERS][YCORNERS];
        float freq = 0.5f;
        float latitudeMultiplier;

        for(int x = 0; x < tempMap.length; x++) {
            for(int y = 0; y < tempMap[x].length; y++) {
                float nx = (float) x/XCORNERS, ny = (float) y/YCORNERS;
                latitudeMultiplier = (float)y/tempMap[x].length;
                tempMap[x][y] = (float)(e0 * SimplexNoise.noise(nx * freq, ny * freq)
                        + e1 * SimplexNoise.noise(nx * 2 * freq, ny * 2 * freq)
                        + e2 * SimplexNoise.noise(nx * 4 * freq, ny * 4 * freq)
                        + e3 * SimplexNoise.noise(nx * 8 * freq, ny * 8 * freq)
                        + e4 * SimplexNoise.noise(nx * 16 * freq, ny * 16 * freq)
                        + e5 * SimplexNoise.noise(nx * 32 * freq, ny * 32 * freq)
                        + e6 * SimplexNoise.noise(nx * 64 * freq, ny * 64 * freq)
                        + e7 * SimplexNoise.noise(nx * 128 * freq, ny * 128 * freq));

                if (adjust) tempMap[x][y] = adjust(tempMap[x][y]);
                tempMap[x][y] = tempMap[x][y] * latitudeMultiplier;
                tempMap[x][y] = tempMap[x][y] + latitudeMultiplier/5;
                tempMap[x][y] = tempMap[x][y] * latitudeMultiplier;
                tempMap[x][y] = tempMap[x][y] + latitudeMultiplier/5;
                tempMap[x][y] = tempMap[x][y] * latitudeMultiplier;
                tempMap[x][y] = tempMap[x][y] + latitudeMultiplier/5;
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
