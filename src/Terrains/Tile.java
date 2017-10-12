package Terrains;

import Main.*;
import Utils.DiverseUtilities;

import java.util.function.Predicate;

public class Tile {

    private static Tile[][] map;
    private static float lw = 0.1f;


    private float[] vertices;
    private float[] textureCoords;
    private float[] normals;
    private int[] indices;
    private float[] outline;

    private boolean showLine = false;
    private int biome;
    private float height;
    private int x;
    private int z;

    private int population;
    private String owner;
    private boolean isCoast = false;
    private boolean isRiver = false;
    private boolean isOcean = false;
    private boolean isLake = false;

    public Tile(float height, int biome, int x, int z) {
        this.x = x;
        this.z = z;
        this.biome = biome;
        this.height = height;
    }

    public int genData(int idx, boolean[] neswIsLower) {
        int x = this.x * Game.SQUARE_WIDTH;
        int z = this.z * Game.SQUARE_WIDTH;
        int w = Game.SQUARE_WIDTH;
        int sides = 0;
        for (int i = 0; i < neswIsLower.length; i++) {
            if (neswIsLower[i]) {
                sides++;
            }
        }

        vertices = new float[sides * 12 + 12];
        textureCoords = new float[vertices.length * 2 / 3];
        normals = new float[vertices.length];
        indices = new int[vertices.length / 2];

        int c = 0;
        DiverseUtilities.addArrayToArray(
                new float[] {
                x + w, height, z + w,
                x,     height, z + w,
                x + w, height, z,
                x,     height, z },
                vertices, c * 12);
        DiverseUtilities.addArrayToArray(
                new float[] {
                        0,1,0,
                        0,1,0,
                        0,1,0,
                        0,1,0 },
                normals, c * 12);
        c++;

        if (neswIsLower[0]) {
            DiverseUtilities.addArrayToArray(
                    new float[] {
                            x + w, height, z,
                            x,     height, z,
                            x + w, 0,      z,
                            x,     0,      z },
                    vertices, c * 12);
            DiverseUtilities.addArrayToArray(
                    new float[] {
                            0,0,1,
                            0,0,1,
                            0,0,1,
                            0,0,1 },
                    normals, c * 12);
            c++;
        }

        if (neswIsLower[2]) {
            DiverseUtilities.addArrayToArray(
                    new float[] {
                            x + w, height, z + w,
                            x,     height, z + w,
                            x + w, 0,      z + w,
                            x,     0,      z + w },
                    vertices, c * 12);
            DiverseUtilities.addArrayToArray(
                    new float[] {
                            0,0,-1,
                            0,0,-1,
                            0,0,-1,
                            0,0,-1 },
                    normals, c * 12);
            c++;
        }

        if (neswIsLower[3]) {
            DiverseUtilities.addArrayToArray(
                    new float[] {
                            x, height, z + w,
                            x, height, z,
                            x, 0,      z + w,
                            x, 0,      z },
                    vertices, c * 12);
            DiverseUtilities.addArrayToArray(
                    new float[] {
                            -1,0,0,
                            -1,0,0,
                            -1,0,0,
                            -1,0,0 },
                    normals, c * 12);
            c++;
        }

        if (neswIsLower[1]) {
            DiverseUtilities.addArrayToArray(
                    new float[] {
                            x + w, height, z + w,
                            x + w, height, z,
                            x + w, 0,      z + w,
                            x + w, 0,      z },
                    vertices, c * 12);

            DiverseUtilities.addArrayToArray(
                    new float[] {
                            1,0,0,
                            1,0,0,
                            1,0,0,
                            1,0,0 },
                    normals, c * 12);
            c++;
        }

        c = 0;
        for (int i = 0; i < vertices.length; i += 3) {
            textureCoords[c++] = vertices[9] / (float) Game.WIDTH;
            textureCoords[c++] = vertices[11] / (float) Game.HEIGHT;
        }

        c = 0;
        for (int i = 0; i < sides + 1; i++) {
            indices[c++] = idx + i * 4;
            indices[c++] = idx + 1 + i * 4;
            indices[c++] = idx + 2 + i * 4;
            indices[c++] = idx + 1 + i * 4;
            indices[c++] = idx + 2 + i * 4;
            indices[c++] = idx + 3 + i * 4;
        }

        return vertices.length / 3;
    }

    public void genLines() {

        int x = this.x * Game.SQUARE_WIDTH;
        int z = this.z * Game.SQUARE_WIDTH;
        int w = Game.SQUARE_WIDTH;
        float lineHeight = height + 0.01f;
        outline = new float[] {
                x,      lineHeight, z,
                x+w,    lineHeight, z,
                x+lw,   lineHeight, z+lw,
                x+w-lw, lineHeight, z+lw,
                x+w,    lineHeight, z,

                x+w,    lineHeight, z+w,
                x+w-lw, lineHeight, z+lw,
                x+w-lw, lineHeight, z+w-lw,
                x+w,    lineHeight, z+w,

                x,      lineHeight, z+w,
                x+w-lw, lineHeight, z+w-lw,
                x+lw,   lineHeight, z+w-lw,
                x,      lineHeight, z+w,

                x,      lineHeight, z,
                x+lw,   lineHeight, z+lw,
                x+lw,   lineHeight, z+w-lw,
                x,      lineHeight, z,
        };
    }

    public Tile getNorthernNeighbor(Predicate<Tile> predicate) throws ArrayIndexOutOfBoundsException {
        try {
            if (predicate.test(map[x][z - 1])) {
                return map[x][z - 1];
            }
            throw new ArrayIndexOutOfBoundsException();
        } catch (ArrayIndexOutOfBoundsException e) {
            throw e;
        }
    }

    public Tile getWesternNeighbor(Predicate<Tile> predicate) throws ArrayIndexOutOfBoundsException {
        try {
            if (predicate.test(map[x-1][z])) {
                return map[x-1][z];
            }
            throw new ArrayIndexOutOfBoundsException();
        } catch (ArrayIndexOutOfBoundsException e) {
            throw e;
        }
    }

    public Tile getEasternNeighbor(Predicate<Tile> predicate) throws ArrayIndexOutOfBoundsException {
        try {
            if (predicate.test(map[x+1][z])) {
                return map[x+1][z];
            }
            throw new ArrayIndexOutOfBoundsException();
        } catch (ArrayIndexOutOfBoundsException e) {
            throw e;
        }
    }

    public Tile getSouthernNeighbor(Predicate<Tile> predicate) throws ArrayIndexOutOfBoundsException {
        try {
            if (predicate.test(map[x][z + 1])) {
                return map[x][z + 1];
            }
            throw new ArrayIndexOutOfBoundsException();
        } catch (ArrayIndexOutOfBoundsException e) {
            throw e;
        }
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

    public void setBiome(int biome) {
        this.biome = biome;
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

    public static void setMap(Tile[][] map) {
        Tile.map = map;
    }

    public void setCoast(boolean coast) {
        isCoast = coast;
    }

    public boolean isCoast() {
        return isCoast;
    }

    public boolean isRiver() {
        return isRiver;
    }

    public void setRiver(boolean river) {
        isRiver = river;
    }

    public boolean isOcean() {
        return isOcean;
    }

    public void setOcean(boolean ocean) {
        isOcean = ocean;
    }

    public boolean isLake() {
        return isLake;
    }

    public void setLake(boolean lake) {
        isLake = lake;
    }

    public boolean isWater() {
        return height < BiomeGenerator.LAND_LINE;
    }

    public void setHeight(float height) {
        this.height = height;
    }

    public float getHeight() {
        return height;
    }
}
