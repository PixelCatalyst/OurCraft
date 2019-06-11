package com.pixcat.graphics.gui;

import com.pixcat.graphics.Texture;
import com.pixcat.graphics.Window;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import static junit.framework.TestCase.assertNotNull;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.lwjgl.glfw.GLFW.glfwInit;

public class GUIFactoryTest {
    private Window placeholderWindow;
    private GUIFactory testGUI;

    @Before
    public void setUp() {
        if (glfwInit() == false)
            throw new RuntimeException("Unable to initialize GLFW");
        placeholderWindow = new Window(200, 200, "window");
        placeholderWindow.bindAsCurrent();

        testGUI = GUIFactory.getInstance();
    }

    @Test
    public void testMakeImage() {
        StaticImage image = testGUI.makeImage(null, 10, 12);

        assertNotNull(image);
        assertEquals(10, image.getWidth());
        assertEquals(12, image.getHeight());
        assertNull(image.getTexture());
    }

    @Test
    public void testMakeButton() {
        Button button = testGUI.makeButton(null, 10, 12);

        assertNotNull(button);
        assertEquals(10, button.getWidth());
        assertEquals(12, button.getHeight());
        assertNull(button.getTexture());
    }

    @Test(expected = NullPointerException.class)
    public void testInvalidMakeImage() {
        testGUI.makeImage(null, null, null);
    }

    @Test(expected = NullPointerException.class)
    public void testInvalidMakeButton() {
        testGUI.makeButton(null, null, null);
    }

    @Test
    public void testCleanup() {
        int refCountPrev = testGUI.modelMesh.getReferenceCount();
        testGUI.cleanup();

        assertEquals(1, refCountPrev - testGUI.modelMesh.getReferenceCount());
    }

    @Test
    public void testGetInstance() {
        final GUIFactory firstInstance = GUIFactory.getInstance();
        final GUIFactory secondInstance = GUIFactory.getInstance();
        final GUIFactory thirdInstance = GUIFactory.getInstance();

        assertNotNull(testGUI);
        assertNotNull(firstInstance);
        assertNotNull(secondInstance);
        assertNotNull(thirdInstance);
        assertEquals(testGUI, firstInstance);
        assertEquals(testGUI, secondInstance);
        assertEquals(testGUI, thirdInstance);
        assertEquals(firstInstance, secondInstance);
        assertEquals(firstInstance, thirdInstance);
        assertEquals(secondInstance, thirdInstance);
    }

    @After
    public void tearDown() {
        placeholderWindow.destroy();

        testGUI.cleanup();
    }
}
