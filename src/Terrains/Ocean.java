package Terrains;

import Main.*;
import org.joml.Vector3f;

public class Ocean {


    private static Texture waterTexture;
    private static Loader loader;

    private RawModel rawModel;
    private Renderer renderer;
    private float rx = 0, ry = 0, rz = 0, scale = Game.WIDTH / 2;
    private Vector3f position = new Vector3f(scale,-0.2f,scale);

    private float[] vertices = {
            -1f,0f,1f,
            1f,0f,1f,
            1f,0f,-1f,
            -1f,0f,-1f
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

    public Ocean(Renderer renderer) {
        this.renderer = renderer;
        loader = new Loader();
        waterTexture = loader.createTextureFromImageFile("ocean.png", 1024, 1024);
        rawModel = loader.createRawModel(vertices,indices,textureCoords,normals);
    }

    public static Texture getWaterTexture() {
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
