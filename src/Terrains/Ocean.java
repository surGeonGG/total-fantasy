package Terrains;

import Main.*;
import org.joml.Vector3f;

public class Ocean {


    private Texture waterTexture;
    private Loader loader;

    private RawModel rawModel;
    private float rx = 0, ry = 0, rz = 0, scale = 1f;
    private Vector3f position = new Vector3f(0,SimplexNoiseGenerator.OCEAN_HEIGHT_MULTIPLIER,0);

    private float[] vertices = {
            (float) 0,0f,Game.HEIGHT,
            (float) Game.WIDTH,0f,Game.HEIGHT,
            (float) Game.WIDTH,0f,0,
            (float) 0,0f,0
    };
    private int[] indices = {
            0,1,2,
            2,3,0
    };
    private float[] textureCoords = {
            0,1,
            1,1,
            0,0,
            1,0
    };
    private float[] normals = {
            0,1,0,
            0,1,0,
            0,1,0,
            0,1,0
    };

    public Ocean() {
        loader = new Loader();
        waterTexture = loader.createTextureFromImageFile("ocean.png", 1024, 1024);
        rawModel = loader.createRawModel(vertices,indices,textureCoords,normals);
    }

    public Texture getWaterTexture() {
        return waterTexture;
    }

    public RawModel getRawModel() {
        return rawModel;
    }

    public Vector3f getPosition() {
        return position;
    }

    public float getRx() {
        return rx;
    }

    public float getRy() {
        return ry;
    }

    public float getScale() {
        return scale;
    }

    public float getRz() {
        return rz;
    }
}
