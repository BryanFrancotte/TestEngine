package engine.shaders;

import engine.entites.Camera;
import engine.entites.Light;
import engine.maths.Vector3f;
import engine.utils.Maths;
import org.lwjglx.util.vector.Matrix4f;

public class TerrainShader extends ShaderProgram{
    private static final String VERTEX_FILE = "/shaders/terrainVertexShader.vs";
    private static final String FRAGMENT_FILE = "/shaders/terrainFragmentShader.fs";

    private int locationTransformationMatrix;
    private int locationProjectionMatrix;
    private int locationViewMatrix;
    private int locationLightPosition;
    private int locationLightColour;
    private int locationShineDamper;
    private int locationReflectivity;
    private int locationSkyColour;
    private int locationBackgroundTexture;
    private int locationRTexture;
    private int locationGTexture;
    private int locationBTexture;
    private int locationBlendMap;

    public TerrainShader() {
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
        this.locationSkyColour = super.getUniformLocation("skyColour");
        this.locationBackgroundTexture = super.getUniformLocation("backgroundTexture");
        this.locationRTexture = super.getUniformLocation("rTexture");
        this.locationGTexture = super.getUniformLocation("gTexture");
        this.locationBTexture = super.getUniformLocation("bTexture");
        this.locationBlendMap = super.getUniformLocation("blendMap");
    }

    public void connectTextureUnits() {
        super.loadInt(this.locationBackgroundTexture, 0);
        super.loadInt(this.locationRTexture, 1);
        super.loadInt(this.locationGTexture, 2);
        super.loadInt(this.locationBTexture, 3);
        super.loadInt(this.locationBlendMap, 4);
    }

    public void loadSkyColour(float r, float g, float b) {
        super.loadVector(this.locationSkyColour, new Vector3f(r, g, b));
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
