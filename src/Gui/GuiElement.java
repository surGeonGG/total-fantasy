package Gui;

import Main.*;
import org.joml.Vector2f;
import org.joml.Vector3f;

public class GuiElement {

    private Texture texture;
    private RawModel rawModel;
    private Vector2f scale = new Vector2f(1f, 1f);
    private Vector3f position = new Vector3f(0f, 0f, 0f);
    private float[] vertices = {
            -1f,1f,
            -1f,-1f,
            1f,1f,
            1f,-1f
    };

    public GuiElement(Vector2f position, Vector2f scale) {
        this.scale = scale;
        this.position = new Vector3f(position.x, position.y, 0);
        Loader loader = new Loader();
        texture = loader.createTextureFromImageFile("ocean.png", 1024, 1024);
        rawModel = loader.createRawModel(vertices);
    }

    public Texture getTexture() {
        return texture;
    }

    public RawModel getRawModel() {
        return rawModel;
    }

    public Vector3f getPosition() {
        return position;
    }

    public Vector2f getScale() {
        return scale;
    }


}

