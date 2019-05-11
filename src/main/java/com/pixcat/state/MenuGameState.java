package com.pixcat.state;

import com.pixcat.gameplay.World;
import com.pixcat.graphics.Renderer;
import com.pixcat.core.InputBuffer;

import static org.lwjgl.glfw.GLFW.GLFW_KEY_ESCAPE;

public class MenuGameState implements GameState {
    private World world;

    public MenuGameState(World world) {
        this.world = world;
    }

    @Override
    public void draw(Renderer renderer) {
        //TODO
    }

    @Override
    public GameState handleInput(InputBuffer input) {
        if (input.isKeyboardKeyDown(GLFW_KEY_ESCAPE))
            return null;
        //TODO
        return this;
    }
}
