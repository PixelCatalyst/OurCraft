package com.pixcat.state;

import com.pixcat.gameplay.Achievements;
import com.pixcat.gameplay.Camera;
import com.pixcat.gameplay.World;
import com.pixcat.graphics.Renderer;
import com.pixcat.core.InputBuffer;
import org.joml.Vector3f;

import static org.lwjgl.glfw.GLFW.*;
import static org.lwjgl.glfw.GLFW.GLFW_KEY_D;

public class PlayGameState implements GameState {
    private World world;
    private Achievements achievements;

    private Camera testCam; //TODO temporary

    public PlayGameState(World world) {
        this.world = world;
        achievements = new Achievements();

        //TODO achievement initial state
        world.addObserver(achievements);

        testCam = new Camera();
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
        //TODO temporary
        float speed = 0.3f;
        if (input.isKeyboardKeyDown(GLFW_KEY_W))
            testCam.movePosition(0, 0, -speed);
        if (input.isKeyboardKeyDown(GLFW_KEY_S))
            testCam.movePosition(0, 0, speed);

        if (input.isKeyboardKeyDown(GLFW_KEY_A))
            testCam.movePosition(-speed, 0, 0);
        if (input.isKeyboardKeyDown(GLFW_KEY_D))
            testCam.movePosition(speed, 0, 0);

        if (input.isKeyboardKeyDown(GLFW_KEY_R))
            testCam.movePosition(0, speed, 0);
        if (input.isKeyboardKeyDown(GLFW_KEY_F))
            testCam.movePosition(0, -speed, 0);

        return this;
    }

    @Override
    public void update(double elapsedTime) {
        world.addGameTime(elapsedTime);
    }
}
