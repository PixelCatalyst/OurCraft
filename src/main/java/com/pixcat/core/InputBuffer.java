package com.pixcat.core;

import org.lwjgl.glfw.GLFWKeyCallback;

import java.util.Map;
import java.util.HashMap;

import static org.lwjgl.glfw.GLFW.*;

public class InputBuffer {

    private static class KeyCallback extends GLFWKeyCallback {
        Map<Integer, Boolean> stateBuffer;

        KeyCallback() {
            stateBuffer = new HashMap<>();
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

    private KeyCallback keyCallback;

    public InputBuffer(long windowHandle) {
        keyCallback = new KeyCallback();
        glfwSetKeyCallback(windowHandle, keyCallback);
    }

    public boolean isKeyDown(int keyCode) {
        Boolean isDown = keyCallback.stateBuffer.get(keyCode);
        if (isDown == null)
            return false;
        return isDown;
    }

    public void update() {
        glfwPollEvents();
    }
}
