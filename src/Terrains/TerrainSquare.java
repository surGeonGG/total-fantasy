package Terrains;

import Main.Game;
import Main.Loader;
import Main.RawModel;
import Utils.DiverseUtilities;

public class TerrainSquare {

    private static Loader loader = new Loader();
    private String biome;
    private float[][] heightMap;
    private float heightMultiplier;
    private float x0;
    private float z0;
    private float[] outline;
    private float lw = 0.05f;

    public TerrainSquare(float[][] heightMap, float heightMultiplier, int x, int z, String biome) {
        this.heightMap = heightMap;
        x0 = x * Game.X_VERTICES_PER_SQUARE;
        z0 = z * Game.Z_VERTICES_PER_SQUARE;
        this.biome = biome;
        this.heightMultiplier = heightMultiplier;
        int sideMinus1 = Game.X_VERTICES_PER_SQUARE - 1;
        int vcount = (sideMinus1 * 4 + sideMinus1 * 4) * 3 + 6;
        outline = new float[vcount];
        for (int i = 0; i < sideMinus1; i++) {
            outline[i*6  ] = x0 + i;
            outline[i*6+1] = DiverseUtilities.clamp(heightMap[0][i], 0f, 99f) + 0.05f;
            outline[i*6+2] = z0;
            outline[i*6+3] = x0 + i;
            outline[i*6+4] = DiverseUtilities.clamp(heightMap[0][i], 0f, 99f) + 0.05f;
            outline[i*6+5] = z0 + lw;

            outline[i*6   + sideMinus1 * 6] = x0 + sideMinus1;
            outline[i*6+1 + sideMinus1 * 6] = DiverseUtilities.clamp(heightMap[i][sideMinus1], 0f, 99f) + 0.05f;
            outline[i*6+2 + sideMinus1 * 6] = z0 + i;
            outline[i*6+3 + sideMinus1 * 6] = x0 + sideMinus1 - lw;
            outline[i*6+4 + sideMinus1 * 6] = DiverseUtilities.clamp(heightMap[i][sideMinus1], 0f, 99f) + 0.05f;
            outline[i*6+5 + sideMinus1 * 6] = z0 + i;

            outline[i*6   + sideMinus1 * 12] = x0 + sideMinus1 - i;
            outline[i*6+1 + sideMinus1 * 12] = DiverseUtilities.clamp(heightMap[sideMinus1][sideMinus1 - i], 0f, 99f) + 0.05f;
            outline[i*6+2 + sideMinus1 * 12] = z0 + sideMinus1;
            outline[i*6+3 + sideMinus1 * 12] = x0 + sideMinus1 - i;
            outline[i*6+4 + sideMinus1 * 12] = DiverseUtilities.clamp(heightMap[sideMinus1][sideMinus1 - i], 0f, 99f) + 0.05f;
            outline[i*6+5 + sideMinus1 * 12] = z0 + sideMinus1 - lw;

            outline[i*6   + sideMinus1 * 18] = x0;
            outline[i*6+1 + sideMinus1 * 18] = DiverseUtilities.clamp(heightMap[sideMinus1 - i][0], 0f, 99f) + 0.05f;
            outline[i*6+2 + sideMinus1 * 18] = z0 + sideMinus1 - i;
            outline[i*6+3 + sideMinus1 * 18] = x0 + lw;
            outline[i*6+4 + sideMinus1 * 18] = DiverseUtilities.clamp(heightMap[sideMinus1 - i][0], 0f, 99f) + 0.05f;
            outline[i*6+5 + sideMinus1 * 18] = z0 + sideMinus1 - i;
        }
        outline[vcount-6] = x0;
        outline[vcount-5] = DiverseUtilities.clamp(heightMap[0][0], 0f, 99f) + 0.05f;
        outline[vcount-4] = z0;
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
        return y * Game.TILE_WIDTH + x;
    }

    public float[] getOutline() {
        return outline;
    }



}
