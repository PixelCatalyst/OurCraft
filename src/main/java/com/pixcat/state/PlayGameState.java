package com.pixcat.state;

import com.pixcat.gameplay.Achievements;
import com.pixcat.gameplay.World;
import com.pixcat.graphics.Renderer;
import com.pixcat.core.InputBuffer;
import org.joml.Vector3f;

import static org.lwjgl.glfw.GLFW.*;

public class PlayGameState implements GameState {
    private World world;
    private Achievements achievements;

    public PlayGameState(World world) {
        this.world = world;
        achievements = new Achievements();

        //TODO achievement initial state
        world.addObserver(achievements);
    }

    @Override
    public void draw(Renderer renderer) {
        Vector3f skyColor = world.getSkyColor();
        renderer.setBackgroundColor(skyColor.x, skyColor.y, skyColor.z);
        //TODO
    }

    @Override
    public GameState handleInput(InputBuffer input) {
        //TODO

        if (input.isKeyboardKeyDown(GLFW_KEY_ESCAPE))
            return new MenuGameState(world); // back to main menu
        if (input.isKeyboardKeyDown(GLFW_KEY_Q))
            return null; // exit game


        return this;
    }

    @Override
    public void update(double elapsedTime) {
        world.addGameTime(elapsedTime);
    }
}
