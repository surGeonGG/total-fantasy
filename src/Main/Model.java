package Main;

import org.lwjgl.BufferUtils;
import java.nio.ByteBuffer;
import java.nio.FloatBuffer;
import java.nio.IntBuffer;

import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.opengl.GL15.*;
import static org.lwjgl.opengl.GL32.GL_PROGRAM_POINT_SIZE;


public class Model {

    private int draw_count, draw_count_v;
    private int VBO_id, i_id, c_id, n_id, l_id;

    public Model(float[] vertices, int[] indices, float[] colors) {

        draw_count = indices.length;
        draw_count_v = vertices.length;

        VBO_id = glGenBuffers();
        i_id = glGenBuffers();
        c_id = glGenBuffers();
        n_id = glGenBuffers();
        l_id = glGenBuffers();

        glBindBuffer(GL_ARRAY_BUFFER, VBO_id);
        glBufferData(GL_ARRAY_BUFFER, createBuffer(vertices), GL_STATIC_DRAW);

        glBindBuffer(GL_ARRAY_BUFFER, c_id);
        glBufferData(GL_ARRAY_BUFFER, createBuffer(colors), GL_STATIC_DRAW);

        glBindBuffer(GL_ARRAY_BUFFER, n_id);
        glBufferData(GL_ARRAY_BUFFER, createBuffer(colors), GL_STATIC_DRAW);

        glBindBuffer(GL_ELEMENT_ARRAY_BUFFER, i_id);
        glBufferData(GL_ELEMENT_ARRAY_BUFFER, createBuffer(indices), GL_STATIC_DRAW);

        glBindBuffer(GL_ARRAY_BUFFER, 0);
        glBindBuffer(GL_ELEMENT_ARRAY_BUFFER, 0);
    }

    public Model(float[] vertices) {

        draw_count = vertices.length/3;

        VBO_id = glGenBuffers();

        glBindBuffer(GL_ARRAY_BUFFER, VBO_id);
        glBufferData(GL_ARRAY_BUFFER, createBuffer(vertices), GL_STATIC_DRAW);

        glBindBuffer(GL_ARRAY_BUFFER, 0);
    }

    public Model(float[] vertices, float[] colors) {

        draw_count = vertices.length/2;

        VBO_id = glGenBuffers();
        c_id = glGenBuffers();

        glBindBuffer(GL_ARRAY_BUFFER, VBO_id);
        glBufferData(GL_ARRAY_BUFFER, createBuffer(vertices), GL_STATIC_DRAW);

        glBindBuffer(GL_ARRAY_BUFFER, c_id);
        glBufferData(GL_ARRAY_BUFFER, createBuffer(colors), GL_STATIC_DRAW);

        glBindBuffer(GL_ARRAY_BUFFER, 0);
    }

    public void render() {

        glEnableClientState(GL_VERTEX_ARRAY);
        glEnableClientState(GL_COLOR_ARRAY);

        glBindBuffer(GL_ARRAY_BUFFER, VBO_id);
        glVertexPointer(3, GL_FLOAT, 0, 0);

        glBindBuffer(GL_ARRAY_BUFFER, c_id);
        glColorPointer(4, GL_FLOAT, 0, 0);

        glBindBuffer(GL_ELEMENT_ARRAY_BUFFER, i_id);
        glDrawElements(GL_TRIANGLES, draw_count, GL_UNSIGNED_INT, 0);

        glBindBuffer(GL_ARRAY_BUFFER, 0);
        glBindBuffer(GL_ELEMENT_ARRAY_BUFFER, 0);

        glDisableClientState(GL_VERTEX_ARRAY);
        glDisableClientState(GL_COLOR_ARRAY);

    }

    public void renderPoints() {

        glEnableClientState(GL_VERTEX_ARRAY);
        glEnable(GL_PROGRAM_POINT_SIZE);

        glBindBuffer(GL_ARRAY_BUFFER, VBO_id);
        glVertexPointer(3, GL_FLOAT, 0, 0);

        glDrawArrays(GL_POINTS, 0, draw_count_v);

        glDisableClientState(GL_VERTEX_ARRAY);
        glDisable(GL_PROGRAM_POINT_SIZE);
    }

    public void renderBorders() {

        glEnableClientState(GL_VERTEX_ARRAY);

        glBindBuffer(GL_ARRAY_BUFFER, VBO_id);
        glVertexPointer(3, GL_FLOAT, 0, 0);

        glDrawArrays(GL_LINES, 0, draw_count);

        glDisableClientState(GL_VERTEX_ARRAY);

    }

    private FloatBuffer createBuffer(float[] floatArray) {
        FloatBuffer buffer = BufferUtils.createFloatBuffer(floatArray.length);
        buffer.put(floatArray);
        buffer.flip();
        return buffer;
    }

    private IntBuffer createBuffer(int[] intArray) {
        IntBuffer bufferInt = BufferUtils.createIntBuffer(intArray.length);
        bufferInt.put(intArray);
        bufferInt.flip();
        return bufferInt;
    }

    private ByteBuffer createBuffer(byte[] byteArray) {
        ByteBuffer bufferByte = BufferUtils.createByteBuffer(byteArray.length);
        bufferByte.put(byteArray);
        bufferByte.flip();
        return bufferByte;
    }

    private void createProjectionMatrix() {

    }
}
