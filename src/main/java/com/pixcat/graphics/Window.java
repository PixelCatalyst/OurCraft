package com.pixcat.graphics;

import org.lwjgl.glfw.GLFWVidMode;
import org.lwjgl.opengl.GL;

import static org.lwjgl.glfw.Callbacks.glfwFreeCallbacks;
import static org.lwjgl.glfw.GLFW.*;
import static org.lwjgl.system.MemoryUtil.NULL;

public class Window {
    private long handle;

    public Window(int width, int height, String text) {
        glfwWindowHint(GLFW_VISIBLE, GLFW_TRUE);
        glfwWindowHint(GLFW_RESIZABLE, GLFW_TRUE);
        handle = glfwCreateWindow(width, height, text, NULL, NULL);
        if (handle == 0)
            throw new RuntimeException("Failed to create the GLFW window");
    }

    public long getHandle() {
        return handle;
    }

    public boolean isOpen() {
        boolean open = false;
        if (handle != 0)
            open = !glfwWindowShouldClose(handle);
        return open;
    }

    public void bindAsCurrent() {
        glfwMakeContextCurrent(handle);
        glfwSwapInterval(1);
        GL.createCapabilities();
    }

    public void center() {
        long monitor = glfwGetPrimaryMonitor();
        GLFWVidMode vidMode = glfwGetVideoMode(glfwGetPrimaryMonitor());
        if (vidMode != null) {
            int[] monitorX = new int[1];
            int[] monitorY = new int[1];
            glfwGetMonitorPos(monitor, monitorX, monitorY);
            int[] windowWidth = new int[1];
            int[] windowHeight = new int[1];
            glfwGetWindowSize(handle, windowWidth, windowHeight);
            glfwSetWindowPos(handle,
                    monitorX[0] + (vidMode.width() - windowWidth[0]) / 2,
                    monitorY[0] + (vidMode.height() - windowHeight[0]) / 2);
        }
    }

    public void swapBuffers() {
        glfwSwapBuffers(handle);
    }

    public void destroy() {
        if (handle != 0) {
            glfwSetWindowShouldClose(handle, true);
            glfwFreeCallbacks(handle);
            glfwDestroyWindow(handle);
            handle = 0;
        }
    }
}
