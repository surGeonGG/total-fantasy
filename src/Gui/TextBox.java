package Gui;

import Main.Loader;
import Main.Main;
import Main.TexturedModel;
import Main.Texture;
import org.joml.Vector2f;
import org.joml.Vector3f;
import org.joml.Vector4f;
import org.lwjgl.opengl.GL30;

import java.awt.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class TextBox extends GuiWindow {

    private Loader loader = new Loader();
    private Vector3f textColor;
    private Texture boxtexture;
    private TexturedModel texturedBoxModel;
    private List<Float> vertices = new ArrayList<>();
    private List<Float> texCoords = new ArrayList<>();
    private List<Integer> indices = new ArrayList<>();
    private TextGraph textGraph;
    private ShapeGraph shapeGraph;
    private float[] padding;

    public TextBox(Vector2f position, Vector2f dimensions, Vector4f color) {
        super(position, dimensions, color);
        this.textColor = new Vector3f(1f,1f,1f);
        padding = new float[] {
                0.02f, 0.02f, 0.02f, 0.02f
        };
        textGraph = new TextGraph(padPosition(), padDimensions());
        boxtexture = loader.createTextureFromImageFile("desert.png", 1024, 1024);
        refreshBoxModel();
    }

    private Vector2f padPosition() {
        return new Vector2f(position.x + padding[3], position.y + padding[0]);
    }

    private Vector2f padDimensions() {
        return new Vector2f(dimensions.x - padding[3] - padding[1], dimensions.y - padding[0] - padding[2]);
    }

    public void refreshBoxModel() {
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
                rightX, upperY,
                leftX, lowerY,
                rightX, upperY,
                leftX, upperY
        ));
        texCoords.addAll(Arrays.asList(
                0f, 1f,
                1f, 1f,
                1f, 0f,
                0f, 1f,
                1f, 0f,
                0f, 0f
        ));
//        indices.addAll(Arrays.asList(
//                lowerLeft, lowerRight, upperRight,
//                lowerLeft, upperRight, upperLeft
//        ));

        texturedBoxModel = loader.createTexturedModel(vertices, texCoords, indices, boxtexture);
    }

    public TexturedModel getBoxModel() {
        return texturedBoxModel;
    }

    public TexturedModel getTextModel() {
        return textGraph.getTextModel();
    }

    public Vector2f getPosition() {
        return position;
    }

    public void addText(int index, String string, Font font, float fontSize) {
        textGraph.addText(index, string, font, fontSize);
        refreshBoxModel();
    }

    @Override
    public void changeText(int i, String s) {
        textGraph.changeText(i, s);
        refreshBoxModel();
    }

    @Override
    public Vector3f getTextColor() {
        return textColor;
    }

    @Override
    public GuiElement getElementAt(float mouseX, float mouseY) {
        for (GuiElement guiElement : guiElements) {
            if (guiElement.coordWithinElement(mouseX, mouseY)) {
                return guiElement.getElementAt(mouseX, mouseY);
            }
        }
        return null;
    }

    @Override
    public void render(Main guiShader) {

        GL30.glBindVertexArray(texturedBoxModel.getVaoID());
        for (GuiElement guiElement : guiElements) {
            guiElement.render(guiShader);
        }
        textGraph.render();
        shapeGraph.render();
    }
}

