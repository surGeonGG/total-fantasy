package Shaders;

import org.joml.Matrix4f;
import org.joml.Vector3f;

public class TerrainShader extends ShaderProgram {

    private int location_texture_atlas;
    private int location_color;
    private int location_transformation_matrix;
    private int location_projection_matrix;
    private int location_view_matrix;
    private int location_light_position;
    private int location_light_color;

    public TerrainShader(String filename) {
        super(filename);
        super.bindAttribute("coordinates", 0);
        super.bindAttribute("texCoords", 1);
        super.bindAttribute("normal", 2);

        location_texture_atlas = getUniformLocation("textureAtlas");
        location_color = getUniformLocation("color");
        location_transformation_matrix = getUniformLocation("transformationMatrix");
        location_projection_matrix = getUniformLocation("projectionMatrix");
        location_view_matrix = getUniformLocation("viewMatrix");
        location_light_position = getUniformLocation("lightPosition");
        location_light_color = getUniformLocation("lightColor");
    }



    public void loadTextureAtlas(int value) {
        setUniform(location_texture_atlas, value);
    }

    public void loadColor(Vector3f value) {
        setUniform(location_color, value);
    }

    public void loadTransformationMatrix(Matrix4f value) {
        setUniform(location_transformation_matrix, value);
    }

    public void loadProjectionMatrix(Matrix4f value) {
        setUniform(location_projection_matrix, value);
    }

    public void loadViewMatrix(Matrix4f value) {
        setUniform(location_view_matrix, value);
    }

    public void loadLightPosition(Vector3f value) {
        setUniform(location_light_position, value);
    }

    public void loadLightColor(Vector3f value) {
        setUniform(location_light_color, value);
    }
}
