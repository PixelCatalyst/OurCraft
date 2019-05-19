package com.pixcat.state;

import com.pixcat.graphics.Renderer;
import com.pixcat.core.InputBuffer;

public interface GameState {

    void draw(Renderer renderer);

    GameState handleInput(InputBuffer input);

    default void update(double elapsedTime) {
        //Do nothing
    }

    default void onEnter(Renderer renderer) {
        //Do nothing
    }

    default void onExit(Renderer renderer) {
        //Do nothing
    }
}
