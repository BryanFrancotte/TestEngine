package engine.models;

import engine.graphics.Texture;

public class TexturedModel {
    private MeshModel meshModel;
    private Texture texture;

    public TexturedModel(MeshModel meshModel, Texture texture) {
        this.meshModel = meshModel;
        this.texture = texture;
    }

    public MeshModel getMeshModel() {
        return meshModel;
    }

    public Texture getTexture() {
        return texture;
    }
}
