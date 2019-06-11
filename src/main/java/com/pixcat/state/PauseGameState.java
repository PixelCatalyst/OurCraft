package com.pixcat.state;

import com.pixcat.core.FileManager;
import com.pixcat.core.InputBuffer;
import com.pixcat.gameplay.World;
import com.pixcat.graphics.Renderer;
import com.pixcat.graphics.gui.GUIFactory;
import com.pixcat.graphics.gui.StaticImage;

import static org.lwjgl.glfw.GLFW.GLFW_KEY_ESCAPE;
import static org.lwjgl.glfw.GLFW.GLFW_KEY_S;

public class PauseGameState implements GameState {
    private World world;
    private PlayGameState pausedGame;

    private StaticImage infoScreen;
    private int initialWidth;
    private int initialHeight;

    public PauseGameState(World world, PlayGameState pausedGame) {
        this.world = world;
        this.pausedGame = pausedGame;
    }

    @Override
    public void draw(Renderer renderer) {
        pausedGame.draw(renderer);

        renderer.setOrthographic();
        int windowWidth = renderer.getWindowWidth();
        int windowHeight = renderer.getWindowHeight();
        float scale = Math.min((float) windowWidth / initialWidth, (float) windowHeight / initialHeight);
        infoScreen.setScale(scale);
        infoScreen.viewport(windowWidth, windowHeight);
        infoScreen.setPositionRel(0.5f, 0.5f);
        infoScreen.selfCenter();
        renderer.draw(infoScreen);
    }

    @Override
    public GameState handleInput(InputBuffer input) {
        if (input.isKeyboardKeyDown(GLFW_KEY_ESCAPE)) {
            FileManager.getInstance().clearSaves();
            return new MenuGameState();
        }
        else if (input.isKeyboardKeyDown(GLFW_KEY_S)) {
            world.save();
            return new MenuGameState();
        } else if (input.isAnyKeyboardKeyDown())
            return pausedGame;

        return this;
    }

    @Override
    public void onEnter(Renderer renderer) {
        GUIFactory gui = GUIFactory.getInstance();
        FileManager fm = FileManager.getInstance();
        infoScreen = gui.makeImage(fm.loadTexture("pause.png"), null, null);
        initialWidth = infoScreen.getWidth() * 2;
        initialHeight = infoScreen.getHeight() * 2;
    }

    @Override
    public void onExit(Renderer renderer) {
        infoScreen.cleanup();
        world.cleanup();
    }
}
