package com.pixcat.graphics.gui;

import com.pixcat.core.FileManager;
import com.pixcat.graphics.Texture;
import com.pixcat.graphics.Window;
import com.pixcat.mesh.Mesh;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.lwjgl.glfw.GLFW.glfwInit;

public class StaticImageTest {


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
    public void testValidCreation(){
        GUIFactory gui = GUIFactory.getInstance();
        StaticImage imageTest = gui.makeImage(texture, 100, 100);
        assertEquals(texture, imageTest.getTexture());
        assertEquals(100, imageTest.getWidth());
        assertEquals(100, imageTest.getHeight());
    }

    @Test
    public void testSetPosition(){
        GUIFactory gui = GUIFactory.getInstance();
        StaticImage image = gui.makeImage(texture, 100, 100);
        image.setPosition(50,50);
        assertEquals(50, image.getPosition().x, 0.0);
        assertEquals(50, image.getPosition().y, 0.0);
    }

    @Test
    public void testSetSize(){
        GUIFactory gui = GUIFactory.getInstance();
        StaticImage image = gui.makeImage(texture, 100, 100);
        image.setSize(200, 200);
        assertEquals(200, image.getWidth());
        assertEquals(200, image.getHeight());
    }

    @Test
    public void viewportTest(){
        GUIFactory gui = GUIFactory.getInstance();
        StaticImage image = gui.makeImage(texture, 100, 100);
        image.viewport(100, 150);
        assertEquals(100, image.getViewportWidth());
        assertEquals(150, image.getViewportHeight());
    }

    @Test
    public void testSetPositionRelative(){
        GUIFactory gui = GUIFactory.getInstance();
        StaticImage image = gui.makeImage(texture, 100, 100);
        image.viewport(400, 300);
        image.setPosition(100, 100);
        image.setPositionRel((float) 0.5, (float) 0.5);
        assertEquals(400 * 0.5, image.getPosition().x, 0.0);
        assertEquals(300 * 0.5, image.getPosition().y, 0.0);
    }

    @Test
    public void testSetPositionRelativeWithNoViewport(){
        GUIFactory gui = GUIFactory.getInstance();
        StaticImage image = gui.makeImage(texture, 100, 100);
        image.setPosition(50, 100);
        image.setPositionRel((float) 0.5, (float) 0.5);
        assertEquals(0, image.getPosition().x, 0.0);
        assertEquals(0, image.getPosition().y, 0.0);
    }

    @Test
    public void testMove(){
        GUIFactory gui = GUIFactory.getInstance();
        StaticImage image = gui.makeImage(texture, 100, 100);
        image.setPosition(50, 100);
        image.move(150, 50);
        assertEquals(200, image.getPosition().x, 0.0);
        assertEquals(150, image.getPosition().y, 0.0);
    }

    @Test //testing all 3 center methods by a main one
    public void testSelfCenter(){
        GUIFactory gui = GUIFactory.getInstance();
        StaticImage image = gui.makeImage(texture, 100, 100);
        image.setPosition(25, 100);
        image.selfCenter();
        assertEquals(-25, image.getPosition().x, 0.0);
        assertEquals(50, image.getPosition().y, 0.0);
    }

    @After
    public void tearDown() {
        placeholderWindow.destroy();
    }

}
