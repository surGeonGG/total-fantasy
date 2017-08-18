package Main;

import Entities.Player;
import org.joml.Matrix4f;
import org.joml.Vector3f;

public class Camera {

    private final float FOV = 70f ;
    private final float NEAR_PLANE = 4f;
    private final float FAR_PLANE = 3000f;
    private float aspectRatio;
    private float distanceFromPlayer = 50f;
    private float angleAroundPlayer = 0f;
    private float pitch = 20f;
    private float pitchRate = 0f;
    private float yaw = 0f;
    private float yawRate = 0f;
    private float zoomRate = 0f;

    private Matrix4f projection;
    private Vector3f position = new Vector3f(0f,0f,0f);

    private Player player;

    public Camera(int width, int height) {
        aspectRatio = (float) width / (float) height;
        projection = new Matrix4f();
        projection.identity();
        float y_scale = (float) ((float) 1/Math.tan(Math.toRadians(FOV/2f)));
        float x_scale = y_scale / aspectRatio;
        float frustum_length = FAR_PLANE - NEAR_PLANE;
        projection.m00(x_scale);
        projection.m11(y_scale);
        projection.m22(-((FAR_PLANE + NEAR_PLANE) / frustum_length));
        projection.m23(-1);
        projection.m32(-((2 * NEAR_PLANE * FAR_PLANE) / frustum_length));
        projection.m33(0);
    }

    public void followPlayer(Player player) {
        this.player = player;
        calculateCameraPosition();
    }

    public void calculateCameraPosition() {
        angleAroundPlayer += yawRate;
        pitch += pitchRate;
        zoom(zoomRate);
        float horizontalDistance = (float) (distanceFromPlayer * Math.cos(Math.toRadians(pitch)));
        float verticalDistance = (float) (distanceFromPlayer * Math.sin(Math.toRadians(pitch)));
        float theta = player.getRy() + angleAroundPlayer;
        float offsetX = (float) (horizontalDistance * Math.sin(Math.toRadians(theta)));
        float offsetZ = (float) (horizontalDistance * Math.cos(Math.toRadians(theta)));
        position.x = player.getPosition().x - offsetX;
        position.y = player.getPosition().y + verticalDistance;
        position.z = player.getPosition().z - offsetZ;
        yaw = 180 - theta;
    }

    public Matrix4f getProjectionMatrix() {
        return projection;
    }

    public void zoom(float zoom) {
        distanceFromPlayer -= zoom;
        if (distanceFromPlayer < 1) {
            distanceFromPlayer = 1;
        }
    }

    public Vector3f getPosition() { return position; }

    public void printPosition() {
        System.out.println("Camera position: " + position.x + " " + position.y + " " + position.z);
        System.out.println("Camera DTP and AAP: " + distanceFromPlayer + " " + angleAroundPlayer);

    }

    public void setPitch(float pitch) {
        this.pitch = pitch;
    }

    public void setYaw(float yaw) {
        this.yaw = yaw;
    }

    public float getPitch() {
        return pitch;
    }

    public float getYaw() {
        return yaw;
    }

    public void setPitchRate(float pitchRate) {
        this.pitchRate = pitchRate;
    }

    public void setYawRate(float yawRate) {
        this.yawRate = yawRate;
    }

    public void setZoomRate(float zoomRate) {
        this.zoomRate = zoomRate;
    }
}
