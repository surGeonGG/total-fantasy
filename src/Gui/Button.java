package Gui;

import Main.Loader;
import Main.Texture;
import Main.TexturedModel;
import org.joml.Vector2f;
import org.joml.Vector4f;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;

import static org.lwjgl.glfw.GLFW.GLFW_PRESS;

public class Button extends GuiElement {

    private List<Float> vertices = new ArrayList<>();
    private List<Float> texCoords = new ArrayList<>();
    private List<Integer> indices = new ArrayList<>();
    private Texture boxtexture;
    private Loader loader = new Loader();
    private TexturedModel buttonModel;

    public Button(Vector2f position, Vector2f dimensions, Vector4f color) {
        super(position, dimensions, color);
        boxtexture = loader.createTextureFromImageFile("desert.png", 1024, 1024);
        refreshModel();
    }

    public void refreshModel() {
        vertices.clear();
        texCoords.clear();
        indices.clear();
        float leftX =  position.x;
        float rightX = position.x + dimensions.x;
        float upperY = -position.y;
        float lowerY = -position.y - dimensions.y;
        vertices.addAll(Arrays.asList(
                leftX, lowerY,
                rightX, lowerY,
                leftX, upperY,
                rightX, upperY
        ));
        texCoords.addAll(Arrays.asList(
                0f, 1f,
                1f, 1f,
                0f, 0f,
                1f, 0f
        ));
        int lowerLeft = 0;
        int lowerRight = 1;
        int upperLeft = 2;
        int upperRight = 3;
        indices.addAll(Arrays.asList(
                lowerLeft, lowerRight, upperRight,
                lowerLeft, upperRight, upperLeft
        ));

        buttonModel = loader.createTexturedModel(vertices, texCoords, indices, boxtexture);
    }

    @Override
    public GuiElement getElementAt(float mouseX, float mouseY) {
        return null;
    }


    @Override
    public void handleMouseInput(int button, int action, int mods) {
        if (action == GLFW_PRESS) {
            Random random = new Random();
            color = new Vector4f(random.nextFloat(), random.nextFloat(), random.nextFloat(), 1f);
//            refreshModel();
        }
    }

    @Override
    public boolean handleKeyboardInput(int key, int scancode, int action, int mods) {
        return false;
    }
}
