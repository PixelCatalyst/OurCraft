package com.pixcat.core;

import org.lwjgl.glfw.GLFWKeyCallback;
import org.lwjgl.glfw.GLFWMouseButtonCallback;
import org.lwjgl.glfw.GLFWScrollCallback;

import java.util.Map;
import java.util.HashMap;
import java.util.Queue;
import java.util.LinkedList;

import static org.lwjgl.glfw.GLFW.*;

public class InputBuffer {

    private static class KeyCallback extends GLFWKeyCallback {
        Map<Integer, Boolean> stateBuffer;

        public void clearBuffer() {
            stateBuffer = new HashMap<>();
        }

        KeyCallback() {
            clearBuffer();
        }

        @Override
        public void invoke(long window, int key, int scancode, int action, int mods) {
            if (key != GLFW_KEY_UNKNOWN) {
                boolean isDown = false;
                if ((action == GLFW_PRESS) || (action == GLFW_REPEAT))
                    isDown = true;
                stateBuffer.put(key, isDown);
            }
        }
    }

    private static class MouseCallback extends GLFWMouseButtonCallback {
        Queue<MouseAction> actionQueue;
        long windowHandle;
        double[] cursorX;
        double[] cursorY;

        MouseCallback(long windowHandle) {
            actionQueue = new LinkedList<>();
            this.windowHandle = windowHandle;
            cursorX = new double[1];
            cursorY = new double[1];
        }

        @Override
        public void invoke(long window, int mousekey, int action, int mods) {
            MouseAction.Button button = MouseAction.Button.NONE;
            if (mousekey == GLFW_MOUSE_BUTTON_LEFT)
                button = MouseAction.Button.LEFT;
            else if (mousekey == GLFW_MOUSE_BUTTON_RIGHT)
                button = MouseAction.Button.RIGHT;

            MouseAction.Event event = MouseAction.Event.NONE;
            if (action == GLFW_PRESS)
                event = MouseAction.Event.PRESS;
            else if (action == GLFW_RELEASE)
                event = MouseAction.Event.RELEASE;

            if ((button != MouseAction.Button.NONE) && (event != MouseAction.Event.NONE)) {
                pollCursor();
                actionQueue.add(new MouseAction(button, event, cursorX[0], cursorY[0]));
            }
        }

        void pollCursor() {
            glfwGetCursorPos(windowHandle, cursorX, cursorY);
        }

        void lockCursor() {
            glfwSetInputMode(windowHandle, GLFW_CURSOR, GLFW_CURSOR_DISABLED);
        }

        void unlockCursor() {
            glfwSetInputMode(windowHandle, GLFW_CURSOR, GLFW_CURSOR_NORMAL);
        }
    }

    private static class ScrollCallback extends GLFWScrollCallback {
        double offset;

        @Override
        public void invoke(long window, double xoffset, double yoffset) {
            offset += yoffset;
        }
    }

    private KeyCallback keyCallback;
    private MouseCallback mouseCallback;
    private ScrollCallback scrollCallback;


    public InputBuffer(long windowHandle) {
        keyCallback = new KeyCallback();
        glfwSetKeyCallback(windowHandle, keyCallback);
        mouseCallback = new MouseCallback(windowHandle);
        glfwSetMouseButtonCallback(windowHandle, mouseCallback);
        scrollCallback = new ScrollCallback();
        glfwSetScrollCallback(windowHandle, scrollCallback);
    }

    public boolean isKeyboardKeyDown(int glfwCode) {
        Boolean isDown = keyCallback.stateBuffer.get(glfwCode);
        if (isDown == null)
            return false;
        return isDown;
    }

    public void setMouseLocked() {
        mouseCallback.lockCursor();
    }

    public void setMouseUnlocked() {
        mouseCallback.unlockCursor();
    }

    public double getMouseX() {
        mouseCallback.pollCursor();
        return mouseCallback.cursorX[0];
    }

    public double getMouseY() {
        mouseCallback.pollCursor();
        return mouseCallback.cursorY[0];
    }

    public MouseAction getMouseAction() {
        if (pendingMouseAction())
            return mouseCallback.actionQueue.poll();
        return new MouseAction(); //empty action
    }

    public boolean pendingMouseAction() {
        return !mouseCallback.actionQueue.isEmpty();
    }

    public double getScrollOffset() {
        double temp = scrollCallback.offset;
        scrollCallback.offset = 0.0;
        return temp;
    }

    public void update() {
        glfwPollEvents();
    }

    public void clearKeyBuffer() {
        keyCallback.clearBuffer();
    }
}
