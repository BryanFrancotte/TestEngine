package engine.graphics;

import engine.utlis.FileUtils;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL20;

public class Shader {
    private String vertexFile, fragmentFile;
    private int vertexID, fragmentID, programID;

    public Shader(String vertexPath, String fragmentPath) {
        this.vertexFile = FileUtils.loadAsString(vertexPath);
        this.fragmentFile = FileUtils.loadAsString(fragmentPath);
    }

    public void create() {
        this.programID = GL20.glCreateProgram();

        this.vertexID = GL20.glCreateShader(GL20.GL_VERTEX_SHADER);

        GL20.glShaderSource(this.vertexID, this.vertexFile);
        GL20.glCompileShader(this.vertexID);

        //Checking for compilation error
        if(GL20.glGetShaderi(this.vertexID, GL20.GL_COMPILE_STATUS) == GL11.GL_FALSE) {
            System.err.println("Vertex Shader: " + GL20.glGetShaderInfoLog(this.vertexID));
            return;
        }

        this.fragmentID = GL20.glCreateShader(GL20.GL_FRAGMENT_SHADER);

        GL20.glShaderSource(this.fragmentID, this.fragmentFile);
        GL20.glCompileShader(this.fragmentID);

        // Checking for compilation error
        if(GL20.glGetShaderi(this.fragmentID, GL20.GL_COMPILE_STATUS) == GL11.GL_FALSE) {
            System.err.println("Fragment Shader: " + GL20.glGetShaderInfoLog(this.fragmentID));
            return;
        }

        // Attaching the shaders to the program
        GL20.glAttachShader(this.programID, this.vertexID);
        GL20.glAttachShader(this.programID, this.fragmentID);

        GL20.glLinkProgram(this.programID);
        // Checking for linking error
        if(GL20.glGetProgrami(this.programID, GL20.GL_LINK_STATUS) == GL11.GL_FALSE) {
            System.err.println("Program linking: " + GL20.glGetProgramInfoLog(this.programID));
            return;
        }

        GL20.glValidateProgram(this.programID);
        // Checking for validation error
        if(GL20.glGetProgrami(this.programID, GL20.GL_VALIDATE_STATUS) == GL11.GL_FALSE) {
            System.err.println("Program validation: " + GL20.glGetProgramInfoLog(this.programID));
            return;
        }

        // We can now delete the shaders programs
        GL20.glDeleteShader(this.vertexID);
        GL20.glDeleteShader(this.fragmentID);
    }

    public void bind() {
        GL20.glUseProgram(this.programID);
    }

    public void unbind() {
        GL20.glUseProgram(0);
    }

    public void destroy() {
        GL20.glDeleteProgram(this.programID);
    }

}
