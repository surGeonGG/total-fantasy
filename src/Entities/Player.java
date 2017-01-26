package Entities;

import org.joml.Vector3f;
import Main.*;

import java.nio.ByteBuffer;

public class Player extends Entity {

    private float[] normals;
    private float[] colors;
    private Vector3f moveTo;
    private float travelspeed = 0.01f;
    private RawModel rawModel;
    private Camera camera;
    private Renderer renderer;
    private Loader loader;
    private int[] stats = { 12, 14, 11, 18, 21, 9 };

    public Player(Vector3f position, Camera camera, Loader loader, Renderer renderer) {
        setPosition(position);
        moveTo = position;
        setScale(0.1f);
        setRx(0);
        setRy(0);
        setRz(0);
        this.camera = camera;
        this.loader = loader;
        this.renderer = renderer;
        rawModel = loader.loadOBJ("stall");
        Texture texture = loader.createTextureFromImageFile("Rock4.png", 256, 256);
        setModel(loader.createTexturedModel(rawModel, texture));
    }

    private void triggerEvent(String biome) {
//        main.getEventHandler().handleEvent(biome, "1");
    }

    public void update() {
        if (getPosition() != moveTo)
            move();
    }

    public void move() {
        Vector3f vector = new Vector3f();
        moveTo.sub(getPosition(), vector);
        vector.normalize().mul(travelspeed);
        addPosition(vector);
    }

    public void moveTo(Vector3f moveTo) {
        this.moveTo = moveTo;
    }

    @Override
    public void setPosition(Vector3f position) {
        super.setPosition(position.add(0,0.5f,0));
    }

    public int[] getStats() {
        return stats;
    }

    public void printPosition() {
        System.out.println("Position: " + getPosition().x + " " + getPosition().y + " " + getPosition().z);
    }
}
