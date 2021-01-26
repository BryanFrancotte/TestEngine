package engine.graphics;

import engine.models.MeshModel;
import org.lwjgl.BufferUtils;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL15;
import org.lwjgl.opengl.GL20;
import org.lwjgl.opengl.GL30;
import org.newdawn.slick.opengl.Texture;
import org.newdawn.slick.opengl.TextureLoader;

import java.io.IOException;
import java.nio.FloatBuffer;
import java.nio.IntBuffer;
import java.util.ArrayList;
import java.util.List;

public class Loader {
    private List<Integer> vaos = new ArrayList<>();
    private List<Integer> vbos = new ArrayList<>();
    private List<Integer> textures = new ArrayList<>();

    public MeshModel loadToVAO(float[] positions, float[] textureCoords, float[] normals, int[] indices) {
        int voaID = createVAO();
        this.bindIndicesBuffer(indices);
        this.storeDataInAttributeList(0, 3, positions);
        this.storeDataInAttributeList(1, 2, textureCoords);
        this.storeDataInAttributeList(2, 3, normals);
        this.unbindVAO();
        return new MeshModel(voaID, indices.length);
    }

    public int loadTexture(String fileName) {
        Texture texture = null;
        try {
            texture = TextureLoader.getTexture(fileName.split("[.]")[1], Loader.class.getResourceAsStream(fileName), GL11.GL_LINEAR);
        } catch (IOException e) {
            System.err.println("Can't find the texture at " + fileName);
        }
        this.textures.add(texture.getTextureID());
        // fixing the texture
//        GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_MAG_FILTER, GL11.GL_LINEAR);
//        GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_MIN_FILTER, GL11.GL_LINEAR);
//        GL11.glTexParameteri(GL11.GL_TEXTURE_2D,GL11.GL_TEXTURE_WRAP_S, GL11.GL_REPEAT);
//        GL11.glTexParameteri(GL11.GL_TEXTURE_2D,GL11.GL_TEXTURE_WRAP_T, GL11.GL_REPEAT);
        //========================
        return texture.getTextureID();
    }

    private int createVAO() {
        int vaoID = GL30.glGenVertexArrays();
        this.vaos.add(vaoID);
        GL30.glBindVertexArray(vaoID);
        return vaoID;
    }

    private void storeDataInAttributeList(int attributeNumber, int size, float[] data){
        int vboID = GL15.glGenBuffers();
        this.vbos.add(vboID);
        GL15.glBindBuffer(GL15.GL_ARRAY_BUFFER, vboID);
        FloatBuffer buffer = storeDataInFloatBuffer(data);
        GL15.glBufferData(GL15.GL_ARRAY_BUFFER, buffer, GL15.GL_STATIC_DRAW);
        GL20.glVertexAttribPointer(attributeNumber, size, GL11.GL_FLOAT, false, 0, 0);
        GL20.glBindBuffer(GL15.GL_ARRAY_BUFFER, 0);
    }

    private void unbindVAO() {
        GL30.glBindVertexArray(0);
    }

    private void bindIndicesBuffer(int[] indices) {
        int vboID = GL15.glGenBuffers();
        this.vbos.add(vboID);
        GL15.glBindBuffer(GL15.GL_ELEMENT_ARRAY_BUFFER, vboID);
        IntBuffer buffer = storeDataInIntBuffer(indices);
        GL15.glBufferData(GL15.GL_ELEMENT_ARRAY_BUFFER, buffer, GL15.GL_STATIC_DRAW);
    }

    private IntBuffer storeDataInIntBuffer(int[] data) {
        IntBuffer buffer = BufferUtils.createIntBuffer(data.length);
        return buffer.put(data).flip();
    }

    private FloatBuffer storeDataInFloatBuffer(float[] data) {
        FloatBuffer buffer = BufferUtils.createFloatBuffer(data.length);
        return buffer.put(data).flip();
    }

    public void destroy() {
        for(int vao : this.vaos) {
            GL30.glDeleteVertexArrays(vao);
        }
        for(int vbo : this.vbos) {
            GL15.glDeleteBuffers(vbo);
        }
        for(int texture : this.textures) {
            GL11.glDeleteTextures(texture);
        }
    }
}
