package com.pixcat.state;

import com.pixcat.gameplay.Achievements;
import com.pixcat.gameplay.World;
import com.pixcat.graphics.Renderer;
import com.pixcat.core.InputBuffer;
import org.joml.Vector3f;

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
        return this;
    }

    @Override
    public void update(double elapsedTime) {
        world.addGameTime(elapsedTime);
    }
}
