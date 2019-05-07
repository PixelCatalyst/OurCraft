package com.pixcat.graphics;

import org.lwjgl.opengl.GL;

import static org.lwjgl.glfw.Callbacks.*;
import static org.lwjgl.glfw.GLFW.*;
import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.system.MemoryUtil.*;

public class Renderer {
    private long windowHandle;

    public Renderer(int windowWidth, int windowHeight, String windowText) {
        glfwWindowHint(GLFW_VISIBLE, GLFW_TRUE);
        glfwWindowHint(GLFW_RESIZABLE, GLFW_TRUE);
        windowHandle = glfwCreateWindow(windowWidth, windowHeight, windowText, NULL, NULL);
        if (windowHandle == NULL)
            throw new RuntimeException("Failed to create the GLFW window");
        glfwMakeContextCurrent(windowHandle);
        glfwSwapInterval(1);
        GL.createCapabilities();
    }

    public long getWindowHandle() {
        return windowHandle;
    }

    public boolean windowIsOpen() {
        boolean isOpen = false;
        if (windowHandle != 0)
            isOpen = !glfwWindowShouldClose(windowHandle);
        return isOpen;
    }

    public void beginFrame() {
        glClearColor(((float) Math.sin(glfwGetTime()) + 1.0f) * 0.5f, 0.0f, 0.0f, 0.0f);
        glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT);
    }

    public void endFrame() {
        glfwSwapBuffers(windowHandle);
    }

    public void destroyWindow() {
        if (windowHandle != 0) {
            glfwSetWindowShouldClose(windowHandle, true);
            glfwFreeCallbacks(windowHandle);
            glfwDestroyWindow(windowHandle);
            windowHandle = 0;
        }
    }
}
