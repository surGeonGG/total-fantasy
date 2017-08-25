package Main;

import Terrains.TerrainTile;
import Utils.DiverseUtilities;
import org.joml.Matrix4f;
import org.joml.Vector2f;
import org.joml.Vector3f;
import org.joml.Vector4f;
import org.lwjgl.BufferUtils;

import java.nio.DoubleBuffer;
import java.nio.IntBuffer;

import static org.lwjgl.glfw.GLFW.glfwGetCursorPos;
import static org.lwjgl.glfw.GLFW.glfwGetWindowSize;

public class MousePicker {

    private static final int RECURSION_COUNT = 40;
    private static final float RAY_RANGE = 6000;
    private TerrainTile[][] terrainTiles;
    private Vector3f currentTerrainPoint;
    Vector3f currentRay;
    Camera camera;
    Matrix4f viewMatrix;
    Matrix4f projectionMatrix;
    long windowID;

    public MousePicker(Camera camera, long windowID, TerrainTile[][] terrainTiles) {
        this.windowID = windowID;
        this.camera = camera;
        this.terrainTiles = terrainTiles;
        viewMatrix = DiverseUtilities.createViewMatrix(camera);
        projectionMatrix = camera.getProjectionMatrix();
    }

    public void update() {
        viewMatrix = DiverseUtilities.createViewMatrix(camera);
        currentRay = calculateCurrentRay();
        if (intersectionInRange(0, RAY_RANGE, currentRay)) {
            currentTerrainPoint = binarySearch(0, 0, RAY_RANGE, currentRay);
        } else {
            currentTerrainPoint = null;
        }
    }

    private Vector3f calculateCurrentRay() {
        DoubleBuffer posX = BufferUtils.createDoubleBuffer(1);
        DoubleBuffer posY = BufferUtils.createDoubleBuffer(1);
        glfwGetCursorPos(windowID, posX, posY);
        float mouseX = (float) posX.get();
        float mouseY = (float) posY.get();
        Vector2f normalizedCoords = getNormalisedDeviceCoordinates(mouseX, mouseY);
        Vector4f clipCoords = new Vector4f(normalizedCoords.x, normalizedCoords.y, -1f, 0f);
        Vector4f eyeCoords = toEyeCoords(clipCoords);
        Vector3f worldRay = toWorldCoords(eyeCoords);
        return worldRay;
    }

    private Vector3f toWorldCoords(Vector4f eyeCoords) {
        Matrix4f invertedViewMatrix = new Matrix4f();
        viewMatrix.invert(invertedViewMatrix);
        Vector4f rayWorld = invertedViewMatrix.transform(eyeCoords);
        Vector3f mouseRay = new Vector3f(rayWorld.x, rayWorld.y, rayWorld.z);
        mouseRay.normalize();
        return mouseRay;
    }

    private Vector4f toEyeCoords(Vector4f clipCoords) {
        Matrix4f invertedProjectionMatrix = new Matrix4f();
        projectionMatrix.invert(invertedProjectionMatrix);
        Vector4f eyeCoords = invertedProjectionMatrix.transform(clipCoords);
//        System.out.println(eyeCoords.x + " " + eyeCoords.y + " " + eyeCoords.z);

        return new Vector4f(eyeCoords.x, -eyeCoords.y, -1f, 0f);
    }

    private Vector2f getNormalisedDeviceCoordinates(float mouseX, float mouseY) {
        IntBuffer widthBuffer = BufferUtils.createIntBuffer(1);
        IntBuffer heightBuffer = BufferUtils.createIntBuffer(1);
        glfwGetWindowSize(windowID, widthBuffer, heightBuffer);
        int width = widthBuffer.get();
        int height = heightBuffer.get();
        float x = (2f * mouseX) / width - 1f;
        float y = (2f * mouseY) / height - 1f;
        System.out.println("mouseXNormalized: " + x);
        System.out.println("mouseYNormalized: " + y);

        return new Vector2f(x, y);
    }

    public Vector3f getCurrentRay() {
        return currentRay;
    }

///////////////////

    private Vector3f getPointOnRay(Vector3f ray, float distance) {
        Vector3f camPos = camera.getPosition();
        Vector3f start = new Vector3f(camPos.x, camPos.y, camPos.z);
        Vector3f scaledRay = new Vector3f(ray.x * distance, ray.y * distance, ray.z * distance);
        return scaledRay.add(start);
    }

    private Vector3f binarySearch(int count, float start, float finish, Vector3f ray) {
        float half = start + ((finish - start) / 2f);
        if (count >= RECURSION_COUNT) {
            Vector3f endPoint = getPointOnRay(ray, half);
            TerrainTile terrainTile = getMap(endPoint.x, endPoint.z);
            if (terrainTile != null) {
                return endPoint;
            } else {
                return null;
            }
        }
        if (intersectionInRange(start, half, ray)) {
            return binarySearch(count + 1, start, half, ray);
        } else {
            return binarySearch(count + 1, half, finish, ray);
        }
    }

    private boolean intersectionInRange(float start, float finish, Vector3f ray) {
        Vector3f startPoint = getPointOnRay(ray, start);
        Vector3f endPoint = getPointOnRay(ray, finish);
        if (!isUnderGround(startPoint) && isUnderGround(endPoint)) {
            return true;
        } else {
            return false;
        }
    }

    private boolean isUnderGround(Vector3f testPoint) {
        TerrainTile terrainTile = getMap(testPoint.x, testPoint.z);
        float height = 0;
        if (terrainTile != null) {
            height = terrainTile.getHeightFromMapCoords((int) testPoint.x, (int) testPoint.z);
        }
        if (testPoint.y < height) {
            return true;
        } else {
            return false;
        }
    }

    private TerrainTile getMap(float worldX, float worldZ) {
        int widthPerTile = Game.WIDTH /Game.NUMBER_OF_TILES_X;
        int heightPerTile = Game.HEIGHT /Game.NUMBER_OF_TILES_Y;
        int x = (int) (worldX / widthPerTile);
        int y = (int) (worldZ / heightPerTile);
        if (x > Game.NUMBER_OF_TILES_X - 1)
            x = Game.NUMBER_OF_TILES_X - 1;
        if (y > Game.NUMBER_OF_TILES_Y - 1)
            y = Game.NUMBER_OF_TILES_Y - 1;
        if (x < 0)
            x = 0;
        if (y < 0)
            y = 0;
        return  terrainTiles[x][y];
    }

    public Vector3f getCurrentTerrainPoint() {
        return currentTerrainPoint;
    }
}
