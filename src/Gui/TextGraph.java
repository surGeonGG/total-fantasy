package Gui;

import Main.Loader;
import Main.TexturedModel;
import org.joml.Vector2f;

import java.util.ArrayList;
import java.util.List;

public class TextGraph {

    private TexturedModel textModel;
    private Vector2f position;
    private Vector2f dimensions;
    private Font font;

    private List<Float> vertices;
    private List<Float> texCoords;
    private List<Integer> indices;
    private List<Text> texts;


    public TextGraph(Vector2f position, Vector2f dimensions) {
        this.position = position;
        this.dimensions = dimensions;
        vertices = new ArrayList<>();
        texCoords = new ArrayList<>();
        indices = new ArrayList<>();
        texts = new ArrayList<>();
    }

    public void refresh() {
        vertices.clear();
        texCoords.clear();
        indices.clear();
        for (Text text : texts) {
            vertices.addAll(text.getVertices());
            texCoords.addAll(text.getTexCoords());
            indices.addAll(text.getIndices());
        }
        Loader loader = new Loader();
        textModel = loader.createTexturedModel(vertices, texCoords, indices, font.getFontTexture());
    }

    public void addText(int index, String string, Font font, float fontSize) {
        this.font = font;
        texts.add(index, new Text(string, position, dimensions, font, fontSize));
        refresh();
    }

    public void removeText(int index) {
        texts.remove(index);
        refresh();
    }

    public TexturedModel getTextModel() {
        return textModel;
    }

    public void changeText(int i, String s) {
        texts.get(i).changeText(s);
        refresh();
    }
}
