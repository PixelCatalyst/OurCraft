package com.pixcat.state;

import com.pixcat.gameplay.World;
import com.pixcat.graphics.Renderer;
import com.pixcat.core.InputBuffer;

import static org.lwjgl.glfw.GLFW.GLFW_KEY_ESCAPE;

public class MenuGameState implements GameState {
    private World world;

    //TODO Background
    //TODO Buttons
    //TODO Button handling, state changes etc...

    public MenuGameState(World world) {
        this.world = world;
    }

    @Override
    public void draw(Renderer renderer) {
        renderer.setBackgroundColor(0.31f, 0.55f, 0.82f);
        renderer.setOrthographic();
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
