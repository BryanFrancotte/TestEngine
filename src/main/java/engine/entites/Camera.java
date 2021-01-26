package engine.entites;

import engine.io.Input;
import org.lwjgl.glfw.GLFW;
import org.lwjglx.util.vector.Vector3f;

public class Camera {
    private Vector3f position = new Vector3f(0,0,0);
    private float pitch;
    private float yaw;
    private float roll;

    public void move() {
        if(Input.isKeyDown(GLFW.GLFW_KEY_W)) { position.z -= 0.2f; }
        if(Input.isKeyDown(GLFW.GLFW_KEY_S)) { position.z += 0.2f; }
        if(Input.isKeyDown(GLFW.GLFW_KEY_D)) { position.x += 0.2f; }
        if(Input.isKeyDown(GLFW.GLFW_KEY_A)) { position.x -= 0.2f; }
    }

    public Vector3f getPosition() {
        return position;
    }

    public float getPitch() {
        return pitch;
    }

    public float getYaw() {
        return yaw;
    }

    public float getRoll() {
        return roll;
    }
}
