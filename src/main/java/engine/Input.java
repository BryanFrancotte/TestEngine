package engine;

import org.lwjgl.glfw.*;

public class Input {
    private static boolean[] keys = new boolean[GLFW.GLFW_KEY_LAST];
    private static boolean[] buttons = new boolean[GLFW.GLFW_GAMEPAD_BUTTON_LAST];
    private static double mouseX;
    private static double mouseY;
    private static double scrollX;
    private static double scrollY;

    private GLFWKeyCallback keyboard;
    private GLFWCursorPosCallback mouseMove;
    private GLFWMouseButtonCallback mouseButtons;
    private GLFWScrollCallback mouseScroll;

    public Input() {
        // keyboard handler
        this.keyboard = new GLFWKeyCallback() {
            @Override
            public void invoke(long window, int key, int scancode, int action, int mods) {
                keys[key] = (action != GLFW.GLFW_RELEASE);
            }
        };

        this.mouseMove = new GLFWCursorPosCallback() {
            @Override
            public void invoke(long window, double xPosition, double yPosition) {
                mouseX = xPosition;
                mouseY = yPosition;
            }
        };

        this.mouseButtons = new GLFWMouseButtonCallback() {
            @Override
            public void invoke(long window, int button, int action, int mods) {
                buttons[button] = (action != GLFW.GLFW_RELEASE);
            }
        };

        this.mouseScroll = new GLFWScrollCallback() {
            @Override
            public void invoke(long window, double offsetX, double offsetY) {
                scrollX += offsetX;
                scrollY += offsetY;
            }
        };
    }

    public static boolean isKeyDown(int key) {
        return keys[key];
    }

    public static boolean isButtonDown(int button) {
        return buttons[button];
    }

    public static double getMouseX() {
        return mouseX;
    }

    public static double getMouseY() {
        return mouseY;
    }

    public static double getScrollX() {
        return scrollX;
    }

    public static double getScrollY() {
        return scrollY;
    }

    public GLFWKeyCallback getKeyboardCallback() {
        return keyboard;
    }

    public GLFWCursorPosCallback getMouseMoveCallback() {
        return mouseMove;
    }

    public GLFWMouseButtonCallback getMouseButtonsCallback() {
        return mouseButtons;
    }

    public GLFWScrollCallback getMouseScrollCallback() {
        return mouseScroll;
    }

    public void destroy() {
        keyboard.free();
        mouseMove.free();
        mouseButtons.free();
        mouseScroll.free();
    }
}
