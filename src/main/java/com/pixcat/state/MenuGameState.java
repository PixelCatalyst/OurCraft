package com.pixcat.state;

import com.pixcat.core.FileManager;
import com.pixcat.core.InputBuffer;
import com.pixcat.gameplay.World;
import com.pixcat.graphics.Renderer;
import com.pixcat.graphics.gui.GUIFactory;
import com.pixcat.graphics.gui.Menu;
import com.pixcat.graphics.gui.StaticImage;

import static org.lwjgl.glfw.GLFW.GLFW_KEY_ESCAPE;

public class MenuGameState implements GameState {
    private World world;
    private Menu mainMenu;
    private StaticImage backgroundImg;

    public MenuGameState(World world) {
        this.world = world;
    }

    @Override
    public void draw(Renderer renderer) {
        renderer.setBackgroundColor(0.31f, 0.55f, 0.82f);
        renderer.setOrthographic();

        int windowWidth = renderer.getWindowWidth();
        int windowHeight = renderer.getWindowHeight();

        backgroundImg.setSize(windowWidth, windowHeight);
        renderer.draw(backgroundImg);

        mainMenu
                .viewport(windowWidth, windowHeight)
                .setPositionRel(0.0f, 0.3f)
                .centerAll();
        mainMenu.draw(renderer);
    }

    @Override
    public GameState handleInput(InputBuffer input) {
        if (input.isKeyboardKeyDown(GLFW_KEY_ESCAPE))
            return null; // exit game

        mainMenu.updateInput(input);
        if (mainMenu.buttonWasClicked("start"))
            return new PlayGameState(world);
        if (mainMenu.buttonWasClicked("continue"))
            return new PlayGameState(world); //TODO load saved world
        if (mainMenu.buttonWasClicked("exit"))
            return null;

        return this;
    }

    @Override
    public void onEnter(Renderer renderer) {
        FileManager fm = FileManager.getInstance();
        mainMenu = new Menu()
                .setSpacing(15)
                .createButton("start", fm.loadTexture("start_button.png"))
                .createButton("continue", fm.loadTexture("continue_button.png"))
                .createButton("exit", fm.loadTexture("exit_button.png"));

        int windowWidth = renderer.getWindowWidth();
        int windowHeight = renderer.getWindowHeight();
        GUIFactory gui = GUIFactory.getInstance();
        backgroundImg = gui.makeImage(fm.loadTexture("menu_background.png"), windowWidth, windowHeight);
    }

    @Override
    public void onExit(Renderer renderer) {
        mainMenu.cleanup();
    }
}
