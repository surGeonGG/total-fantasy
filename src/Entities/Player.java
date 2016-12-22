package Entities;

import Main.Main;
import Utils.DiverseUtilities;
import org.joml.Matrix4f;
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
    private static float[] normals;
    private float[] colors;
    private static Main main;
    private static RawModel rawModel;
    private static Camera camera;
    private static Shader playerShader;
    private static MapCoord[][] map;
    private static int[] stats = { 12, 14, 11, 18, 21, 9 };

    public Player(Vector3f position, Main main) {
        setPosition(position);
        setScale(0.5f);
        setRx(0);
        setRy(0);
        setRz(0);
        this.main = main;
        camera = main.getCamera();
        playerShader = new Shader("player");
        this.map = main.getWorld().getMapCoords();

        for (int i = 0; i < vertices.length; i++) {
            vertices[i] *= getScale();
        }

        colors = new float[vertices.length*3];
        for (int i = 0; i < colors.length; i++) {
            colors[i] = 0f;
        }
        rawModel = main.getLoader().createRawModel(vertices, indices);
        try {
            ByteBuffer texByteBuffer = DiverseUtilities.ioResourceToByteBuffer("grasstexture.png", 256*256*32*4);
            Texture texture = main.getLoader().createTextureFromByteBuffer(texByteBuffer, 256, 256);
            setModel(main.getLoader().createTexturedModel(rawModel, texture));
    
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void triggerEvent(String biome) {
        main.getEventHandler().handleEvent(biome, "1");
    }

    public void render() {
        playerShader.bind();
        playerShader.setUniform("projectionMatrix", camera.getProjection());
        playerShader.setUniform("rotationMatrix", camera.getRotation());
        playerShader.setUniform("transformationMatrix", getTransformationMatrix());
        main.getRenderer().render(this, playerShader);
    }

    public void move(Vector3f vector) {

        float elevation = map[(int) (getPosition().x + vector.x)][(int)(getPosition().y + vector.y)].getBiomeElevation();
        String biome = map[(int) (getPosition().x + vector.x)][(int)(getPosition().y + vector.y)].getBiome();
        if (elevation >= 0f) {
            addPosition(vector);
            System.out.println(biome);
            triggerEvent(biome);
        }
    }

    public static int[] getStats() {
        return stats;
    }
}
