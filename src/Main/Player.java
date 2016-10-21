package Main;

import org.joml.Matrix4f;
import org.joml.Vector3f;

public class Player {

    private Vector3f position = new Vector3f(0, 0, 0);

    private static float[] vertices;

    private static float[] normals;

    private static int[] indices;

    private float[] colors;

    private static Main main;

    private static RawModel rawModel;

    private static Camera camera;

    private static Shader shader;

    private static MapCoord[][] mapCoords;


    private static float scale = 0.5f;

    private static int[] stats = { 12, 14, 11, 18, 21, 9 };

    public Player(Vector3f position, Main main, MapCoord[][] corners) {
        this.position = position;
        position.x += 0.5f;
        position.y += 0.5f;
        position.z += 0.5f;

        this.main = main;

        camera = main.getCamera();
        shader = main.getShader();
        this.mapCoords = mapCoords;

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

        //rawModel = new RawModel(vertices, indices, colors);
    }

    private void triggerEvent(String biome) {
        main.getEventHandler().handleEvent(biome, "1");
    }

    public void render() {
        shader.bind();

        Matrix4f projection = new Matrix4f();
        Matrix4f pos = new Matrix4f().translate(position);
        camera.getProjection().mul(pos, projection);

        shader.setUniform("projectionMatrix", projection);

       // rawModel.render();
    }

    public Vector3f getPosition() {
        return position;
    }

    public void setPosition(Vector3f position) {
        this.position = position;
    }

    public void addPosition(Vector3f add) {

        float elevation = mapCoords[(int) (position.x + add.x)][(int)(position.y + add.y)].getElevation();
        String biome = mapCoords[(int) (position.x + add.x)][(int)(position.y + add.y)].getBiome();

        if (elevation > 0) {
            this.position = this.position.add(add);
            System.out.println(biome);
            triggerEvent(biome);
        }
    }



    public static int[] getStats() {
        return stats;
    }
}
