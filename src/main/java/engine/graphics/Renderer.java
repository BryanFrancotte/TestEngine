package engine.graphics;

import engine.entites.Entity;
import engine.io.Window;
import engine.models.TexturedModel;
import engine.shaders.StaticShader;
import engine.utils.Maths;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL13;
import org.lwjgl.opengl.GL20;
import org.lwjgl.opengl.GL30;
import org.lwjglx.util.Display;
import org.lwjglx.util.vector.Matrix4f;

public class Renderer {
    private static final float FOV = 70.0f;
    private static final float NEAR_PLANE = 0.1f;
    private static final float FAR_PLANE = 1000.0f;

    private Matrix4f projectionMatrix;

    public Renderer(Window window, StaticShader shader) {
        createProjectionMatrix(window);
        shader.start();
        shader.loadProjectionMatrix(this.projectionMatrix);
        shader.stop();
    }

    public void render(Entity entity, StaticShader shader) {
        TexturedModel model = entity.getModel();
        GL30.glBindVertexArray(model.getMeshModel().getVoaID());
        GL20.glEnableVertexAttribArray(0);
        GL20.glEnableVertexAttribArray(1);
        Matrix4f transformationMatrix =
                Maths.createTransformationMatrix(entity.getPosition(), entity.getRotX(), entity.getRotY(), entity.getRotZ(), entity.getScale());
        shader.loadTransformationMatrix(transformationMatrix);
        GL13.glActiveTexture(GL13.GL_TEXTURE0);
        GL11.glBindTexture(GL30.GL_TEXTURE_2D, model.getTexture().getTextureID());
        GL11.glDrawElements(GL11.GL_TRIANGLES, model.getMeshModel().getVertexCount(), GL11.GL_UNSIGNED_INT, 0);
        GL20.glDisableVertexAttribArray(0);
        GL20.glDisableVertexAttribArray(1);
        GL30.glBindVertexArray(0);
    }

    // TODO: try to understand this shit.
    private void createProjectionMatrix (Window window) {
        float aspectRatio = (float) window.getWidth() / (float) window.getHeight(); // TODO: Move to window class ?
        float y_scale = (float) (1f / Math.tan(Math.toRadians(FOV / 2f))) * aspectRatio;
        float x_scale = y_scale / aspectRatio;
        float frustum_length = FAR_PLANE - NEAR_PLANE;

        projectionMatrix = new Matrix4f();
        projectionMatrix.m00 = x_scale;
        projectionMatrix.m11 = y_scale;
        projectionMatrix.m22 = -((FAR_PLANE + NEAR_PLANE) / frustum_length);
        projectionMatrix.m23 = -1;
        projectionMatrix.m32 = -((2 * NEAR_PLANE * FAR_PLANE) / frustum_length);
        projectionMatrix.m33 = 0;
    }
}
