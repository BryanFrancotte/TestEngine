package engine.graphics;

import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL15;
import org.lwjgl.opengl.GL20;
import org.lwjgl.opengl.GL30;
import org.lwjgl.system.MemoryUtil;

import java.nio.FloatBuffer;
import java.nio.IntBuffer;

public class Mesh {
    private Vertex [] vertices;
    private int[] indices;
    private int vertexArrayObject, positionBufferObject, indicesBufferObject;

    public Mesh(Vertex[] vertices, int[] indices) {
        this.vertices = vertices;
        this.indices = indices;
    }

    public void create() {
        this.vertexArrayObject = GL30.glGenVertexArrays();
        GL30.glBindVertexArray(this.vertexArrayObject);

        FloatBuffer positionBuffer = MemoryUtil.memAllocFloat(this.vertices.length * 3);
        float[] positionData = new float[this.vertices.length * 3];
        for(int i = 0; i < this.vertices.length; i++) {
            positionData[i * 3] = this.vertices[i].getPosition().getX();
            positionData[i * 3 + 1] = this.vertices[i].getPosition().getY();
            positionData[i * 3 + 2] = this.vertices[i].getPosition().getZ();
        }
        positionBuffer.put(positionData).flip();

        this.positionBufferObject = GL15.glGenBuffers();
        GL15.glBindBuffer(GL15.GL_ARRAY_BUFFER, this.positionBufferObject);
        GL15.glBufferData(GL15.GL_ARRAY_BUFFER, positionBuffer, GL15.GL_STATIC_DRAW);
        GL20.glVertexAttribPointer(0, 3, GL11.GL_FLOAT, false, 0, 0);
        GL15.glBindBuffer(GL15.GL_ARRAY_BUFFER, 0); // unbinding the buffer

        IntBuffer indicesBuffer = MemoryUtil.memAllocInt(this.indices.length);
        indicesBuffer.put(this.indices).flip();

        this.indicesBufferObject = GL15.glGenBuffers();
        GL15.glBindBuffer(GL15.GL_ELEMENT_ARRAY_BUFFER, this.indicesBufferObject);
        GL15.glBufferData(GL15.GL_ELEMENT_ARRAY_BUFFER, indicesBuffer, GL15.GL_STATIC_DRAW);
        GL15.glBindBuffer(GL15.GL_ELEMENT_ARRAY_BUFFER, 0); //unbinding the buffer
    }

    public Vertex[] getVertices() {
        return this.vertices;
    }

    public int[] getIndices() {
        return this.indices;
    }

    public int getVertexArrayObject() {
        return this.vertexArrayObject;
    }

    public int getPositionBufferObject() {
        return this.positionBufferObject;
    }

    public int getIndicesBufferObject() {
        return this.indicesBufferObject;
    }
}
