package Gui;

import org.joml.Vector2f;
import org.joml.Vector3f;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Letter {

    private List<Float> vertices;
    private List<Float> texCoords;
    private List<Integer> indices;
    private Vector3f caret;
    private Font font;
    private float fontSize;
    private char letter;
    private Vector2f dimensions;
    private Vector2f position;

    public Letter(char letter, Vector3f caret, Vector2f position, Vector2f dimensions, Font font, float fontSize) {
        vertices = new ArrayList<>();
        texCoords = new ArrayList<>();
        indices = new ArrayList<>();
        this.caret = caret;
        this.font = font;
        this.fontSize = fontSize;
        this.letter = letter;
        this.dimensions = dimensions;
        this.position = position;
        if (letter != 32) {
            refreshLetter();
            caret.x += font.getXAdvance(letter, fontSize) * 0.7f;
        }
    }

    private void refreshLetter() {
        float newWidth = caret.x + font.getXAdvance(letter, fontSize);
        if (newWidth > dimensions.x) {
            caret.x = 0;
            caret.y += font.getLineHeight(fontSize);
        }
        float xOffset =  font.getXOffset(letter, fontSize);
        float yOffset =  font.getYOffset(letter, fontSize);
        float x = (position.x + caret.x + xOffset);
        float y = (position.y + caret.y + yOffset);
        float w = font.getWidthInScreenCoords(letter, fontSize);
        float h = font.getHeightInScreenCoords(letter, fontSize);
        float leftX = x;
        float rightX = x + w;
        float upperY = -y;
        float lowerY = -y - h;
        vertices.addAll(Arrays.asList(
                leftX, lowerY,
                rightX, lowerY,
                rightX, upperY,
                leftX, lowerY,
                rightX, upperY,
                leftX, upperY
        ));
        leftX = font.getX(letter);
        lowerY = font.getY2(letter);
        rightX = font.getX2(letter);
        upperY = font.getY(letter);
        texCoords.addAll(Arrays.asList(
                leftX, lowerY,
                rightX, lowerY,
                rightX, upperY,
                leftX, lowerY,
                rightX, upperY,
                leftX, upperY
        ));
    }

    public List<Float> getVertices() {
        return vertices;
    }

    public List<Float> getTexCoords() {
        return texCoords;
    }

    public List<Integer> getIndices() {
        return indices;
    }

    public Vector3f getCaret() {
        return caret;
    }
}
