package engine.shaders;

import engine.maths.Vector3f;
import org.lwjgl.BufferUtils;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL20;
import org.lwjglx.util.vector.Matrix4f;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.FloatBuffer;

public abstract class ShaderProgram {
    private int programID;
    private int vertexShaderID;
    private int fragmentShaderID;

    private static FloatBuffer matrixBuffer = BufferUtils.createFloatBuffer(16);

    public ShaderProgram(String vertexFile, String fragmentFile) {
        this.vertexShaderID = loadShader(vertexFile, GL20.GL_VERTEX_SHADER);
        this.fragmentShaderID = loadShader(fragmentFile, GL20.GL_FRAGMENT_SHADER);
        this.programID = GL20.glCreateProgram();
        GL20.glAttachShader(programID, vertexShaderID);
        GL20.glAttachShader(programID, fragmentShaderID);
        this.bindAttributes();
        GL20.glLinkProgram(this.programID);
        if(GL20.glGetProgrami(this.programID, GL20.GL_LINK_STATUS) == GL11.GL_FALSE) {
            System.err.println("Program linking: " + GL20.glGetProgramInfoLog(this.programID));
            System.exit(-1);
        }
        GL20.glValidateProgram(this.programID);
        if(GL20.glGetProgrami(this.programID, GL20.GL_VALIDATE_STATUS) == GL11.GL_FALSE) {
            System.err.println("Program validation: " + GL20.glGetProgramInfoLog(this.programID));
            System.exit(-1);
        }
        this.getAllUniformLocation();
    }

    public void start() {
        GL20.glUseProgram(this.programID);
    }

    public void stop() {
        GL20.glUseProgram(0);
    }

    public void destroy() {
        this.stop();
        GL20.glDetachShader(this.programID, this.vertexShaderID);
        GL20.glDetachShader(this.programID, this.fragmentShaderID);
        GL20.glDeleteShader(this.vertexShaderID);
        GL20.glDeleteShader(this.fragmentShaderID);
        GL20.glDeleteProgram(this.programID);
    }

    protected abstract void getAllUniformLocation();

    protected int getUniformLocation(String uniformName) {
        return GL20.glGetUniformLocation(this.programID, uniformName);
    }

    protected void loadFloat(int location, float value) {
        GL20.glUniform1f(location, value);
    }

    protected void loadVector(int location, Vector3f vector) {
        GL20.glUniform3f(location, vector.getX(), vector.getY(), vector.getY());
    }

    protected void loadBoolean(int location, boolean value) {
        float valueToLoad = 0.0f;
        if(value) {
            valueToLoad = 1.0f;
        }
        GL20.glUniform1f(location, valueToLoad);
    }

    protected void loadMatrix(int location, Matrix4f matrix) {
         matrix.store(matrixBuffer);
         matrixBuffer.flip();
         GL20.glUniformMatrix4fv(location, false, matrixBuffer);
    }

    protected abstract void bindAttributes();

    protected void bindAttribute(int attribute, String variableName) {
        GL20.glBindAttribLocation(this.programID, attribute, variableName);
    }

    private static int loadShader(String path, int type) {
        // TODO: is the file loader here good ?
        StringBuilder shaderSource = new StringBuilder();
        try (BufferedReader reader = new BufferedReader(new InputStreamReader(ShaderProgram.class.getResourceAsStream(path)))){
            String line = "";
            while((line = reader.readLine()) != null) {
                shaderSource.append(line).append("\n");
            }
        } catch (IOException e) {
            System.err.println("Couldn't find the file at " + path);
            e.printStackTrace();
            System.exit(-1);
        }
        int shaderID = GL20.glCreateShader(type);
        GL20.glShaderSource(shaderID, shaderSource);
        GL20.glCompileShader(shaderID);
        if(GL20.glGetShaderi(shaderID, GL20.GL_COMPILE_STATUS) == GL11.GL_FALSE) {
            System.err.println(
                    ((type == GL20.GL_VERTEX_SHADER)? "Vertex " : "Fragment ")
                            + "shader: " + GL20.glGetShaderInfoLog(shaderID)
            );
            System.exit(-1);
        }
        return shaderID;
    }
}
