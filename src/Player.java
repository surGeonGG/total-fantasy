import org.joml.Matrix4f;
import org.joml.Vector3f;

import java.awt.*;
import java.util.Random;

public class Player {

    private Vector3f position = new Vector3f(0, 0, 0);

    private static float[] vertices;

    private static float[] normals;

    private static int[] indices;

    private float[] colors;

    private static Model model;

    private static Camera camera;

    private static Shader shader;

    private static float scale = 1f;

    public Player(Vector3f position, Camera camera, Shader shader) {
        this.position = position;
        position.x += 0.5f;
        position.y += 0.5f;
        position.z += 0.5f;

        this.camera = camera;
        this.shader = shader;

        vertices = new float[] {
                -0.5f, -0.5f, 0.5f,
                0.5f, -0.5f, 0.5f,
                0.5f, 0.5f, 0.5f,
                -0.5f, 0.5f, 0.5f,
                -0.5f, -0.5f, -0.5f,
                0.5f, -0.5f, -0.5f,
                0.5f, 0.5f, -0.5f,
                -0.5f, 0.5f, -0.5f
        };

        for (int i = 0; i < vertices.length; i++) {

            vertices[i] *= scale;

        }

        indices = new int[] {
                0, 1, 2, 2, 3, 0,
                3, 2, 6, 6, 7, 3,
                7, 6, 5, 5, 4, 7,
                4, 0, 3, 3, 7, 4,
                0, 1, 5, 5, 4, 0,
                1, 5, 6, 6, 2, 1
        };

        colors = new float[vertices.length*3];

        for (int i = 0; i < colors.length; i++) {
            colors[i] = 0f;

        }

        model = new Model(vertices, indices, colors);
    }

    public void render() {
        shader.bind();

        Matrix4f projection = new Matrix4f();
        Matrix4f pos = new Matrix4f().translate(position);
        camera.getProjection().mul(pos, projection);

        /*Matrix4f projection = new Matrix4f();
        projection.translate(camera.getPosition(), projection);*/

        shader.setUniform("projectionMatrix", projection);

        model.render();
    }

    public Vector3f getPosition() {
        return position;
    }

    public void setPosition(Vector3f position) {
        this.position = position;
    }

    public void addPosition(Vector3f position) {
        this.position = this.position.add(position);
    }
}
