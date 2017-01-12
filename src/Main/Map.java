package Main;

import Entities.Entity;
import org.joml.Vector3f;

import java.nio.ByteBuffer;

public class Map extends Entity {

    private RawModel rawModel;
    private Texture texture;
    private TexturedModel texturedModel;
    private Camera camera;
    private Shader shader;
    private Renderer renderer;
    private Loader loader;
    private Vector3f position = new Vector3f(0,0,0);
    private float rx = 0, ry = 0, rz = 0, scale = 1;
    private float[] vertices = {
            -100f, 0, 100f,
            -100f, 0f, -100f,
            100f, 0f, -100f,
            100f, 0f, 100f
    };
    private int[] indices = {
            0, 1, 2,
            0, 2, 3
    };
    private float[] texCoords = {
            0f, 0f,
            0f, 1f,
            1f, 1f,
            1f, 0f
    };
    private float[] normals = {
            0f, 1f, 0f,
            0f, 1f, 0f,
            0f, 1f, 0f,
            0f, 1f, 0f
    };

    public Map(Camera camera, Shader shader, Loader loader, Renderer renderer, Vector3f position) {
        setPosition(position);
        setScale(scale);
        setRx(rx);
        setRy(ry);
        setRz(rz);
        this.camera = camera;
        this.renderer = renderer;
        this.loader = loader;
        this.shader = shader;
//        this.shader = new Shader("general");
//        shader.bind();
//        shader.setUniform("projectionMatrix", camera.getProjectionMatrix());
        rawModel = loader.createRawModel(vertices, indices, texCoords, normals);
        ByteBuffer texByteBuffer = loader.loadImageFileToByteBuffer("Grass1.png");
        texture = loader.createTextureFromByteBuffer(texByteBuffer, 2048, 2048);
    }

    public void update() {

    }

    public void render(){

        renderer.renderMap(this, shader);
    }

    public RawModel getRawModel() {
        return rawModel;
    }

    public Texture getTexture() {
        return texture;
    }

    public float getHeightOfMap(float x, float z) {
    return 0f;
    }

    public void printPosition() {
        System.out.println("Map position: " + position.x + " " + position.y + " " + position.z);
    }
}
