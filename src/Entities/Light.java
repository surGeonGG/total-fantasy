package Entities;

import org.joml.Quaternionf;
import org.joml.Vector3f;

public class Light {

    private Vector3f position;
    private Vector3f color;
    private float timeOfDay = 0;

    public Light(Vector3f position, Vector3f color) {
        this.position = position;
        this.color = color;
    }

    public Vector3f getPosition() {
        return position;
    }

    public void setPosition(Vector3f position) {
        this.position = position;
    }

    public void addPosition(Vector3f movement) {
        this.position = position.add(movement);
    }

    public void advanceTime(float time) {


        timeOfDay += time;
        Quaternionf quaternionf = new Quaternionf(0,0,0.0002f,1f);
        position.rotate(quaternionf);


    }

    public Vector3f getColor() {
        return color;
    }

    public void setColor(Vector3f color) {
        this.color = color;
    }
}
