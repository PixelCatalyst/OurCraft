package com.pixcat.state;

import com.pixcat.core.FileManager;
import com.pixcat.core.InputBuffer;
import com.pixcat.core.MouseAction;
import com.pixcat.gameplay.World;
import com.pixcat.graphics.*;

import static org.lwjgl.glfw.GLFW.GLFW_KEY_ESCAPE;

public class MenuGameState implements GameState {
    private World world;
    private GUIFactory gui;

    private Texture backgroundTex;
    private Texture startBtnTex;
    private Texture exitBtnTex;

    private StaticImage backgroundImg;
    private Button playButton;
    private Button exitButton;

    public MenuGameState(World world) {
        this.world = world;
        this.gui = GUIFactory.getInstance();

        FileManager fm = FileManager.getInstance();
        backgroundTex = fm.loadTexture("menu_background.png");
        startBtnTex = fm.loadTexture("start_button.png");
        exitBtnTex = fm.loadTexture("exit_button.png");
    }

    @Override
    public void draw(Renderer renderer) {
        int windowWidth = renderer.getWindowWidth();
        int windowHeight = renderer.getWindowHeight();

        playButton
            .viewport(windowWidth, windowHeight)
            .setPositionRel(0.5f, 0.5f)
            .selfCenter()
            .move(0, -exitButton.getHeight());

        exitButton
            .viewport(windowWidth, windowHeight)
            .setPositionRel(0.5f, 0.5f)
            .selfCenter()
            .move(0, exitButton.getHeight());

        renderer.draw(backgroundImg);
        renderer.draw(playButton);
        renderer.draw(exitButton);
    }

    @Override
    public GameState handleInput(InputBuffer input) {
        if (input.isKeyboardKeyDown(GLFW_KEY_ESCAPE))
            return null; // exit game

        MouseAction mouseAction = input.getMouseAction();
        if (playButton.wasClicked(mouseAction, MouseAction.Button.LEFT))
            return new PlayGameState(world);
        if (exitButton.wasTouched(mouseAction))
            return null;

        return this;
    }

    public void onEnter(Renderer renderer) {
        renderer.setBackgroundColor(0.31f, 0.55f, 0.82f);
        renderer.setOrthographic();

        int windowWidth = renderer.getWindowWidth();
        int windowHeight = renderer.getWindowHeight();

        backgroundImg = gui.makeImage(backgroundTex, windowWidth, windowHeight);
        playButton = gui.makeButton(startBtnTex, null, null);
        exitButton = gui.makeButton(exitBtnTex, null, null);
    }
}
