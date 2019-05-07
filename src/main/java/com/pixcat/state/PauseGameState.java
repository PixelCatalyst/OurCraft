package com.pixcat.state;

import com.pixcat.gameplay.World;
import com.pixcat.graphics.Renderer;
import com.pixcat.core.InputBuffer;

public class PauseGameState implements GameState {
    private World world;
    private PlayGameState pausedGame;

    public PauseGameState(World world, PlayGameState pausedGame) {
        this.world = world;
        this.pausedGame = pausedGame;
    }

    @Override
    public void draw(Renderer renderer) {
        pausedGame.draw(renderer);
        //TODO overlay drawing
    }

    @Override
    public GameState handleInput(InputBuffer input) {
        //TODO
        return this;
    }
}
