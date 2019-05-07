package com.pixcat.state;

import com.pixcat.gameplay.World;
import com.pixcat.graphics.Renderer;
import com.pixcat.core.InputBuffer;

public class StartGameState implements GameState {
    private World world;

    public StartGameState(World world) {
        this.world = world;
    }

    @Override
    public void draw(Renderer renderer) {
        //TODO
    }

    @Override
    public GameState handleInput(InputBuffer input) {
        //TODO
        return this;
    }
}
