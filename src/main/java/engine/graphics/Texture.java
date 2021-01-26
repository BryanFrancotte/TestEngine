package engine.graphics;

public class Texture {
    private int textureID;

    private float shineDamper = 1;
    private float reflectivity = 0;

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

    public void setShineDamper(float shineDamper) {
        this.shineDamper = shineDamper;
    }

    public void setReflectivity(float reflectivity) {
        this.reflectivity = reflectivity;
    }
}
