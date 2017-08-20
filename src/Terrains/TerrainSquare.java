package Terrains;

import Main.Game;
import Main.Loader;
import Utils.DiverseUtilities;

public class TerrainSquare {

    private static Loader loader = new Loader();
    private String biome;
    private float[][] heightMap;
    private float heightMultiplier;
    private float x0;
    private float z0;
    private float[] outline;
    private float lw = 0.1f;
    private boolean showLine = false;

    public TerrainSquare(float[][] heightMap, float heightMultiplier, int x, int z, String biome) {
        this.heightMap = heightMap;
        x0 = x * Game.X_VERTICES_PER_SQUARE - x;
        z0 = z * Game.Z_VERTICES_PER_SQUARE - z;
        this.biome = biome;
        this.heightMultiplier = heightMultiplier;
        int vPerSideM1 = Game.X_VERTICES_PER_SQUARE - 1;
        int vcount = (vPerSideM1 * 24) + 6;
        outline = new float[vcount];
        for (int i = 0; i < vPerSideM1; i++) {
            outline[i*6  ] = x0 + i;
            outline[i*6+1] = DiverseUtilities.clamp(heightMap[0][i], 0f, 99f) + 0.05f;
            outline[i*6+2] = z0 - lw;
            outline[i*6+3] = x0 + i;
            outline[i*6+4] = DiverseUtilities.clamp(heightMap[0][i], 0f, 99f) + 0.05f;
            outline[i*6+5] = z0 + lw;

            outline[i*6   + vPerSideM1 * 6] = x0 + vPerSideM1 + lw;
            outline[i*6+1 + vPerSideM1 * 6] = DiverseUtilities.clamp(heightMap[i][vPerSideM1], 0f, 99f) + 0.05f;
            outline[i*6+2 + vPerSideM1 * 6] = z0 + i;
            outline[i*6+3 + vPerSideM1 * 6] = x0 + vPerSideM1 - lw;
            outline[i*6+4 + vPerSideM1 * 6] = DiverseUtilities.clamp(heightMap[i][vPerSideM1], 0f, 99f) + 0.05f;
            outline[i*6+5 + vPerSideM1 * 6] = z0 + i;

            outline[i*6   + vPerSideM1 * 12] = x0 + vPerSideM1 - i;
            outline[i*6+1 + vPerSideM1 * 12] = DiverseUtilities.clamp(heightMap[vPerSideM1][vPerSideM1 - i], 0f, 99f) + 0.05f;
            outline[i*6+2 + vPerSideM1 * 12] = z0 + vPerSideM1 + lw;
            outline[i*6+3 + vPerSideM1 * 12] = x0 + vPerSideM1 - i;
            outline[i*6+4 + vPerSideM1 * 12] = DiverseUtilities.clamp(heightMap[vPerSideM1][vPerSideM1 - i], 0f, 99f) + 0.05f;
            outline[i*6+5 + vPerSideM1 * 12] = z0 + vPerSideM1 - lw;

            outline[i*6   + vPerSideM1 * 18] = x0 - lw;
            outline[i*6+1 + vPerSideM1 * 18] = DiverseUtilities.clamp(heightMap[vPerSideM1 - i][0], 0f, 99f) + 0.05f;
            outline[i*6+2 + vPerSideM1 * 18] = z0 + vPerSideM1 - i;
            outline[i*6+3 + vPerSideM1 * 18] = x0 + lw;
            outline[i*6+4 + vPerSideM1 * 18] = DiverseUtilities.clamp(heightMap[vPerSideM1 - i][0], 0f, 99f) + 0.05f;
            outline[i*6+5 + vPerSideM1 * 18] = z0 + vPerSideM1 - i;
        }
        outline[vcount-6] = x0;
        outline[vcount-5] = DiverseUtilities.clamp(heightMap[0][0], 0f, 99f) + 0.05f;
        outline[vcount-4] = z0 - lw;
        outline[vcount-3] = x0;
        outline[vcount-2] = DiverseUtilities.clamp(heightMap[0][0], 0f, 99f) + 0.05f;
        outline[vcount-1] = z0 + lw;

    }

    public float[][] getHeightMap() {
        return heightMap;
    }

    public float getHeightRelCoords(int x, int z) {
        return heightMap[x][z] * heightMultiplier;
    }

    public float getHeightAbsCoords(int z, int x) {
        return heightMap[x % Game.X_VERTICES_PER_SQUARE][z % Game.Z_VERTICES_PER_SQUARE] * heightMultiplier;
    }

    public int getIndex(int x, int y) {
        return y * Game.X_VERTICES_PER_TILE + x;
    }

    public float[] getOutline() {
        return outline;
    }

    public boolean isShown() {
        return showLine;
    }

    public void toggleLines() {
        showLine = !showLine;
    }
}
