package Utils;

import Entities.Entity;
import Gui.GuiElement;
import Main.Camera;
import Terrains.Ocean;
import Terrains.TerrainTile;
import org.joml.Matrix4f;
import org.joml.Vector3f;
import org.lwjgl.BufferUtils;

import java.nio.ByteBuffer;

public class DiverseUtilities {

    public static float clamp(float val, float lower, float upper) {
        if (val > upper) val = upper;
        if (val < lower) val = lower;
        return val;
    }

    public static float[][] clamp(float[][] array, float lower, float upper) {
        for (int i = 0; i < array.length; i++) {
            for (int j = 0; j < array[i].length; j++) {
                if (array[j][i] > upper) array[j][i] = upper;
                if (array[j][i] < lower) array[j][i] = lower;
            }
        }
        return array;
    }

    public static float[][] clampAndCopy(float[][] array, float lower, float upper) {
        float[][] newArray = new float[array.length][array[0].length];
        for (int i = 0; i < array.length; i++) {
            for (int j = 0; j < array[i].length; j++) {
                newArray[j][i] = array[j][i];
                if (array[j][i] > upper) newArray[j][i] = upper;
                if (array[j][i] < lower) newArray[j][i] = lower;
            }
        }
        return newArray;
    }

    public static Matrix4f createTransformationMatrix(Entity entity) {
        Matrix4f targetMatrix = new Matrix4f();
        targetMatrix.identity();
        targetMatrix.translate(entity.getPosition());
        targetMatrix.rotate((float) Math.toRadians(entity.getRx()), new Vector3f(1,0,0));
        targetMatrix.rotate((float) Math.toRadians(entity.getRy()), new Vector3f(0,1,0));
        targetMatrix.rotate((float) Math.toRadians(entity.getRz()), new Vector3f(0,0,1));
        targetMatrix.scale(new Vector3f(entity.getScale(), entity.getScale(), entity.getScale()));
        return targetMatrix;
    }

    public static Matrix4f createTransformationMatrix(TerrainTile terrainTile) {
        Matrix4f targetMatrix = new Matrix4f();
        targetMatrix.identity();
        targetMatrix.translate(terrainTile.getPosition());
        targetMatrix.rotate((float) Math.toRadians(terrainTile.getRx()), new Vector3f(1,0,0));
        targetMatrix.rotate((float) Math.toRadians(terrainTile.getRy()), new Vector3f(0,1,0));
        targetMatrix.rotate((float) Math.toRadians(terrainTile.getRz()), new Vector3f(0,0,1));
        targetMatrix.scale(new Vector3f(terrainTile.getScale(), terrainTile.getScale(), terrainTile.getScale()));
        return targetMatrix;
    }

    public static Matrix4f createTransformationMatrix(Ocean ocean) {
        Matrix4f targetMatrix = new Matrix4f();
        targetMatrix.identity();
        targetMatrix.translate(ocean.getPosition());
        targetMatrix.rotate((float) Math.toRadians(ocean.getRx()), new Vector3f(1,0,0));
        targetMatrix.rotate((float) Math.toRadians(ocean.getRy()), new Vector3f(0,1,0));
        targetMatrix.rotate((float) Math.toRadians(ocean.getRz()), new Vector3f(0,0,1));
        targetMatrix.scale(new Vector3f(ocean.getScale(), ocean.getScale(), ocean.getScale()));
        return targetMatrix;
    }

    public static Matrix4f createTransformationMatrix(GuiElement guiElement) {
        Matrix4f targetMatrix = new Matrix4f();
        targetMatrix.identity();
        targetMatrix.translate(guiElement.getPosition());
        targetMatrix.scale(new Vector3f(guiElement.getScale().x, guiElement.getScale().y, 1));
        return targetMatrix;
    }

    public static Matrix4f createViewMatrix(Camera camera) {
        Matrix4f targetMatrix = new Matrix4f();
        targetMatrix.identity();
        targetMatrix.rotate((float) Math.toRadians(camera.getPitch()), new Vector3f(1,0,0));
        targetMatrix.rotate((float) Math.toRadians(camera.getYaw()), new Vector3f(0,1,0));
        Vector3f negativeCameraPosition = new Vector3f(camera.getPosition().x, camera.getPosition().y,
                camera.getPosition().z).mul(-1);
        targetMatrix.translate(negativeCameraPosition);
        return targetMatrix;
    }

    private static ByteBuffer resizeBuffer(ByteBuffer buffer, int newCapacity) {
        ByteBuffer newBuffer = BufferUtils.createByteBuffer(newCapacity);
        buffer.flip();
        newBuffer.put(buffer);
        return newBuffer;
    }
}
