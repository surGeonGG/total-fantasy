package Main;

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
    private static final float RAY_RANGE = 600;



    private Map map;
    private Vector3f currentTerrainPoint;
    Vector3f currentRay;
    Camera camera;
    Matrix4f viewMatrix;
    Matrix4f projectionMatrix;
    long windowID;

    public MousePicker(Camera camera, long windowID, Map map) {
        this.windowID = windowID;
        this.camera = camera;
        this.map = map;
        viewMatrix = DiverseUtilities.createViewMatrix(camera);
        projectionMatrix = camera.getProjectionMatrix();
    }

    public void update() {
        viewMatrix = DiverseUtilities.createViewMatrix(camera);
        currentRay = calculateCurrentRay();
        if (intersectionInRange(0, RAY_RANGE, currentRay)) {
            currentTerrainPoint = binarySearch(0, 0, RAY_RANGE, currentRay);
//            System.out.println(":)");
        } else {
//            System.out.println(":(");
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
        System.out.println(eyeCoords.x + " " + eyeCoords.y + " " + eyeCoords.z);

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
//        System.out.println("Binary search: " + count);
        float half = start + ((finish - start) / 2f);
        if (count >= RECURSION_COUNT) {
            Vector3f endPoint = getPointOnRay(ray, half);
            Map map = getMap(endPoint.x, endPoint.z);
            if (map != null) {
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
//        System.out.println("startpoint: " + startPoint);
//        System.out.println("endpoint: " + endPoint);
//        System.out.println(isUnderGround(startPoint) );
//        System.out.println(isUnderGround(endPoint) );
        if (!isUnderGround(startPoint) && isUnderGround(endPoint)) {
            return true;
        } else {
            return false;
        }
    }

    private boolean isUnderGround(Vector3f testPoint) {
        Map map = getMap(testPoint.x, testPoint.z);
        float height = 0;
        if (map != null) {
            height = map.getHeightOfMap(testPoint.x, testPoint.z);
//            System.out.println("mapheight: " + height);
        }
        if (testPoint.y < height) {
            return true;
        } else {
            return false;
        }
    }

    private Map getMap(float worldX, float worldZ) {
        return map;
    }

    public Vector3f getCurrentTerrainPoint() {
        return currentTerrainPoint;
    }
}
