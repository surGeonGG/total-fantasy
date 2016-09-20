import org.joml.Matrix4f;
import org.joml.Vector3f;

public class Camera {

    private static final float FOV = 70;
    private static final float NEAR_PLANE = 0.1f;
    private static final float FAR_PLANE = 10000;
    private static float aspectRatio;

    private static float pitch = 0f, yaw = 0f, roll;

    Matrix4f projection;

    Vector3f position;

    Vector3f rotation;

    public Camera(int width, int height) {

        aspectRatio = width / height;

        position = new Vector3f(-300, -300, -100f);
        projection = new Matrix4f();

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

    public void setPosition(Vector3f position) {
        this.position = position;
    }

    public void addPosition(Vector3f position) {
        this.position.add(position);
    }

    public Vector3f getPosition() { return position; }

    public Matrix4f getProjection() {
        Matrix4f targetMatrix = new Matrix4f();
        projection.translate(position, targetMatrix);
        targetMatrix.rotate((float) Math.toRadians(getPitch()), new Vector3f(1,0,0), targetMatrix);
        targetMatrix.rotate((float) Math.toRadians(getYaw()), new Vector3f(0,0,1), targetMatrix);
        return targetMatrix;
    }

    public static float getYaw() {
        return yaw;
    }

    public static void addYaw(float yaw) {
        Camera.yaw += yaw;
    }

    public static float getPitch() {
        return pitch;
    }

    public static void addPitch(float pitch) {
        Camera.pitch += pitch;
    }

   /* public Matrix4f getRotation() {
        Matrix4f viewMatrix = new Matrix4f();
        Matrix4f.rotate((float) Math.toRadians(getPitch()), new Vector3f(1, 0, 0), viewMatrix,
                viewMatrix);
        Matrix4f.rotate((float) Math.toRadians(getYaw()), new Vector3f(0, 1, 0), viewMatrix, viewMatrix);

    }*/
}
