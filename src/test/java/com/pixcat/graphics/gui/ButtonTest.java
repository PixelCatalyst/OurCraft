package com.pixcat.graphics.gui;

import com.pixcat.core.FileManager;
import com.pixcat.core.MouseAction;
import com.pixcat.graphics.Texture;
import com.pixcat.graphics.Window;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import static com.pixcat.core.MouseAction.Button.RIGHT;
import static com.pixcat.core.MouseAction.Event.RELEASE;
import static org.junit.Assert.*;
import static org.hamcrest.CoreMatchers.*;

import static com.pixcat.core.MouseAction.Button.LEFT;
import static com.pixcat.core.MouseAction.Event.PRESS;
import static org.junit.Assert.assertEquals;
import static org.lwjgl.glfw.GLFW.glfwInit;

public class ButtonTest {
    private Texture texture;
    private Window placeholderWindow;

    @Before
    public void setUp() {
        if (glfwInit() == false)
            throw new RuntimeException("Unable to initialize GLFW");
        placeholderWindow = new Window(200, 200, "placeholder");
        placeholderWindow.bindAsCurrent();
        texture = FileManager.getInstance().loadTexture("water.png");
    }
    @Test
    public void testValidCreation(){
        GUIFactory gui = GUIFactory.getInstance();
        Button button = gui.makeButton(texture, 100, 100);
        assertEquals(100, button.getWidth());
        assertEquals(100, button.getHeight());
    }

    @Test
    public void testWasTouched(){
        GUIFactory gui = GUIFactory.getInstance();
        Button button = gui.makeButton(texture, 100, 100);
        button.setPosition(0,0);
        MouseAction press = new MouseAction(LEFT, PRESS, 50,50);
        assertTrue(button.wasTouched(press));
        button.setPosition(300,300);
        assertFalse(button.wasTouched(press));
}

    @Test
    public void testWasClicked(){
        GUIFactory gui = GUIFactory.getInstance();
        Button button = gui.makeButton(texture, 100, 100);
        button.setPosition(0,0);
        MouseAction press = new MouseAction(LEFT, PRESS, 50,50);
        assertFalse(button.wasClicked(press)); //button pressed
        button.setPosition(300,300);
        assertFalse(button.wasClicked(press)); //missed button
        MouseAction click = new MouseAction(RIGHT, RELEASE, 50,50);
        button.setPosition(0,0);
        assertTrue(button.wasClicked(click)); // button released (clicked)
        button.setPosition(300,300);
        assertFalse(button.wasClicked(click)); // button missed
    }

    @After
    public void tearDown() {
        placeholderWindow.destroy();
    }


    //testing of buttons is specifically in the MenuTest class
}
