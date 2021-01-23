package engine.models;

public class MeshModel {
    private int voaID;
    private int vertexCount;

    public MeshModel(int voaID, int vertexCount) {
        this.voaID = voaID;
        this.vertexCount = vertexCount;
    }

    public int getVoaID() {
        return voaID;
    }

    public int getVertexCount() {
        return vertexCount;
    }
}
