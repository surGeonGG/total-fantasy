package Terrains;

import Main.Game;
import Main.Loader;
import Utils.DiverseUtilities;

public class TerrainSquare {

    private static Loader loader = new Loader();
    private int biome;
    private float height;
    private float[] vertices;
    private float[] textureCoords;
    private float[] normals;
    private int[] indices;
    private float[] outline;
    private float lw = 0.1f;
    private boolean showLine = false;

    public TerrainSquare(float height, int x, int z, int biome, int idx) {
        this.biome = biome;
        this.height = height;

        genData(x, z, idx);
    }



    private void genData(int x, int z, int idx) {
        int w = Game.SQUARE_WIDTH;
        vertices = new float[] {
                //top
                x + w, height, z + w,
                x,     height, z + w,
                x + w, height, z,
                x,     height, z,
                //north
                x + w, height, z,
                x,     height, z,
                x + w, 0,      z,
                x,     0,      z,
                //south
                x + w, height, z + w,
                x,     height, z + w,
                x + w, 0,      z + w,
                x,     0,      z + w,
                //west
                x, height, z + w,
                x, height, z,
                x, 0,      z + w,
                x, 0,      z,
                //east
                x + w, height, z + w,
                x + w, height, z,
                x + w, 0,      z + w,
                x + w, 0,      z
        };
        textureCoords = new float[40];
        int c = 0;
        for (int i = 0; i < vertices.length; i += 3) {
            textureCoords[c++] = vertices[i] / (float) Game.WIDTH;
            textureCoords[c++] = vertices[i+2] / (float) Game.HEIGHT;
        }

        normals = new float[] {
                0,1,0,
                0,1,0,
                0,1,0,
                0,1,0,
                0,0,1,
                0,0,1,
                0,0,1,
                0,0,1,
                0,0,-1,
                0,0,-1,
                0,0,-1,
                0,0,-1,
                -1,0,0,
                -1,0,0,
                -1,0,0,
                -1,0,0,
                1,0,0,
                1,0,0,
                1,0,0,
                1,0,0
        };
        indices = new int[30];
        c = 0;
        for (int i = 0; i < 5; i++) {
            indices[c++] = idx + i * 4;
            indices[c++] = idx + 1 + i * 4;
            indices[c++] = idx + 2 + i * 4;
            indices[c++] = idx + 1 + i * 4;
            indices[c++] = idx + 2 + i * 4;
            indices[c++] = idx + 3 + i * 4;
        }
    }

    public float getHeight() {
        return height;
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

    public int getBiome() {
        return biome;
    }

    public float[] getVertices() {
        return vertices;
    }

    public float[] getTextureCoords() {
        return textureCoords;
    }

    public float[] getNormals() {
        return normals;
    }

    public int[] getIndices() {
        return indices;
    }
}
