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
        if(Input.isKeyDown(GLFW.GLFW_KEY_W)) { this.position.z -= 0.2f; }
        if(Input.isKeyDown(GLFW.GLFW_KEY_S)) { this.position.z += 0.2f; }
        if(Input.isKeyDown(GLFW.GLFW_KEY_D)) { this.position.x += 0.2f; }
        if(Input.isKeyDown(GLFW.GLFW_KEY_A)) { this.position.x -= 0.2f; }
        if(Input.isKeyDown(GLFW.GLFW_KEY_SPACE)) { this.position.y += 0.2f; }
        if(Input.isKeyDown(GLFW.GLFW_KEY_LEFT_CONTROL)) { this.position.y -= 0.2f; }

        if(Input.isKeyDown(GLFW.GLFW_KEY_UP)) { this.pitch -= 0.5f; }
        if(Input.isKeyDown(GLFW.GLFW_KEY_DOWN)) { this.pitch += 0.5f; }
        if(Input.isKeyDown(GLFW.GLFW_KEY_LEFT)) { this.yaw -= 0.5f; }
        if(Input.isKeyDown(GLFW.GLFW_KEY_RIGHT)) { this.yaw += 0.5f; }
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
