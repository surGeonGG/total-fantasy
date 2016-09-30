package Main;


public class SimplexNoiseGenerator {

    private static int XCORNERS, YCORNERS;
    private int min = -1, max = 1;
    private static final float weight = 1.00f;

    public SimplexNoiseGenerator(int XCORNERS, int YCORNERS) {
        this.XCORNERS = XCORNERS;
        this.YCORNERS = YCORNERS;
    }

    public float[][] generateNoise(int seed, boolean adjust) {
        if (adjust) min = 0;

        new SimplexNoise(seed);
        float[][] tempMap = new float[XCORNERS][YCORNERS];
        float freq = 1.5f;
        for(int x = 0; x < XCORNERS; x++) {
            for(int y = 0; y < YCORNERS; y++) {
                float nx = (float) x/XCORNERS, ny = (float) y/YCORNERS;
                tempMap[x][y] = (float)(weight * SimplexNoise.noise(nx * freq, ny * freq)
                        + weight * Math.pow(0.5, 1) * SimplexNoise.noise(nx * 2 * freq, ny * 2 * freq)
                        + weight * Math.pow(0.5, 2) * SimplexNoise.noise(nx * 4 * freq, ny * 4 * freq)
                        + weight * Math.pow(0.5, 3) * SimplexNoise.noise(nx * 8 * freq, ny * 8 * freq)
                        + weight * Math.pow(0.5, 4) * SimplexNoise.noise(nx * 16 * freq, ny * 16 * freq)
                        + weight * Math.pow(0.5, 5) * SimplexNoise.noise(nx * 32 * freq, ny * 32 * freq)
                        + weight * Math.pow(0.5, 6) * SimplexNoise.noise(nx * 64 * freq, ny * 64 * freq)
                        + weight * Math.pow(0.5, 7) * SimplexNoise.noise(nx * 128 * freq, ny * 128 * freq)
                        + weight * Math.pow(0.5, 8) * SimplexNoise.noise(nx * 256 * freq, ny * 256 * freq)
                        + weight * Math.pow(0.5, 9) * SimplexNoise.noise(nx * 512 * freq, ny * 512 * freq)
                        + weight * Math.pow(0.5, 10) * SimplexNoise.noise(nx * 1024 * freq, ny * 1024 * freq));

                
                if (adjust) tempMap[x][y] = adjust(tempMap[x][y]);
                tempMap[x][y] = clamp(tempMap[x][y]);

                tempMap[x][y] = (float) Math.pow(tempMap[x][y], 3);


            }
        }
        return tempMap;
    }

    public float[][] generateNoiseM(int seed, boolean adjust) {
        if (adjust) min = 0;

        new SimplexNoise(seed);
        float[][] tempMap = new float[XCORNERS][YCORNERS];
        int freq = 4;
        for(int x = 0; x < XCORNERS; x++) {
            for(int y = 0; y < YCORNERS; y++) {
                float nx = (float) x/XCORNERS, ny = (float) y/YCORNERS;
                tempMap[x][y] = (float)(1.00f * SimplexNoise.noise(nx * freq, ny * freq)
                        + weight * Math.pow(0.5, 1) * SimplexNoise.noise(nx * 2 * freq, ny * 2 * freq)
                        + weight * Math.pow(0.5, 2) * SimplexNoise.noise(nx * 4 * freq, ny * 4 * freq)
                        + weight * Math.pow(0.5, 3) * SimplexNoise.noise(nx * 8 * freq, ny * 8 * freq)
                        + weight * Math.pow(0.5, 4) * SimplexNoise.noise(nx * 16 * freq, ny * 16 * freq)
                        + weight * Math.pow(0.5, 5) * SimplexNoise.noise(nx * 32 * freq, ny * 32 * freq)
                        + weight * Math.pow(0.5, 6) * SimplexNoise.noise(nx * 64 * freq, ny * 64 * freq)
                        + weight * Math.pow(0.5, 7) * SimplexNoise.noise(nx * 128 * freq, ny * 128 * freq)
                        + weight * Math.pow(0.5, 8) * SimplexNoise.noise(nx * 256 * freq, ny * 256 * freq)
                        + weight * Math.pow(0.5, 9) * SimplexNoise.noise(nx * 512 * freq, ny * 512 * freq)
                        + weight * Math.pow(0.5, 10) * SimplexNoise.noise(nx * 1024 * freq, ny * 1024 * freq));

                if (adjust) tempMap[x][y] = adjust(tempMap[x][y]);
                tempMap[x][y] = clamp(tempMap[x][y]);
            }
        }
        return tempMap;
    }

    public float[][] generateNoiseH(int seed, boolean adjust) {
        if (adjust) min = 0;

        new SimplexNoise(seed);
        float[][] tempMap = new float[XCORNERS][YCORNERS];
        float freq = 0.5f;
        float latitudeMultiplier;

        for(int x = 0; x < tempMap.length; x++) {
            for(int y = 0; y < tempMap[x].length; y++) {
                float nx = (float) x/XCORNERS, ny = (float) y/YCORNERS;
                latitudeMultiplier = (float)y/tempMap[x].length;
                tempMap[x][y] = (float)(1.00f * SimplexNoise.noise(nx * freq, ny * freq)
                        + weight * Math.pow(0.5, 1) * SimplexNoise.noise(nx * 2 * freq, ny * 2 * freq)
                        + weight * Math.pow(0.5, 2) * SimplexNoise.noise(nx * 4 * freq, ny * 4 * freq)
                        + weight * Math.pow(0.5, 3) * SimplexNoise.noise(nx * 8 * freq, ny * 8 * freq)
                        + weight * Math.pow(0.5, 4) * SimplexNoise.noise(nx * 16 * freq, ny * 16 * freq)
                        + weight * Math.pow(0.5, 5) * SimplexNoise.noise(nx * 32 * freq, ny * 32 * freq)
                        + weight * Math.pow(0.5, 6) * SimplexNoise.noise(nx * 64 * freq, ny * 64 * freq)
                        + weight * Math.pow(0.5, 7) * SimplexNoise.noise(nx * 128 * freq, ny * 128 * freq)
                        + weight * Math.pow(0.5, 8) * SimplexNoise.noise(nx * 256 * freq, ny * 256 * freq));

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
