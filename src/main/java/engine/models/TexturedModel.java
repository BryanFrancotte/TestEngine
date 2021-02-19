package engine.models;

import engine.textures.ModelTexture;

public class TexturedModel {
    private MeshModel meshModel;
    private ModelTexture modelTexture;

    public TexturedModel(MeshModel meshModel, ModelTexture modelTexture) {
        this.meshModel = meshModel;
        this.modelTexture = modelTexture;
    }

    public MeshModel getMeshModel() {
        return meshModel;
    }

    public ModelTexture getTexture() {
        return modelTexture;
    }
}
