package com.pixcat.core;

import com.pixcat.graphics.Renderer;
import org.lwjgl.glfw.*;

import static org.lwjgl.glfw.GLFW.*;

public class Game {
    private Renderer renderer;
    private InputBuffer input;

    private void run() {
        init();
        loop();
        shutdown();
    }

    private void init() {
        initGLFW();
        renderer = new Renderer(300, 300, "OurCraft");
        input = new InputBuffer(renderer.getWindowHandle());
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

            input.update();
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
