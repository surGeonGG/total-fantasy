package Entities;

import Main.*;
import Utils.DiverseUtilities;
import org.joml.Matrix4f;
import org.joml.Vector3f;

public class Entity {

    private TexturedModel texturedModel;
    private Vector3f position = new Vector3f(0,0,0);
    private float rx = 0, ry = 0, rz = 0, scale = 1;

    public Entity(TexturedModel texturedModel, Vector3f position, float rx, float ry, float rz, float scale) {
        this.texturedModel = texturedModel;
        this.position = position;
        this.rx = rx;
        this.ry = ry;
        this.rz = rz;
        this.scale = scale;
    }

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
        return texturedModel;
    }

    public void setModel(TexturedModel model) {
        this.texturedModel = model;
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

}
