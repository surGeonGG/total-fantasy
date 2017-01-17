package Main;

import Entities.Entity;
import Entities.Light;
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
    Shader terrainShader;
    Shader entityShader;


    public Renderer(Camera camera) {
        this.camera = camera;
        terrainShader = new Shader("terrain");
        entityShader = new Shader("entity");
        terrainShader.bind();
        terrainShader.setUniform("projectionMatrix", camera.getProjectionMatrix());
        entityShader.bind();
        entityShader.setUniform("projectionMatrix", camera.getProjectionMatrix());
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
        RawModel rawModel = terrain.getRawModel();
        GL30.glBindVertexArray(rawModel.getVaoID());
        GL20.glEnableVertexAttribArray(0);
        GL20.glEnableVertexAttribArray(1);
        GL20.glEnableVertexAttribArray(2);
        glActiveTexture(GL_TEXTURE0);
        glBindTexture(GL_TEXTURE_2D, terrain.getRockTexture().getTexID());
        glActiveTexture(GL_TEXTURE1);
        glBindTexture(GL_TEXTURE_2D, terrain.getGrassTexture().getTexID());
        glActiveTexture(GL_TEXTURE2);
        glBindTexture(GL_TEXTURE_2D, terrain.getWaterTexture().getTexID());
        glActiveTexture(GL_TEXTURE3);
        glBindTexture(GL_TEXTURE_2D, terrain.getDesertTexture().getTexID());
        glActiveTexture(GL_TEXTURE4);
        glBindTexture(GL_TEXTURE_2D, terrain.getTextureMapTexture().getTexID());
        glActiveTexture(GL_TEXTURE5);
        glBindTexture(GL_TEXTURE_2D, terrain.getSnowTexture().getTexID());
        terrainShader.setUniform("rockTextureSampler", 0);
        terrainShader.setUniform("grassTextureSampler", 1);
        terrainShader.setUniform("waterTextureSampler", 2);
        terrainShader.setUniform("desertTextureSampler", 3);
        terrainShader.setUniform("textureMapSampler", 4);
        terrainShader.setUniform("snowTextureSampler", 5);
        glDrawElements(GL_TRIANGLES, rawModel.getVertexCount(), GL_UNSIGNED_INT, 0);
        terrainShader.unbind();
        GL20.glDisableVertexAttribArray(0);
        GL20.glDisableVertexAttribArray(1);
        GL20.glDisableVertexAttribArray(2);
        GL30.glBindVertexArray(0);
    }
}