package engine.entites;


import engine.maths.Vector3f;

public class Light {
    private Vector3f position;
    private Vector3f colour;

    public Light(Vector3f position, Vector3f colour) {
        this.position = position;
        this.colour = colour;
    }

    public Vector3f getPosition() {
        return this.position;
    }

    public void setPosition(Vector3f position) {
        this.position = position;
    }

    public Vector3f getColour() {
        return this.colour;
    }

    public void setColour(Vector3f colour) {
        this.colour = colour;
    }
}
