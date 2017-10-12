package Shaders;

import org.joml.Vector3f;

public class EntityShader extends ShaderProgram {

    private int location_texture_atlas;
    private int location_color;

    public EntityShader(String filename) {
        super(filename);
        super.bindAttribute("coordinates", 0);
        super.bindAttribute("texCoords", 1);

        location_texture_atlas = getUniformLocation("textureAtlas");
        location_color = getUniformLocation("color");
    }

    public void loadTextureAtlas(int value) {
        setUniform(location_texture_atlas, value);
    }

    public void loadColor(Vector3f value) {
        setUniform(location_texture_atlas, value);
    }

}
