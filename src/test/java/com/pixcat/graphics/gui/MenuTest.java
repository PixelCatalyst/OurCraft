package com.pixcat.graphics.gui;

import com.pixcat.core.FileManager;
import com.pixcat.core.MouseAction;
import com.pixcat.graphics.Texture;
import com.pixcat.graphics.Window;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;
import static org.hamcrest.CoreMatchers.*;

import static org.junit.Assert.assertEquals;
import static org.lwjgl.glfw.GLFW.glfwInit;

public class MenuTest {

    private Texture texture;
    private Window placeholderWindow;

    @Before
    public void setUp() {
        if (glfwInit() == false)
            throw new RuntimeException("Unable to initialize GLFW");
        placeholderWindow = new Window(100, 100, "placeholder");
        placeholderWindow.bindAsCurrent();
        texture = FileManager.getInstance().loadTexture("water.png");
    }

    @Test
    public void testSetSpacing(){
        Menu menu = new Menu();
        menu.setSpacing(10);
        assertEquals(10, menu.getSpacing());
    }

    @Test
    public void testCreateButtonsAndGetButtonByName(){
        Menu menu = new Menu();
        GUIFactory gui = GUIFactory.getInstance();
        Button button1 = gui.makeButton(texture, 100, 100);
        Button button2 = gui.makeButton(texture, 50, 50);
        menu.addButton("name1", button1);
        menu.addButton("name2", button2);
        assertEquals(button1, menu.getButtonByName("name1") );
        assertEquals(button2, menu.getButtonByName("name2"));
        assertThat(button1, not(equalTo(menu.getButtonByName("name2"))));
    }

    @Test
    public void viewportTest(){
        Menu menu = new Menu();
        menu.viewport(150, 150);
        assertEquals(150, menu.getViewportWidth());
        assertEquals(150, menu.getViewportHeight());
        menu.viewport(-150, -150);
        assertEquals(-150, menu.getViewportWidth());
        assertEquals(-150, menu.getViewportHeight());
    }

    @Test
    public void testSetPositionRelative(){
        Menu menu = new Menu();
        menu.viewport(200, 300);
        menu.setPositionRel((float) 0.5, (float) 0.5);
        assertEquals(200 * 0.5, menu.getBatch().getPosition().x, 0.0);
        assertEquals(300 * 0.5, menu.getBatch().getPosition().y, 0.0);
    }
    @Test
    public void testSetPositionRelative1(){
        Menu menu = new Menu();
        menu.viewport(-200, 300);
        menu.setPositionRel((float) 0.5, (float) 0.5);
        assertEquals(-200 * 0.5, menu.getBatch().getPosition().x, 0.0);
        assertEquals(300 * 0.5, menu.getBatch().getPosition().y, 0.0);
    }

    @After
    public void tearDown() {
        placeholderWindow.destroy();
    }

}
