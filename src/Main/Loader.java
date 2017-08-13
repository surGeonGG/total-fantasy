package Main;

import org.joml.Vector2f;
import org.joml.Vector3f;
import org.lwjgl.BufferUtils;
import org.lwjgl.nuklear.*;
import org.lwjgl.system.MemoryStack;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.*;
import java.nio.ByteBuffer;
import java.nio.FloatBuffer;
import java.nio.IntBuffer;
import java.util.ArrayList;
import java.util.List;

import static org.lwjgl.nuklear.Nuklear.*;
import static org.lwjgl.opengl.GL11.*;

import static org.lwjgl.opengl.GL13.GL_TEXTURE0;
import static org.lwjgl.opengl.GL13.GL_TEXTURE9;
import static org.lwjgl.opengl.GL13.glActiveTexture;
import static org.lwjgl.opengl.GL15.*;
import static org.lwjgl.opengl.GL20.glVertexAttribPointer;
import static org.lwjgl.opengl.GL30.glBindVertexArray;
import static org.lwjgl.opengl.GL30.glDeleteVertexArrays;
import static org.lwjgl.opengl.GL30.glGenVertexArrays;
import static org.lwjgl.system.MemoryStack.stackPush;


public class Loader {

    private List<Integer> vaos = new ArrayList<Integer>();
    private List<Integer> vbos = new ArrayList<Integer>();


    public RawModel createRawModel(float[] coordinates, int[] indices) {
        int vaoID = createVAO();
        bindIndicesBuffer(indices);
        storeDataInAttributeList(0, 3, coordinates);
        unbindVAO();
        return new RawModel(vaoID, indices.length);
    }

    public RawModel createRawModel(float[] coordinates, int dimensions) {
        int vaoID = createVAO();
        storeDataInAttributeList(0, dimensions, coordinates);
        unbindVAO();
        return new RawModel(vaoID, coordinates.length/dimensions);
    }

    public RawModel createRawModel(float[] coordinates, int[] indices, float[] texCoords, float[] normals) {
        int vaoID = createVAO();
        bindIndicesBuffer(indices);
        storeDataInAttributeList(0, 3, coordinates);
        storeDataInAttributeList(1, 2, texCoords);
        storeDataInAttributeList(2, 3, normals);
        unbindVAO();
        return new RawModel(vaoID, indices.length);
    }


    public TexturedModel createTexturedModel(RawModel model, Texture texture) {
        TexturedModel texturedModel = new TexturedModel(model, texture);
        return texturedModel;
    }

    public int createVAO(float[] coordinates, int[] indices) {
        int vaoID = createVAO();
        bindIndicesBuffer(indices);
        storeDataInAttributeList(0, 3, coordinates);
        unbindVAO();
        return vaoID;
    }

    public BufferedImage loadFileToBufferedImage(String path) {
        BufferedImage bufferedImage = null;
        System.out.println(path);
        try {
            bufferedImage = ImageIO.read(new File(path));
        } catch (IOException e) {
            e.printStackTrace();
        }
        return bufferedImage;
    }

    public ByteBuffer loadImageFileToByteBuffer(String filename) {
        BufferedImage bufferedImage = loadFileToBufferedImage("res/textures/"+filename);
        ByteBuffer byteBuffer = BufferUtils.createByteBuffer(bufferedImage.getWidth() * bufferedImage.getHeight() * 4);
        for (int i = 0; i < bufferedImage.getHeight(); i++) {
            for (int j = 0; j < bufferedImage.getWidth(); j++) {
                int color = bufferedImage.getRGB(j, i);
                byteBuffer.put((byte) (color >> 16 & 0xff)); //red
//                System.out.println((byte) (color >> 16 & 0xff));
                byteBuffer.put((byte) (color >> 8 & 0xff)); //green
                byteBuffer.put((byte) (color & 0xff)); // blue
                byteBuffer.put((byte) (color >> 24 & 0xff)); //alpha

            }
        }
        byteBuffer.flip();
        return byteBuffer;
    }

    public ByteBuffer loadArrayToByteBuffer(float[][] array, float[][] array2, float[][] array3, float[][] array4) {
        ByteBuffer byteBuffer = BufferUtils.createByteBuffer(array.length * array[0].length * 4);
        for (int i = 0; i < array.length; i++) {
            for (int j = 0; j < array[i].length; j++) {
                byteBuffer.put((byte) (array[j][i] * (float) 255)); //red
                byteBuffer.put((byte) (array2[j][i] * (float) 255)); //green
                byteBuffer.put((byte) (array3[j][i] * (float) 255)); //blue
                byteBuffer.put((byte) (array4[j][i] * (float) 255)); //alpha
            }
        }
        byteBuffer.flip();
        return byteBuffer;
    }

    public ByteBuffer loadArrayToByteBuffer(float[][] array, float[][] array2) {
        ByteBuffer byteBuffer = BufferUtils.createByteBuffer(array.length * array[0].length * 4);
        for (int i = 0; i < array[0].length; i++) {
            for (int j = 0; j < array.length; j++) {
                byteBuffer.put((byte) (array[j][i] * (float) 255)); //red
                byteBuffer.put((byte) (array2[j][i] * (float) 255)); //green
                byteBuffer.put((byte) (array[j][i] * (float) 255)); //blue
                byteBuffer.put((byte) (array[j][i] * (float) 255)); //alpha
            }
        }
        byteBuffer.flip();
        return byteBuffer;
    }

    public ByteBuffer loadArrayToByteBuffer(int[] array) {
        ByteBuffer byteBuffer = BufferUtils.createByteBuffer(array.length * 4);
        for (int i = 0; i < array.length/4; i++) {
            byteBuffer.put((byte) array[i*4]); //red
            byteBuffer.put((byte) array[i*4+1]); //green
            byteBuffer.put((byte) array[i*4+2]); //blue
            byteBuffer.put((byte) array[i*4+3]); //alpha
        }
        byteBuffer.flip();
        return byteBuffer;
    }

    public ByteBuffer initPermTexture(int[] perm, int[][] grad3) {
        ByteBuffer byteBuffer = BufferUtils.createByteBuffer(perm.length * perm.length * 4);
        for (int i = 0; i < perm.length; i++) {
            for (int j = 0; j < perm.length; j++) {
                int value = perm[(j+perm[i]) & 0xFF];
                byteBuffer.put((byte) (grad3[value & 0x0F][0] * 64 + 64));   // Gradient x
                byteBuffer.put((byte) (grad3[value & 0x0F][1] * 64 + 64)); // Gradient y
                byteBuffer.put((byte) (grad3[value & 0x0F][2] * 64 + 64)); // Gradient z
                byteBuffer.put((byte) value);                     // Permuted index
            }
        }
        byteBuffer.flip();
        return byteBuffer;
    }

    //        BufferedImage bufferedImage = loadFileToBufferedImage(path);
//    public Texture loadImageFileToArray(String path) {

//    }

    public RawModel loadOBJ(String filename) {
        FileReader fileReader = null;
        String line;
        try {
            fileReader = new FileReader("res/models/"+filename+".obj");
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        BufferedReader bufferedReader = new BufferedReader(fileReader);
        List<Vector3f> vertices = new ArrayList<Vector3f>();
        List<Vector2f> textures = new ArrayList<Vector2f>();
        List<Vector3f> normals = new ArrayList<Vector3f>();
        List<Integer> indices = new ArrayList<Integer>();
        float[] verticesArray = null;
        float[] textureArray = null;
        float[] normalsArray = null;
        int[] indicesArray = null;
        try {
            while (true) {
                line = bufferedReader.readLine();
                String[] currentLine = line.split(" ");
                if (line.startsWith("v ")) {
                    Vector3f vertex = new Vector3f(Float.parseFloat(currentLine[1]), Float.parseFloat(currentLine[2]),
                            Float.parseFloat(currentLine[3]));
                    vertices.add(vertex);
                } else if (line.startsWith("vt ")) {
                    Vector2f texture = new Vector2f(Float.parseFloat(currentLine[1]), Float.parseFloat(currentLine[2]));
                    textures.add(texture);
                } else if (line.startsWith("vn ")) {
                    Vector3f normal = new Vector3f(Float.parseFloat(currentLine[1]), Float.parseFloat(currentLine[2]),
                            Float.parseFloat(currentLine[3]));
                    normals.add(normal);
                } else if (line.startsWith("f ")) {
                    textureArray = new float[vertices.size() * 2];
                    normalsArray = new float[vertices.size() * 3];
                    break;
                }
            }
            while (line != null) {
                if (!line.startsWith("f ")) {
                    line = bufferedReader.readLine();
                    continue;
                }
                String[] currentLine = line.split(" ");
                String[] vertex1 = currentLine[1].split("/");
                String[] vertex2 = currentLine[2].split("/");
                String[] vertex3 = currentLine[3].split("/");
                processVertex(vertex1, textureArray, normalsArray, indices, textures, normals);
                processVertex(vertex2, textureArray, normalsArray, indices, textures, normals);
                processVertex(vertex3, textureArray, normalsArray, indices, textures, normals);
                line = bufferedReader.readLine();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        verticesArray = new float[vertices.size() * 3];
        int counter = 0;
        for (Vector3f vertex : vertices) {
            verticesArray[counter++] = vertex.x;
            verticesArray[counter++] = vertex.y;
            verticesArray[counter++] = vertex.z;
        }
        indicesArray = new int[indices.size()];
        for (int i = 0; i < indices.size(); i++) {
            indicesArray[i] = indices.get(i);
        }
        return createRawModel(verticesArray, indicesArray, textureArray, normalsArray);
    }

    public Texture create2DTextureFromByteBuffer(ByteBuffer texture, int width, int height) {
        int texID = glGenTextures();
        glBindTexture(GL_TEXTURE_2D, texID);
        glTexParameterf(GL_TEXTURE_2D, GL_TEXTURE_MIN_FILTER, GL_NEAREST);
        glTexParameterf(GL_TEXTURE_2D, GL_TEXTURE_MAG_FILTER, GL_NEAREST);
        glTexImage2D(GL_TEXTURE_2D, 0, GL_RGBA, width, height, 0, GL_RGBA, GL_UNSIGNED_BYTE , texture);
        return new Texture(texID);
    }

    public Texture create1DTextureFromByteBuffer(ByteBuffer texture, int length) {
        int texID = glGenTextures();
        glBindTexture(GL_TEXTURE_1D, texID);
        glTexParameterf(GL_TEXTURE_1D, GL_TEXTURE_MIN_FILTER, GL_NEAREST);
        glTexParameterf(GL_TEXTURE_1D, GL_TEXTURE_MAG_FILTER, GL_NEAREST);
        glTexImage1D(GL_TEXTURE_1D, 0, GL_RGBA, length, 0, GL_RGBA, GL_UNSIGNED_BYTE , texture);
        return new Texture(texID);
    }

    private void processVertex(String[] vertex, float[] textureArray, float[] normalsArray, List<Integer> indices,
                                List<Vector2f> textures, List<Vector3f> normals) {
        int currentVertexPointer = Integer.parseInt(vertex[0]) - 1;
        indices.add(currentVertexPointer);
        Vector2f currentTex = textures.get(Integer.parseInt(vertex[1]) - 1);
        textureArray[currentVertexPointer * 2] = currentTex.x;
        textureArray[currentVertexPointer * 2 + 1] = 1 - currentTex.y;
        Vector3f currentNorm = normals.get(Integer.parseInt(vertex[2]) - 1);
        normalsArray[currentVertexPointer * 3] = currentNorm.x;
        normalsArray[currentVertexPointer * 3 + 1] = currentNorm.y;
        normalsArray[currentVertexPointer * 3 + 2] = currentNorm.z;
    }

    public void cleanUp() {
        for (int vao : vaos) {
            glDeleteVertexArrays(vao);
        }
        for (int vbo : vbos) {
            glDeleteBuffers(vbo);
        }
    }

    private int createVAO() {
        int vaoID = glGenVertexArrays();
        vaos.add(vaoID);
        glBindVertexArray(vaoID);
        return vaoID;
    }

    private void storeDataInAttributeList(int attributeNumber, int size, float[] data) {
        int vboID = glGenBuffers();
        vbos.add(vboID);
        glBindBuffer(GL_ARRAY_BUFFER, vboID);
        FloatBuffer buffer = storeDataInFloatBuffer(data);
        glBufferData(GL_ARRAY_BUFFER, buffer, GL_STATIC_DRAW);
        glVertexAttribPointer(attributeNumber, size, GL_FLOAT, false, 0, 0);
        glBindBuffer(GL_ARRAY_BUFFER, 0);
    }

    private void unbindVAO() {
        glBindVertexArray(0);
    }

    private void bindIndicesBuffer(int[] indices) {
        int vboID = glGenBuffers();
        vbos.add(vboID);
        glBindBuffer(GL_ELEMENT_ARRAY_BUFFER, vboID);
        IntBuffer buffer = storeDataInIntBuffer(indices);
        glBufferData(GL_ELEMENT_ARRAY_BUFFER, buffer, GL_STATIC_DRAW);
    }

    private IntBuffer storeDataInIntBuffer(int[] data) {
        IntBuffer buffer = BufferUtils.createIntBuffer(data.length);
        buffer.put(data);
        buffer.flip();
        return buffer;
    }

    private FloatBuffer storeDataInFloatBuffer(float[] data) {
        FloatBuffer buffer = BufferUtils.createFloatBuffer(data.length);
        buffer.put(data);
        buffer.flip();
        return buffer;
    }

    public Texture createTextureFromImageFile(String filename, int width, int height) {
        ByteBuffer texByteBuffer = loadImageFileToByteBuffer(filename);
        return create2DTextureFromByteBuffer(texByteBuffer, width, height);
    }
}