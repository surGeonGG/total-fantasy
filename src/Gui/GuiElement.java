package Gui;

import Main.Main;
import org.joml.Vector2f;
import org.joml.Vector4f;

public abstract class GuiElement {



    private boolean show = true;
    protected Vector2f position;
    protected Vector2f dimensions;
    protected Vector4f color;


    public GuiElement(Vector2f position, Vector2f dimensions, Vector4f color) {
        this.position = position;
        this.dimensions = dimensions;
        this.color = color;
    }

    public abstract GuiElement getElementAt(float mouseX, float mouseY);

    public boolean isShown() {
        return show;
    }

    public boolean coordWithinElement(float mouseX, float mouseY) {
        boolean xTooSmall = mouseX < position.x;
        boolean xTooLarge = mouseX > position.x + dimensions.x;
        boolean yTooSmall = mouseY < position.y;
        boolean yTooLarge = mouseY > position.y + dimensions.y;
        return !xTooSmall && !xTooLarge && !yTooSmall && !yTooLarge && isShown();
    }

    public abstract void handleMouseInput(int button, int action, int mods);

    public abstract boolean handleKeyboardInput(int key, int scancode, int action, int mods);

    public Vector4f getColor() {
        return color;
    }

    public abstract void render(Main guiShader);

}
