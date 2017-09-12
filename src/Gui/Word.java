package Gui;

import org.joml.Vector2f;
import org.joml.Vector3f;

import java.util.ArrayList;
import java.util.List;

public class Word {

    private List<Letter> letters;
    private Vector3f caret;

    public Word(String word, Vector3f caret, Vector2f position, Vector2f dimensions, Font font, float fontSize) {
        this.caret = caret;
        letters = new ArrayList<>();
        float wordWidth = font.getXAdvance(word, fontSize);
        float newWidth = caret.x + wordWidth;
        if (newWidth > dimensions.x) {
            caret.x = 0;
            caret.y += font.getLineHeight(fontSize);
        }
        for (int i = 0; i < word.length(); i++) {
            Letter letter = new Letter(word.charAt(i), caret, position, dimensions, font, fontSize);
            letters.add(letter);
            caret = letter.getCaret();
            caret.z++;
        }
    }

    public List<Float> getVertices() {
        List<Float> vertices = new ArrayList<>();
        for (Letter letter : letters) {
            vertices.addAll(letter.getVertices());
        }
        return vertices;
    }

    public List<Float> getTexCoords() {
        List<Float> texCoord = new ArrayList<>();
        for (Letter letter : letters) {
            texCoord.addAll(letter.getTexCoords());
        }
        return texCoord;
    }

    public List<Integer> getIndices() {
        List<Integer> indices = new ArrayList<>();
        for (Letter letter : letters) {
            indices.addAll(letter.getIndices());
        }
        return indices;
    }

    public Vector3f getCaret() {
        return caret;
    }
}
