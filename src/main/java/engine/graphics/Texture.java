package engine.graphics;

public class Texture {
    private int textureID;

    private float shineDamper = 1;
    private float reflectivity = 0;

    private boolean hasTransparency = false;
    private boolean useFakeLighting = false;

    public Texture(int textureID) {
        this.textureID = textureID;
    }

    public int getTextureID() {
        return this.textureID;
    }

    public float getShineDamper() {
        return shineDamper;
    }

    public float getReflectivity() {
        return reflectivity;
    }

    public boolean hasTransparency() {
        return this.hasTransparency;
    }

    public boolean useFakeLighting() {
        return useFakeLighting;
    }

    public void setShineDamper(float shineDamper) {
        this.shineDamper = shineDamper;
    }

    public void setReflectivity(float reflectivity) {
        this.reflectivity = reflectivity;
    }

    public void setHasTransparency(boolean hasTransparency) {
        this.hasTransparency = hasTransparency;
    }

    public void setUseFakeLighting(boolean useFakeLighting) {
        this.useFakeLighting = useFakeLighting;
    }
}
