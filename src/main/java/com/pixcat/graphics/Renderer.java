package com.pixcat.graphics;

import static org.lwjgl.glfw.GLFW.*;
import static org.lwjgl.opengl.GL11.*;

public class Renderer {
    private Window window;

    public Renderer(int oglMajorVersion, int oglMinorVersion) {
        glfwWindowHint(GLFW_CONTEXT_VERSION_MAJOR, oglMajorVersion);
        glfwWindowHint(GLFW_CONTEXT_VERSION_MINOR, oglMinorVersion);
        glfwWindowHint(GLFW_OPENGL_PROFILE, GLFW_OPENGL_CORE_PROFILE);
        glfwWindowHint(GLFW_OPENGL_FORWARD_COMPAT, GL_TRUE);
    }

    public void createWindow(int width, int height, String text) {
        window = new Window(width, height, text);
        window.bindAsCurrent();
    }

    public long getWindowHandle() {
        return window.getHandle();
    }

    public boolean windowIsOpen() {
        if (window == null)
            return false;
        return window.isOpen();
    }

    public void centerWindow() {
        window.center();
    }

    public void destroyWindow() {
        window.destroy();
    }

    public void beginFrame() {
        glClearColor(((float) Math.sin(glfwGetTime()) + 1.0f) * 0.5f, 0.0f, 0.0f, 0.0f);
        glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT);
    }

    public void endFrame() {
        window.swapBuffers();
    }
}
