package Main;

import Entities.Entity;
import Entities.Light;
import Terrains.Ocean;
import Terrains.Terrain;
import Utils.DiverseUtilities;
import org.joml.Matrix4f;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL20;
import org.lwjgl.opengl.GL30;
import org.lwjgl.opengl.GL31;

import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.opengl.GL13.*;
import static org.lwjgl.opengles.GLES20.GL_TEXTURE3;

public class Renderer {

    private Camera camera;
    private Shader terrainShader;
    private Shader entityShader;
    private Shader oceanShader;
    private Shader guiShader;


    public Renderer(Camera camera) {
        this.camera = camera;
        terrainShader = new Shader("terrain");
        entityShader = new Shader("entity");
        oceanShader = new Shader("ocean");
        guiShader = new Shader("gui");
        terrainShader.bind();
        terrainShader.setUniform("projectionMatrix", camera.getProjectionMatrix());
        entityShader.bind();
        entityShader.setUniform("projectionMatrix", camera.getProjectionMatrix());
        oceanShader.bind();
        oceanShader.setUniform("projectionMatrix", camera.getProjectionMatrix());
        guiShader.bind();
    }

    public void prepare(){
        GL11.glClear(GL11.GL_COLOR_BUFFER_BIT);
        GL11.glClearColor(1, 0, 0, 1);
    }

    public void render(RawModel rawModel, Shader shader){
        GL30.glBindVertexArray(rawModel.getVaoID());
        GL20.glEnableVertexAttribArray(0);
        glActiveTexture(GL_TEXTURE0);
        glDrawElements(GL_TRIANGLES, rawModel.getVertexCount(), GL_UNSIGNED_INT, 0);
        GL20.glDisableVertexAttribArray(0);
        GL30.glBindVertexArray(0);
    }

    public void renderInstanced(RawModel rawModel, int instances){
        GL30.glBindVertexArray(rawModel.getVaoID());
        GL20.glEnableVertexAttribArray(0);
        GL31.glDrawElementsInstanced(GL11.GL_TRIANGLES, rawModel.getVertexCount(), GL11.GL_UNSIGNED_INT, 0, instances);
        GL20.glDisableVertexAttribArray(0);
        GL30.glBindVertexArray(0);
    }

    public void renderInstanced(TexturedModel texturedModel, int instances){
        RawModel rawModel = texturedModel.getRawModel();
        GL30.glBindVertexArray(rawModel.getVaoID());
        GL20.glEnableVertexAttribArray(0);
        glActiveTexture(GL_TEXTURE0);
        glBindTexture(GL_TEXTURE_2D, texturedModel.getTexture().getTexID());
        GL31.glDrawElementsInstanced(GL11.GL_TRIANGLES, rawModel.getVertexCount(), GL11.GL_UNSIGNED_INT, 0, instances);
        GL20.glDisableVertexAttribArray(0);
        GL30.glBindVertexArray(0);
    }

    public void renderEntity(Entity entity, Light light){
        Matrix4f transformationMatrix = DiverseUtilities.createTransformationMatrix(entity);
        entityShader.bind();
        entityShader.setUniform("transformationMatrix", transformationMatrix);
        entityShader.setUniform("viewMatrix", DiverseUtilities.createViewMatrix(camera));
        entityShader.setUniform("lightPosition", light.getPosition());
        entityShader.setUniform("lightColor", light.getColor());
        RawModel rawModel = entity.getModel().getRawModel();
        GL30.glBindVertexArray(rawModel.getVaoID());
        GL20.glEnableVertexAttribArray(0);
        GL20.glEnableVertexAttribArray(1);
        GL20.glEnableVertexAttribArray(2);
        glActiveTexture(GL_TEXTURE0);
        glBindTexture(GL_TEXTURE_2D, entity.getModel().getTexture().getTexID());
        entityShader.setUniform("textureSampler", 0);
        glDrawElements(GL_TRIANGLES, rawModel.getVertexCount(), GL_UNSIGNED_INT, 0);
        entityShader.unbind();
        GL20.glDisableVertexAttribArray(0);
        GL20.glDisableVertexAttribArray(1);
        GL20.glDisableVertexAttribArray(2);
        GL30.glBindVertexArray(0);
    }

    public void renderTerrain(Terrain terrain, Light light){
        Matrix4f transformationMatrix = DiverseUtilities.createTransformationMatrix(terrain);
        terrainShader.bind();
        terrainShader.setUniform("transformationMatrix", transformationMatrix);
        terrainShader.setUniform("viewMatrix", DiverseUtilities.createViewMatrix(camera));
        terrainShader.setUniform("lightPosition", light.getPosition());
        terrainShader.setUniform("lightColor", light.getColor());
        terrainShader.setUniform("season", terrain.getSeason());
        RawModel rawModel = terrain.getRawModel();
        GL30.glBindVertexArray(rawModel.getVaoID());
        GL20.glEnableVertexAttribArray(0);
        GL20.glEnableVertexAttribArray(1);
        GL20.glEnableVertexAttribArray(2);
        glActiveTexture(GL_TEXTURE0);
        glBindTexture(GL_TEXTURE_2D, terrain.getMountainTexture().getTexID());
        glActiveTexture(GL_TEXTURE1);
        glBindTexture(GL_TEXTURE_2D, terrain.getGrassTexture().getTexID());
        glActiveTexture(GL_TEXTURE2);
        glBindTexture(GL_TEXTURE_2D, terrain.getDesertTexture().getTexID());
        glActiveTexture(GL_TEXTURE3);
        glBindTexture(GL_TEXTURE_2D, terrain.getSnowTexture().getTexID());
        glActiveTexture(GL_TEXTURE4);
        glBindTexture(GL_TEXTURE_2D, terrain.getHeightMoisture().getTexID());
        glActiveTexture(GL_TEXTURE5);
        glBindTexture(GL_TEXTURE_2D, terrain.getForestTexture().getTexID());
        glActiveTexture(GL_TEXTURE6);
        glBindTexture(GL_TEXTURE_2D, terrain.getPermTexture().getTexID());
        glActiveTexture(GL_TEXTURE7);
        glBindTexture(GL_TEXTURE_1D, terrain.getSimplexTexture().getTexID());
        terrainShader.setUniform("mountainTextureSampler", 0);
        terrainShader.setUniform("grassTextureSampler", 1);
        terrainShader.setUniform("desertTextureSampler", 2);
        terrainShader.setUniform("snowTextureSampler", 3);
        terrainShader.setUniform("height_moisture", 4);
        terrainShader.setUniform("forestTextureSampler", 5);
        terrainShader.setUniform("permTexture", 6);
        terrainShader.setUniform("simplexTexture", 7);
        glDrawElements(GL_TRIANGLES, rawModel.getVertexCount(), GL_UNSIGNED_INT, 0);
        terrainShader.unbind();
        GL20.glDisableVertexAttribArray(0);
        GL20.glDisableVertexAttribArray(1);
        GL20.glDisableVertexAttribArray(2);
        GL30.glBindVertexArray(0);
    }

    public void renderOcean(Ocean ocean, Light light) {
        Matrix4f transformationMatrix = DiverseUtilities.createTransformationMatrix(ocean);
        oceanShader.bind();
        oceanShader.setUniform("transformationMatrix", transformationMatrix);
        oceanShader.setUniform("viewMatrix", DiverseUtilities.createViewMatrix(camera));
        oceanShader.setUniform("lightPosition", light.getPosition());
        oceanShader.setUniform("lightColor", light.getColor());
        RawModel rawModel = ocean.getRawModel();
        GL30.glBindVertexArray(rawModel.getVaoID());
        GL20.glEnableVertexAttribArray(0);
        GL20.glEnableVertexAttribArray(1);
        GL20.glEnableVertexAttribArray(2);
        glActiveTexture(GL_TEXTURE0);
        glBindTexture(GL_TEXTURE_2D, ocean.getWaterTexture().getTexID());
        oceanShader.setUniform("waterTextureSampler", 0);
        glDrawElements(GL_TRIANGLES, rawModel.getVertexCount(), GL_UNSIGNED_INT, 0);
        oceanShader.unbind();
        GL20.glDisableVertexAttribArray(0);
        GL20.glDisableVertexAttribArray(1);
        GL20.glDisableVertexAttribArray(2);
        GL30.glBindVertexArray(0);
    }

    public void renderGui(Gui.GuiElement guiElement) {
        Matrix4f transformationMatrix = DiverseUtilities.createTransformationMatrix(guiElement);
        guiShader.bind();
        guiShader.setUniform("transformationMatrix", transformationMatrix);
//        guiShader.setUniform("viewMatrix", DiverseUtilities.createViewMatrix(camera));
        RawModel rawModel = guiElement.getRawModel();
        GL30.glBindVertexArray(rawModel.getVaoID());
        GL20.glEnableVertexAttribArray(0);
        glEnable(GL_BLEND);
        glDisable(GL_DEPTH_TEST);
        glBlendFunc(GL_SRC_ALPHA, GL_ONE_MINUS_SRC_ALPHA);
        glActiveTexture(GL_TEXTURE0);
        glBindTexture(GL_TEXTURE_2D, guiElement.getTexture().getTexID());
        oceanShader.setUniform("textureSampler", 0);
        glDrawArrays(GL_TRIANGLE_STRIP, 0, rawModel.getVertexCount());
        guiShader.unbind();
        glEnable(GL_DEPTH_TEST);
        glDisable(GL_BLEND);
        GL20.glDisableVertexAttribArray(0);
        GL30.glBindVertexArray(0);
    }
}