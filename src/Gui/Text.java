package Gui;


import org.joml.Vector2f;
import org.joml.Vector3f;
import org.joml.Vector4f;

import java.util.ArrayList;
import java.util.List;

public class Text extends GuiElement {

    private List<Word> words;
    private Vector3f caret;
    private String text;
    private Font font;
    private float fontSize;

    public Text(String text, Vector2f position, Vector2f dimensions, Font font, float fontSize, Vector4f color) {
        super(position, dimensions, color);
        words = new ArrayList<>();
        this.text = text;
        this.position = position;
        this.dimensions = dimensions;
        this.font = font;
        this.fontSize = fontSize;
        generateText();
    }

    private void generateText() {
        words.clear();
        this.caret = new Vector3f(0, 0, 0);
        String[] splitText = text.split(" ");
        for (int i = 0; i < splitText.length; i++) {
            Word word = new Word(splitText[i], caret, position, dimensions, font, fontSize);
            caret = word.getCaret();
            caret.x += font.getSpace(fontSize);
            words.add(word);
        }
    }

    public List<Float> getVertices() {
        List<Float> vertices = new ArrayList<>();
        for (Word word : words) {
            vertices.addAll(word.getVertices());
        }
        return vertices;
    }

    public List<Float> getTexCoords() {
        List<Float> texCoords = new ArrayList<>();
        for (Word word : words) {
            texCoords.addAll(word.getTexCoords());
        }
        return texCoords;
    }

    public List<Integer> getIndices() {
        List<Integer> indices = new ArrayList<>();
        for (Word word : words) {
            indices.addAll(word.getIndices());
        }
        return indices;
    }

    public void changeText(String text) {
        this.text = text;
        generateText();
    }

    @Override
    public boolean coordWithinElement(float mouseX, float mouseY) {
        return false;
    }

    @Override
    public GuiElement getElementAt(float mouseX, float mouseY) {
        return this;
    }

    @Override
    public void handleMouseInput(int button, int action, int mods) {

    }

    @Override
    public boolean handleKeyboardInput(int key, int scancode, int action, int mods) {
        return false;
    }
}
