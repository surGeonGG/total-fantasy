package Gui;

import Main.*;
import org.joml.Vector2f;
import org.joml.Vector3f;

import java.util.List;

public abstract class GuiElement {


    public abstract Vector2f getPosition();

    public abstract TexturedModel getBoxModel();

    public abstract TexturedModel getTextModel();

    public abstract void addText(int index, String string, Font font, float fontSize);

    public abstract Vector3f getTextColor();

    public abstract Vector3f getBoxColor();

    public abstract void changeText(int i, String s);
}

