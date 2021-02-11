package engine.shaders;

import engine.entites.Camera;
import engine.entites.Light;
import engine.maths.Vector3f;
import engine.utils.Maths;
import org.lwjglx.util.vector.Matrix4f;

public class StaticShader extends ShaderProgram{
    private static final String VERTEX_FILE = "/shaders/vertexShader.vs";
    private static final String FRAGMENT_FILE = "/shaders/fragmentShader.fs";

    private int locationTransformationMatrix;
    private int locationProjectionMatrix;
    private int locationViewMatrix;
    private int locationLightPosition;
    private int locationLightColour;
    private int locationShineDamper;
    private int locationReflectivity;
    private int locationUseFakeLighting;
    private int locationSkyColour;

    public StaticShader() {
        super(VERTEX_FILE, FRAGMENT_FILE);
    }

    @Override
    protected void bindAttributes() {
        super.bindAttribute(0, "position");
        super.bindAttribute(1, "textureCoords");
        super.bindAttribute(2, "normal");
    }

    @Override
    protected void getAllUniformLocation() {
        this.locationTransformationMatrix = super.getUniformLocation("transformationMatrix");
        this.locationProjectionMatrix = super.getUniformLocation("projectionMatrix");
        this.locationViewMatrix = super.getUniformLocation("viewMatrix");
        this.locationLightPosition = super.getUniformLocation("lightPosition");
        this.locationLightColour = super.getUniformLocation("lightColour");
        this.locationShineDamper = super.getUniformLocation("shineDamper");
        this.locationReflectivity = super.getUniformLocation("reflectivity");
        this.locationUseFakeLighting = super.getUniformLocation("useFakeLighting");
        this.locationSkyColour = super.getUniformLocation("skyColour");
    }

    public void loadSkyColour(float r, float g, float b) {
        super.loadVector(this.locationSkyColour, new Vector3f(r, g, b));
    }

    public void loadFakeLighting(boolean useFake) {
        super.loadBoolean(this.locationUseFakeLighting, useFake);
    }

    public void loadShineVariables(float damper, float reflectivity) {
        super.loadFloat(this.locationShineDamper, damper);
        super.loadFloat(this.locationReflectivity, reflectivity);
    }

    public void loadTransformationMatrix(Matrix4f matrix) {
        super.loadMatrix(this.locationTransformationMatrix, matrix);
    }

    public void loadLight(Light light) {
        super.loadVector(this.locationLightPosition, light.getPosition());
        super.loadVector(this.locationLightColour, light.getColour());
    }

    public void loadViewMatrix(Camera camera) {
        Matrix4f viewMatrix = Maths.createViewMatrix(camera);
        super.loadMatrix(this.locationViewMatrix, viewMatrix);
    }

    public void loadProjectionMatrix(Matrix4f matrix) {
        super.loadMatrix(this.locationProjectionMatrix, matrix);
        super.loadMatrix(this.locationProjectionMatrix, matrix);
    }
}
