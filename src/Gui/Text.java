package Gui;

import Main.Loader;
import Main.RawModel;
import Main.Texture;
import Main.TexturedModel;
import org.joml.Vector2f;
import org.joml.Vector3f;
import org.joml.Vector4i;

import java.util.ArrayList;
import java.util.List;

public class Text {

    private List<Word> words;
    private Vector3f caret;
    private Vector2f position;
    private Vector2f dimensions;
    private String text;
    private Font font;
    private float fontSize;

    public Text(String text, Vector2f position, Vector2f dimensions, Font font, float fontSize) {
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
}
