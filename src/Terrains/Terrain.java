package Terrains;

import Entities.Entity;
import Main.*;
import org.joml.Vector3f;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.util.Random;

public class Terrain {

    private final float HEIGHT_MULTIPLIER = 10;
    private RawModel rawModel;
    private Texture texture;
    private TexturedModel texturedModel;
    private Camera camera;
    private Shader shader;
    private Renderer renderer;
    private Loader loader;
    private Vector3f position = new Vector3f(0,0,0);
    private float rx = 0, ry = 0, rz = 0, scale = 1;
    private float tileX, tileY;
    private float[] vertices;
    private int[] indices;
    private float[] textureCoords;
    private float[] normals;

    public Terrain(Camera camera, Shader shader, Loader loader, Renderer renderer, int tileX, int tileY, float[][] heightMap) {
        this.tileX = tileX;
        this.tileY = tileY;
        this.camera = camera;
        this.renderer = renderer;
        this.loader = loader;
        this.shader = shader;
//        this.shader = new Shader("general");
//        shader.bind();
//        shader.setUniform("projectionMatrix", camera.getProjectionMatrix());
        generateModel(heightMap);
        ByteBuffer texByteBuffer = loader.loadImageFileToByteBuffer("Grass1.png");
        texture = loader.createTextureFromByteBuffer(texByteBuffer, 1024, 1024);
    }

    private void generateModel(float[][] heightMap) {
        int numberOfVertices = heightMap.length * heightMap[0].length;
        int numberOfIndices = (heightMap.length - 1) * (heightMap[0].length - 1) * 6;
        vertices = new float[numberOfVertices * 3];
        textureCoords = new float[numberOfVertices * 2];
        normals = new float[numberOfVertices * 3];
        indices = new int[numberOfIndices];
        int vertexPointer = 0;
        int texCoordPointer = 0;
        int normalsPointer = 0;
        for (int i = 0; i < heightMap.length; i++) {
            for (int j = 0; j < heightMap[i].length; j++) {
                vertices[vertexPointer++] = j;
                vertices[vertexPointer++] = heightMap[j][i] * HEIGHT_MULTIPLIER;
                vertices[vertexPointer++] = i;
                textureCoords[texCoordPointer++] = ((float) j / ((float) heightMap[0].length - 1) * 100);
                textureCoords[texCoordPointer++] = ((float) i / ((float) heightMap.length - 1) * 100);
                Vector3f normal = calculateNormal(j, i, heightMap);
                normals[normalsPointer++] = normal.x;
                normals[normalsPointer++] = normal.y;
                normals[normalsPointer++] = normal.z;
            }
        }
        int indicesPointer = 0;
        for (int gz = 0; gz < heightMap.length - 1; gz++) {
            for (int gx = 0; gx < heightMap.length - 1; gx++) {
                int topLeft = (gz * heightMap.length) + gx;
                int topRight = topLeft + 1;
                int bottomLeft = ((gz + 1) * heightMap.length) + gx;
                int bottomRight = bottomLeft + 1;
                indices[indicesPointer++] = topLeft;
                indices[indicesPointer++] = bottomLeft;
                indices[indicesPointer++] = topRight;
                indices[indicesPointer++] = topRight;
                indices[indicesPointer++] = bottomLeft;
                indices[indicesPointer++] = bottomRight;
            }
        }
        rawModel = loader.createRawModel(vertices, indices, textureCoords, normals);
    }

    private Vector3f calculateNormal(int x, int z, float[][] heightMap) {
        float heightL = getHeight(x - 1, z, heightMap);
        float heightR = getHeight(x + 1, z, heightMap);
        float heightD = getHeight(x, z - 1, heightMap);
        float heightU = getHeight(x, z + 1, heightMap);
        Vector3f normal = new Vector3f(heightL - heightR, 2f, heightD - heightU);
        normal.normalize();
        return normal;
    }

    private float getHeight(int x, int z, float[][] heightMap) {
        if (x < 0 || x >= heightMap.length || z < 0 || z >= heightMap[0].length) {
            return 0;
        }
        float height = heightMap[x][z] * HEIGHT_MULTIPLIER;
        return height;
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
        System.out.println("Terrain position: " + position.x + " " + position.y + " " + position.z);
    }

    public float getScale() {
        return scale;
    }

    public float getRz() {
        return rz;
    }

    public float getRy() {
        return ry;
    }

    public float getRx() {
        return rx;
    }

    public Vector3f getPosition() {
        return position;
    }
}
