package com.pixcat.state;

import com.pixcat.gameplay.World;
import com.pixcat.graphics.Renderer;
import com.pixcat.core.InputBuffer;

public class PlayGameState implements GameState {
    private World world;

    public PlayGameState(World world) {
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

    @Override
    public void update(double elapsedTime) {
        //TODO
    }
}
