package com.pixcat.core;

import com.pixcat.gameplay.World;
import com.pixcat.graphics.Renderer;
import com.pixcat.state.GameState;
import com.pixcat.state.MenuGameState;
import org.lwjgl.glfw.*;

import static org.lwjgl.glfw.GLFW.*;

public class Game {
    private Renderer renderer;
    private InputBuffer input;
    private World world;
    private Timer timer;
    private GameState currentState;

    private void run() {
        init();
        loop();
        shutdown();
    }

    private void init() {
        initGLFW();
        renderer = new Renderer(3, 3);
        renderer.createWindow(854, 480, "OurCraft");
        renderer.centerWindow();
        renderer.initAssets();
        input = new InputBuffer(renderer.getWindowHandle());
        world = new World();
        timer = new Timer();
        currentState = new MenuGameState(world);
        currentState.onEnter();
    }

    private void initGLFW() {
        GLFWErrorCallback.createPrint(System.err).set();
        if (glfwInit() == false)
            throw new IllegalStateException("Unable to initialize GLFW");
    }

    private void loop() {
        while (renderer.windowIsOpen()) {
            draw();
            handleInput();
            update();
        }
    }

    private void draw() {
        if (currentState != null) {
            renderer.beginFrame();
            currentState.draw(renderer);
            renderer.endFrame();
        }
    }

    private void handleInput() {
        input.update();
        GameState nextState = currentState.handleInput(input);
        if (nextState == null) {
            currentState.onExit();
            currentState = null;
            renderer.destroyWindow();
        } else if (nextState != currentState) {
            currentState.onExit();
            nextState.onEnter();
            currentState = nextState;
        }
    }

    private void update() {
        if (currentState != null)
            currentState.update(timer.getElapsedSeconds());
    }

    private void shutdown() {
        renderer.destroyWindow();
        renderer.cleanup();
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
