package Main;



public class TexturedModel {

    private RawModel rawModel;
    private Texture texture;


    public TexturedModel(RawModel rawModel, Texture texture){
        this.rawModel = rawModel;
        this.texture = texture;
    }

    public RawModel getRawModel() {
        return rawModel;
    }

    public Texture getTexture() {
        return texture;
    }

    public int getVertexCount() {
        return rawModel.getVertexCount();
    }

    public int getVaoID() {
        return rawModel.getVaoID();
    }

}