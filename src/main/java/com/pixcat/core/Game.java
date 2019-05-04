package com.pixcat.core;

import com.pixcat.graphics.Renderer;
import org.lwjgl.glfw.*;

import static org.lwjgl.glfw.GLFW.*;

public class Game {
    private Renderer renderer;

    private void run() {
        System.out.println("Chyba działa XD");

        init();
        loop();
        shutdown();
    }

    private void init() {
        initGLFW();
        renderer = new Renderer(300, 300, "OurCraft");

        glfwSetKeyCallback(renderer.getWindowHandle(), (window, key, scancode, action, mods) -> {
            if (key == GLFW_KEY_ESCAPE && action == GLFW_RELEASE)
                glfwSetWindowShouldClose(window, true);
        });
    }

    private void initGLFW() {
        GLFWErrorCallback.createPrint(System.err).set();
        if (glfwInit() == false)
            throw new IllegalStateException("Unable to initialize GLFW");
    }

    private void loop() {
        while (renderer.windowIsOpen()) {
            renderer.beginFrame();
            renderer.endFrame();

            glfwPollEvents();
        }
    }

    private void shutdown() {
        renderer.destroyWindow();
        shutdownGLFW();
    }

    private void shutdownGLFW() {
        glfwTerminate();
        GLFWErrorCallback usedErrorCallback = glfwSetErrorCallback(null);
        if (usedErrorCallback != null)
            usedErrorCallback.free();
    }

    public static void main(String[] args) {
        new Game().run();
    }
}
