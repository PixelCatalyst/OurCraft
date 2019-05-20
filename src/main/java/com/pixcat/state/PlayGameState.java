package com.pixcat.state;

import com.pixcat.gameplay.Achievements;
import com.pixcat.gameplay.Camera;
import com.pixcat.gameplay.World;
import com.pixcat.graphics.Renderer;
import com.pixcat.core.InputBuffer;
import org.joml.Vector2d;
import org.joml.Vector3f;

import static org.lwjgl.glfw.GLFW.*;
import static org.lwjgl.glfw.GLFW.GLFW_KEY_D;

public class PlayGameState implements GameState {
    private World world;
    private Achievements achievements;
    boolean isEntering;
    InputBuffer exitingInput;

    private Camera testCam; //TODO temporary
    private float lastT;
    private Vector2d lastMouse;

    public PlayGameState(World world) {
        this.world = world;
        achievements = new Achievements();
        isEntering = false;

        //TODO achievement initial state
        world.addObserver(achievements);
    }

    @Override
    public void draw(Renderer renderer) {
        Vector3f skyColor = world.getSkyColor();
        renderer.setBackgroundColor(skyColor.x, skyColor.y, skyColor.z);

        //TODO render world
        //renderer.setPerspective(world.getPlayerCamera());
        renderer.setPerspective(testCam);
        world.drawChunks(renderer);

        //TODO render player-HUD
        renderer.setOrthographic();
        world.drawStatusBar(renderer);

        //TODO render awarded achievements
    }

    @Override
    public GameState handleInput(InputBuffer input) {
        if (isEntering)
            input.setMouseLocked();

        if (lastMouse == null) {
            lastMouse = new Vector2d(input.getMouseX(), input.getMouseY());
        } else {
            Vector2d currMouse = new Vector2d(input.getMouseX(), input.getMouseY());
            double deltaX = lastMouse.x - currMouse.x;
            double deltaY = lastMouse.y - currMouse.y;
            lastMouse = currMouse;
            testCam.moveRotation((float) -deltaY, (float) -deltaX, 0);
        }

        //TODO temporary
        float speed = 4.5f;
        if (input.isKeyboardKeyDown(GLFW_KEY_W))
            testCam.movePosition(0, 0, -speed * lastT);
        if (input.isKeyboardKeyDown(GLFW_KEY_S))
            testCam.movePosition(0, 0, speed * lastT);

        if (input.isKeyboardKeyDown(GLFW_KEY_A))
            testCam.movePosition(-speed * lastT, 0, 0);
        if (input.isKeyboardKeyDown(GLFW_KEY_D))
            testCam.movePosition(speed * lastT, 0, 0);

        if (input.isKeyboardKeyDown(GLFW_KEY_R))
            testCam.movePosition(0, speed * lastT, 0);
        if (input.isKeyboardKeyDown(GLFW_KEY_F))
            testCam.movePosition(0, -speed * lastT, 0);
        exitingInput = input;

        return this;
    }

    @Override
    public void update(double elapsedTime) {
        world.addGameTime(elapsedTime);
        lastT = (float) elapsedTime;
    }

    @Override
    public void onEnter() {
        isEntering = true;

        //TODO temp, should be in StartState
        String testSeed = "ra";
        world.beginGeneration(testSeed);
        //
        testCam = world.getPlayerCamera();
    }

    @Override
    public void onExit() {
        if (exitingInput != null)
            exitingInput.setMouseUnlocked();
    }
}
