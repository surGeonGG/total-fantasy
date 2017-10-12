package Gui;

import Main.Loader;
import Main.TexturedModel;
import org.joml.Vector2f;
import org.joml.Vector4f;

import java.util.ArrayList;
import java.util.List;

public class ShapeGraph {

    private TexturedModel textModel;

    public ShapeGraph() {

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

    public TexturedModel getTextModel() {
        return textModel;
    }

}
