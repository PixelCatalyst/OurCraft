package com.pixcat.graphics.gui;

import com.pixcat.core.InputBuffer;
import com.pixcat.core.MouseAction;
import com.pixcat.graphics.GraphicBatch;
import com.pixcat.graphics.Renderer;
import com.pixcat.graphics.Texture;

import java.util.HashMap;

public class Menu {
    private GraphicBatch batch;
    private HashMap<String, Integer> buttonIndex;

    private int viewportWidth;
    private int viewportHeight;

    private int spacing;
    private int offsetTop;

    private MouseAction mouseAction;

    public int getSpacing() {
        return spacing;
    }

    public Menu() {
        batch = new GraphicBatch();
        buttonIndex = new HashMap<>();
    }

    public int getViewportWidth() {
        return viewportWidth;
    }

    public int getViewportHeight() {
        return viewportHeight;
    }

    public GraphicBatch getBatch() {
        return batch;
    }

    //changed to public to make testing easier, might need to change back if not working properly
    public Button getButtonByName(String name) {
        int buttonNo = buttonIndex.get(name);
        return (Button) batch.getObject(buttonNo);
    }

    public Menu setSpacing(int spacing) {
        this.spacing = Math.max(spacing, 0);
        return this;
    }

    public Menu addButton(String name, Button button) {
        offsetTop += button.getHeight() + spacing;
        buttonIndex.put(name, batch.size());
        batch.addObject(button);
        return this;
    }

    public Menu createButton(String name, Texture texture) {
        GUIFactory gui = GUIFactory.getInstance();
        Button button = gui.makeButton(texture, null, null);
        button.setPosition(0, offsetTop);
        return addButton(name, button);
    }

    public Menu viewport(int width, int height) {
        viewportWidth = width;
        viewportHeight = height;
        return this;
    }

    public Menu setPositionRel(Float relX, Float relY) {
        float relativeX = (relX == null ? (int) batch.getPosition().x : relX);
        float relativeY = (relY == null ? (int) batch.getPosition().y : relY);
        int left = (int) (relativeX * viewportWidth);
        int top = (int) (relativeY * viewportHeight);
        batch.setPosition(left, top, 0.0f);
        return this;
    }

    public Menu centerAll() {
        batch.beginIteration();
        while (batch.hasNext()) {
            Button button = (Button) batch.getObject();
            button
                    .viewport(viewportWidth, viewportHeight)
                    .setPositionRel(0.5f, null)
                    .selfCenterX();

            batch.next();
        }
        return this;
    }


    public void updateInput(InputBuffer input) {
        mouseAction = input.getMouseAction();
        mouseAction.translateCoords(batch.getPosition());
    }

    public boolean buttonWasClicked(String buttonName) {
        return getButtonByName(buttonName).wasClicked(mouseAction, MouseAction.Button.LEFT);
    }

    public void draw(Renderer renderer) {
        renderer.draw(batch);
    }

    public void cleanup() {
        batch.cleanup();
    }
}
