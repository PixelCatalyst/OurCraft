package com.pixcat.state;

import com.pixcat.core.FileManager;
import com.pixcat.core.MouseAction;
import com.pixcat.gameplay.World;
import com.pixcat.graphics.*;
import com.pixcat.core.InputBuffer;

import static org.lwjgl.glfw.GLFW.*;

public class MenuGameState implements GameState {
    private World world;
    private GUIFactory gui;
    private FileManager fm;

    private Texture backgroundTex;
    private Texture startBtnTex;
    private Texture exitBtnTex;

    private Button playButton;
    private Button exitButton;

    public MenuGameState(World world) {
        this.world = world;
        this.gui = GUIFactory.getInstance();
        this.fm = FileManager.getInstance();

        backgroundTex = fm.loadTexture("menu_background.png");
        startBtnTex = fm.loadTexture("start_button.png");
        exitBtnTex = fm.loadTexture("exit_button.png");
    }

    @Override
    public void draw(Renderer renderer) {
        renderer.setBackgroundColor(0.31f, 0.55f, 0.82f);
        renderer.setOrthographic();

        int windowWidth = renderer.getWindowWidth();
        int windowHeight = renderer.getWindowHeight();
        StaticImage bg = gui.makeImage(windowWidth, windowHeight, backgroundTex);
        renderer.draw(bg);

        int btnWidth = 512;
        int btnHeight = 48;
        playButton = gui.makeButton(btnWidth, btnHeight, startBtnTex);
        exitButton = gui.makeButton(btnWidth, btnHeight, exitBtnTex);
        playButton.setPosition(windowWidth/2 - btnWidth/2, windowHeight/2 - btnHeight/2 - btnHeight);
        exitButton.setPosition(windowWidth/2 - btnWidth/2, windowHeight/2 - btnHeight/2 + btnHeight);
        renderer.draw(playButton);
        renderer.draw(exitButton);
    }

    @Override
    public GameState handleInput(InputBuffer input) {
        if (input.isKeyboardKeyDown(GLFW_KEY_ESCAPE))
            return null; // exit game

        if (input.isKeyboardKeyDown(GLFW_KEY_H))
            return new PlayGameState(this.world);

        MouseAction mouseAction = input.getMouseAction();
        if (playButton.wasTouched(mouseAction))
            return new PlayGameState(world);
        if (exitButton.wasTouched(mouseAction))
            return null;

        return this;
    }

    public void onEnter() {

    }
}
