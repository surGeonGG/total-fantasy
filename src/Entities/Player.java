package Entities;

import Main.Main;
import Utils.DiverseUtilities;
import org.joml.Vector3f;
import Main.*;

import java.io.IOException;
import java.nio.ByteBuffer;

public class Player extends Entity {

    private static float[] vertices = new float[] {
            -0.5f, -0.5f, 0.5f,
            0.5f, -0.5f, 0.5f,
            0.5f, 0.5f, 0.5f,
            -0.5f, 0.5f, 0.5f,
            -0.5f, -0.5f, -0.5f,
            0.5f, -0.5f, -0.5f,
            0.5f, 0.5f, -0.5f,
            -0.5f, 0.5f, -0.5f
    };
    private static int[] indices = new int[] {
            0, 1, 2, 2, 3, 0,
            3, 2, 6, 6, 7, 3,
            7, 6, 5, 5, 4, 7,
            4, 0, 3, 3, 7, 4,
            0, 1, 5, 5, 4, 0,
            1, 5, 6, 6, 2, 1
    };
    private static float[] texCoords = {
            0, 1,
            1, 1,
            1, 0,
            0, 0
    };
    private float[] normals;
    private float[] colors;
    private Main main;
    private RawModel rawModel;
    private Camera camera;
    private Renderer renderer;
    private Loader loader;
    private Shader playerShader;
    private MapCoord[][] map;
    private int[] stats = { 12, 14, 11, 18, 21, 9 };

    public Player(Vector3f position, Camera camera, Loader loader, Renderer renderer, Shader playerShader) {
        setPosition(position);
        setScale(1);
        setRx(0);
        setRy(0);
        setRz(0);
        this.camera = camera;
        this.loader = loader;
        this.renderer = renderer;
        this.playerShader = playerShader;
//        playerShader = new Shader("player");
//        playerShader.bind();
//        playerShader.setUniform("projectionMatrix", camera.getProjectionMatrix());

        for (int i = 0; i < vertices.length; i++) {
            vertices[i] *= getScale();
        }
        colors = new float[vertices.length*3];
        for (int i = 0; i < colors.length; i++) {
            colors[i] = 0f;
        }
        rawModel = loader.createRawModel(vertices, indices, texCoords);

        ByteBuffer texByteBuffer = loader.loadImageFileToByteBuffer("res/grasstexture.png");
        Texture texture = loader.createTextureFromByteBuffer(texByteBuffer, 256, 256);
        setModel(loader.createTexturedModel(rawModel, texture));
    }

    private void triggerEvent(String biome) {
//        main.getEventHandler().handleEvent(biome, "1");
    }

    public void render() {
        renderer.render(this, playerShader);
    }

    public void move(Vector3f vector) {

        addPosition(vector);
        printPosition();
        camera.calculateCameraPosition();
//        triggerEvent(biome);
    }

    public int[] getStats() {
        return stats;
    }

    public void printPosition() {
        System.out.println("Position: " + getPosition().x + " " + getPosition().y + " " + getPosition().z);
    }
}
