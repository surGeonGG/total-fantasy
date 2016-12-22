package Entities;

import Main.*;
import Utils.DiverseUtilities;
import org.joml.Matrix4f;
import org.joml.Vector3f;

public abstract class Entity {

    private TexturedModel model;
    private Vector3f position;
    private float rx, ry, rz, scale;

    public Entity() {
    }

    private static void render() {

    }

    private static void update() {
    }

    public void addPosition(Vector3f add) {
        position.add(add);
    }

    public void addRotation(float rx, float ry, float rz) {
        this.rx += rx;
        this.ry += ry;
        this.rz += rz;
    }

    public void scale(float scale) {
        this.scale += scale;
    }

    public TexturedModel getModel() {
        return model;
    }

    public void setModel(TexturedModel model) {
        this.model = model;
    }

    public Vector3f getPosition() {
        return position;
    }

    public void setPosition(Vector3f position) {
        this.position = position;
    }

    public float getRx() {
        return rx;
    }

    public void setRx(float rx) {
        this.rx = rx;
    }

    public float getRy() {
        return ry;
    }

    public void setRy(float ry) {
        this.ry = ry;
    }

    public float getRz() {
        return rz;
    }

    public void setRz(float rz) {
        this.rz = rz;
    }

    public float getScale() {
        return scale;
    }

    public void setScale(float scale) {
        this.scale = scale;
    }

    public Matrix4f getTransformationMatrix() {
        return DiverseUtilities.createTransformationMatrix(position, rx, ry, rz, scale);
    }
}
