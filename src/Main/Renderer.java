package Main;

import Entities.Entity;
import Entities.Light;
import Terrains.Ocean;
import Terrains.TerrainTile;
import Utils.DiverseUtilities;
import org.joml.Matrix4f;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL20;
import org.lwjgl.opengl.GL30;
import org.lwjgl.opengl.GL31;

import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.opengl.GL13.*;

public class Renderer {

    private Camera camera;
    private Shader terrainShader;
    private Shader lineShader;
    private Shader entityShader;
    private Shader oceanShader;
    private Shader guiShader;


    public Renderer(Camera camera) {
        this.camera = camera;
        terrainShader = new Shader("terrain");
        entityShader = new Shader("entity");
        oceanShader = new Shader("ocean");
        guiShader = new Shader("gui");
        lineShader = new Shader("line");
        terrainShader.bind();
        terrainShader.setUniform("projectionMatrix", camera.getProjectionMatrix());
        entityShader.bind();
        entityShader.setUniform("projectionMatrix", camera.getProjectionMatrix());
        oceanShader.bind();
        oceanShader.setUniform("projectionMatrix", camera.getProjectionMatrix());
        lineShader.bind();
        lineShader.setUniform("projectionMatrix", camera.getProjectionMatrix());

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

    public void renderTerrain(TerrainTile terrainTile, Light light){
        Matrix4f transformationMatrix = DiverseUtilities.createTransformationMatrix(terrainTile);
        terrainShader.bind();
        terrainShader.setUniform("transformationMatrix", transformationMatrix);
        terrainShader.setUniform("viewMatrix", DiverseUtilities.createViewMatrix(camera));
        terrainShader.setUniform("lightPosition", light.getPosition());
        terrainShader.setUniform("lightColor", light.getColor());
        RawModel rawModel = terrainTile.getRawModel();
        GL30.glBindVertexArray(rawModel.getVaoID());
        GL20.glEnableVertexAttribArray(
                0);
        GL20.glEnableVertexAttribArray(1);
        GL20.glEnableVertexAttribArray(2);
        glActiveTexture(GL_TEXTURE4);
        glBindTexture(GL_TEXTURE_2D, terrainTile.getBiomesTexture().getTexID());
        glActiveTexture(GL_TEXTURE6);
        glBindTexture(GL_TEXTURE_2D, terrainTile.getPermTexture().getTexID());
        glActiveTexture(GL_TEXTURE7);
        glBindTexture(GL_TEXTURE_1D, terrainTile.getSimplexTexture().getTexID());
        terrainShader.setUniform("biomes", 4);
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

    public void renderOutline(RawModel outline) {
        lineShader.bind();
        lineShader.setUniform("viewMatrix", DiverseUtilities.createViewMatrix(camera));
        GL30.glBindVertexArray(outline.getVaoID());
        GL20.glEnableVertexAttribArray(0);
        int skip = outline.getVertexCount() / Game.SQUARES_PER_TILE;
        for (int i = 0; i < Game.SQUARES_PER_TILE; i++) {
            glDrawArrays(GL_TRIANGLE_STRIP, i*skip, skip);
        }
        lineShader.unbind();
        GL20.glDisableVertexAttribArray(0);
        GL30.glBindVertexArray(0);
    }
}