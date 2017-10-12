package Main;

import Entities.Entity;
import Gui.Gui;
import Gui.GuiWindow;
import Gui.GuiElement;
import Entities.Light;
import Shaders.*;
import Terrains.Ocean;
import Terrains.Terrain;
import Utils.DiverseUtilities;
import org.joml.Matrix4f;
import org.joml.Vector3f;
import org.joml.Vector4f;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL20;
import org.lwjgl.opengl.GL30;
import org.lwjgl.opengl.GL31;

import java.util.List;

import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.opengl.GL13.*;

public class Renderer {

    private Camera camera;
    private ShaderProgram terrainShader;
    private ShaderProgram lineShader;
    private ShaderProgram entityShader;
    private ShaderProgram oceanShader;
    private ShaderProgram guiShader;


    public Renderer(Camera camera) {
        this.camera = camera;
        terrainShader = new TerrainShader("terrain");
        entityShader = new EntityShader("entity");
        oceanShader = new OceanShader("ocean");
        guiShader = new GuiShader("gui");
        lineShader = new LineShader("line");
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

    public void render(RawModel rawModel, ShaderProgram shader){
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
        RawModel rawModel = terrain.getRawModel();
        GL30.glBindVertexArray(rawModel.getVaoID());
        GL20.glEnableVertexAttribArray(0);
        GL20.glEnableVertexAttribArray(1);
        GL20.glEnableVertexAttribArray(2);
        glActiveTexture(GL_TEXTURE4);
        glBindTexture(GL_TEXTURE_2D, terrain.getBiomesTexture().getTexID());
        terrainShader.setUniform("biomes", 4);
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

    public void renderGui(Gui gui) {
        List<GuiWindow> guiWindows = gui.getGuiWindows();
        glEnable(GL_BLEND);
        glBlendFunc(GL_SRC_ALPHA, GL_ONE_MINUS_SRC_ALPHA);
        glDisable(GL_DEPTH_TEST);
        guiShader.bind();
        for (GuiWindow guiWindow : guiWindows) {




            if (!guiWindow.isShown()) {
                continue;
            }
            TexturedModel boxModel = guiWindow.getBoxModel();
            TexturedModel textModel = guiWindow.getTextModel();

            GL30.glBindVertexArray(boxModel.getVaoID());
            GL20.glEnableVertexAttribArray(0);
            GL20.glEnableVertexAttribArray(1);
            glActiveTexture(GL_TEXTURE0);
            glBindTexture(GL_TEXTURE_2D, boxModel.getTexture().getTexID());
            guiShader.setUniform("textureSampler", 0);
            Vector4f color = guiWindow.getColor();
            guiShader.setUniform("color", new Vector3f(color.x, color.y, color.z));

            glDrawElements(GL_TRIANGLES, boxModel.getVertexCount(), GL_UNSIGNED_INT, 0);

            GL30.glBindVertexArray(textModel.getVaoID());
            GL20.glEnableVertexAttribArray(0);
            GL20.glEnableVertexAttribArray(1);

            guiWindow.render(guiShader);


            for (GuiElement guiElement : guiWindow.getGuiElements()) {
                glBindTexture(GL_TEXTURE_2D, guiElement.getTexturedModel().getTexID());
                guiShader.setUniform("color", guiWindow.getTextColor());
                guiShader.setUniform("textureSampler", 0);
            }


            glBindTexture(GL_TEXTURE_2D, textModel.getTexture().getTexID());
            guiShader.setUniform("color", guiWindow.getTextColor());
            guiShader.setUniform("textureSampler", 0);

            glDrawElements(GL_TRIANGLES, textModel.getVertexCount(), GL_UNSIGNED_INT, 0);

            GL20.glDisableVertexAttribArray(0);
            GL20.glDisableVertexAttribArray(1);
            GL30.glBindVertexArray(0);
        }
        guiShader.unbind();
        glEnable(GL_DEPTH_TEST);
        glDisable(GL_BLEND);
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