package com.pixcat.state;

import com.pixcat.core.FileManager;
import com.pixcat.core.InputBuffer;
import com.pixcat.gameplay.World;
import com.pixcat.graphics.Renderer;
import com.pixcat.graphics.gui.GUIFactory;
import com.pixcat.graphics.gui.Menu;
import com.pixcat.graphics.gui.StaticImage;

import javax.swing.*;
import java.nio.charset.Charset;
import java.util.Random;

import static org.lwjgl.glfw.GLFW.GLFW_KEY_ESCAPE;

public class StartGameState implements GameState {
    private World world;
    private Menu startMenu;
    private StaticImage backgroundImg;

    private InputDialog dialog;

    public StartGameState(World world) {
        this.world = world;
        dialog = new InputDialog("World Seed");
    }

    @Override
    public void draw(Renderer renderer) {
        renderer.setBackgroundColor(0.24f, 0.17f, 0.13f);
        renderer.setOrthographic();

        int windowWidth = renderer.getWindowWidth();
        int windowHeight = renderer.getWindowHeight();

        backgroundImg.setSize(windowWidth, windowHeight);
        renderer.draw(backgroundImg);

        startMenu
                .viewport(windowWidth, windowHeight)
                .setPositionRel(0.0f, 0.2f)
                .centerAll();
        startMenu.draw(renderer);
    }

    @Override
    public GameState handleInput(InputBuffer input) {
        if (dialog.visible == false) {
            if (input.isKeyboardKeyDown(GLFW_KEY_ESCAPE))
                return new MenuGameState(world);

            startMenu.updateInput(input);
            if (startMenu.buttonWasClicked("seed"))
                dialog.start();
            if (startMenu.buttonWasClicked("play")) {
                String userWorldSeed = dialog.result;
                if (userWorldSeed == null)
                    userWorldSeed = generateRandomString();
                world.beginGeneration(userWorldSeed);
                return new PlayGameState(world);
            }
            if (startMenu.buttonWasClicked("return"))
                return new MenuGameState(world);
        } else
            input.flushMouseActions();

        return this;
    }

    class InputDialog extends Thread {
        private final String message;
        private String result;
        private boolean visible;

        InputDialog(String message) {
            this.message = message;
        }

        @Override
        public void start() {
            if (visible == false) {
                visible = true;
                new Thread(this, message).start();
            }
        }

        @Override
        public void run() {
            result = JOptionPane.showInputDialog(message);
            visible = false;
        }
    }

    private String generateRandomString() {
        Random random = new Random();
        final int minLength = 16;
        final int maxLength = 32;
        final int length = (random.nextInt(maxLength)) + minLength;
        byte[] bytesForString = new byte[length];
        random.nextBytes(bytesForString);
        return new String(bytesForString, Charset.forName("UTF-8"));
    }

    @Override
    public void onEnter(Renderer renderer) {
        FileManager fm = FileManager.getInstance();
        startMenu = new Menu()
                .setSpacing(30)
                .createButton("seed", fm.loadTexture("set_seed_button.png"))
                .setSpacing(80)
                .createButton("play", fm.loadTexture("play_button.png"))
                .createButton("return", fm.loadTexture("return_button.png"));

        int windowWidth = renderer.getWindowWidth();
        int windowHeight = renderer.getWindowHeight();
        GUIFactory gui = GUIFactory.getInstance();
        backgroundImg = gui.makeImage(fm.loadTexture("start_background.png"), windowWidth, windowHeight);
    }

    @Override
    public void onExit(Renderer renderer) {
        startMenu.cleanup();
        backgroundImg.cleanup();
    }
}
