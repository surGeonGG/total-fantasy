package Gui;

import Main.*;
import org.joml.Vector2f;
import org.joml.Vector3f;
import org.joml.Vector4f;

import java.util.ArrayList;
import java.util.List;

public abstract class GuiWindow {

    private boolean show = true;
    protected Vector2f position;
    protected Vector2f dimensions;
    protected Vector4f color;
    protected List<GuiElement> guiElements = new ArrayList<>();


    public GuiWindow(Vector2f position, Vector2f dimensions, Vector4f color) {
        this.position = position;
        this.dimensions = dimensions;
        this.color = color;
    }

    public Vector4f getColor() {
        return color;
    }

    public void toggleShow() {
        show = !show;
    }

    public boolean isShown() {
        return show;
    }

    public boolean coordWithinWindow(float mouseX, float mouseY) {
        boolean xTooSmall = mouseX < position.x;
        boolean xTooLarge = mouseX > position.x + dimensions.x;
        boolean yTooSmall = mouseY < position.y;
        boolean yTooLarge = mouseY > position.y + dimensions.y;
        return !xTooSmall && !xTooLarge && !yTooSmall && !yTooLarge && isShown();
    }

    public void addGuiElement(GuiElement guiElement) {
        guiElements.add(guiElement);
    }

    public List<GuiElement> getGuiElements() {
        return guiElements;
    }

    public abstract Vector3f getTextColor();

    public abstract void changeText(int i, String s);

    public abstract GuiElement getElementAt(float mouseX, float mouseY);

    public abstract Vector2f getPosition();

    public abstract TexturedModel getBoxModel();

    public abstract TexturedModel getTextModel();

    public abstract void addText(int index, String string, Font font, float fontSize);

    public abstract void render(Main guiShader);
}

