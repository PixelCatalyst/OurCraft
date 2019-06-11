package com.pixcat.state;

import com.pixcat.core.InputBuffer;
import com.pixcat.gameplay.Achievements;
import com.pixcat.gameplay.World;
import com.pixcat.graphics.Renderer;
import org.joml.Vector2f;
import org.joml.Vector3f;

import static org.lwjgl.glfw.GLFW.GLFW_KEY_ESCAPE;

public class PlayGameState implements GameState {
    private World world;
    private Achievements achievements;
    private boolean isEntering;
    private InputBuffer exitingInput;

    private float lastTimeStep;
    private Vector2f lastMousePosition;

    public PlayGameState(World world) {
        this.world = world;
        achievements = new Achievements();

        world.addObserver(achievements);
        world.notifyAllObservers();
    }

    @Override
    public void draw(Renderer renderer) {
        Vector3f skyColor = world.getSkyColor();
        renderer.setBackgroundColor(skyColor.x, skyColor.y, skyColor.z);

        renderer.setPerspective(world.getPlayerCamera());
        world.drawChunks(renderer);

        renderer.setOrthographic();
        world.drawStatusBar(renderer);

        achievements.draw(renderer);
    }

    @Override
    public GameState handleInput(InputBuffer input) {
        if (isEntering) {
            isEntering = false;
            input.setMouseLocked();
        }

        if (lastMousePosition == null) {
            lastMousePosition = new Vector2f((float) input.getMouseX(), (float) input.getMouseY());
        } else {
            Vector2f currentMousePosition = new Vector2f((float) input.getMouseX(), (float) input.getMouseY());
            lastMousePosition.sub(currentMousePosition);
            world.rotatePlayer(-lastMousePosition.x, -lastMousePosition.y, lastTimeStep);
            lastMousePosition = currentMousePosition;
        }
        world.movePlayer(input, lastTimeStep);
        world.updateBlockCursor(input.getMouseAction(), input.getScrollOffset());
        exitingInput = input;

        if (input.isKeyboardKeyDown(GLFW_KEY_ESCAPE))
            return new PauseGameState(world, this);
        return this;
    }

    @Override
    public void update(double elapsedTime) {
        achievements.updateTimeToLive(elapsedTime);
        world.addGameTime(elapsedTime);
        world.updateChunks();
        world.notifyAllObservers();
        lastTimeStep = (float) elapsedTime;
    }

    @Override
    public void onEnter(Renderer renderer) {
        isEntering = true;
    }

    @Override
    public void onExit(Renderer renderer) {
        if (exitingInput != null)
            exitingInput.setMouseUnlocked();
    }
}
