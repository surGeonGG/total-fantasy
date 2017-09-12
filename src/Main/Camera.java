package Main;

import Entities.Player;
import org.joml.Matrix4f;
import org.joml.Vector3f;

public class Camera {

    private final float FOV = 40f ;
    private final float NEAR_PLANE = 4f;
    private final float FAR_PLANE = 10000f;
    private float aspectRatio;
    private float distanceFromPlayer = 50f;
    private float angleAroundPlayer = 0f;
    private float pitch = 40f;
    private float pitchRate = 0f;
    private float yaw = 0f;
    private float yawRate = 0f;
    private float zoomRate = 0f;

    private Matrix4f projection;
    private Vector3f position = new Vector3f(0f,0f,0f);

    private Player player;

    public Camera(int width, int height) {
        aspectRatio = (float) width / (float) height;
        float y_scale = (float) (1f / Math.tan(Math.toRadians(FOV / 2f)));

        float x_scale = y_scale / aspectRatio;
        float frustum_length = FAR_PLANE - NEAR_PLANE;
        float m22 = -((FAR_PLANE + NEAR_PLANE) / frustum_length);
        float m32 = -((2 * NEAR_PLANE * FAR_PLANE) / frustum_length);
        projection = new Matrix4f(
                x_scale, 0, 0, 0,
                0, y_scale, 0, 0,
                0, 0, m22, -1,
                0, 0, m32, 0
        );
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
        if (distanceFromPlayer < 20) {
            distanceFromPlayer = 20;
        }
        if (pitch > 90f) {
            pitch = 90f;
        }
        if (pitch < 2f) {
            pitch = 2f;
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
