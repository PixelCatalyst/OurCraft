package com.pixcat.graphics;

import com.pixcat.gameplay.Camera;
import com.pixcat.mesh.Mesh;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;
import static org.lwjgl.glfw.GLFW.glfwGetCurrentContext;
import static org.lwjgl.glfw.GLFW.glfwInit;
import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.opengl.GL20.GL_CURRENT_PROGRAM;

public class RendererTest {
    private Renderer testRenderer;

    //Actual drawing can only be tested visually/ by a graphics debugger, so unit tests(of draw/frame methods) can
    //only check exception throwing, color settings and similar stuff

    @Before
    public void setUp() {
        testRenderer = new Renderer(3, 3);
    }

    @Test
    //Detailed window creation tests in WindowTest
    public void testCreateWindow() {
        if (glfwInit() == false)
            throw new RuntimeException("Unable to initialize GLFW");
        testRenderer.createWindow(100, 100, "create window test");

        assertNotEquals(0, testRenderer.getWindowHandle());
        assertEquals(testRenderer.getWindowHandle(), glfwGetCurrentContext());
    }

    @Test(expected = IllegalStateException.class)
    public void testInitAssetsNullWindow() {
        testRenderer.initAssets();
    }

    @Test
    //Shouldn't throw anything, proper init of sub-resources checked in corresponding test classes
    public void testInitAssetsNotNullWindow() {
        if (glfwInit() == false)
            throw new RuntimeException("Unable to initialize GLFW");
        testRenderer.createWindow(100, 100, "create window test");

        testRenderer.initAssets();

        assertTrue(glIsEnabled(GL_CULL_FACE)); //Also, back-face culling should be enable after proper init
    }

    @Test
    public void testGetWindowWidth() {
        if (glfwInit() == false)
            throw new RuntimeException("Unable to initialize GLFW");
        testRenderer.createWindow(200, 300, "create window test");

        assertEquals(200, testRenderer.getWindowWidth());
    }

    @Test
    public void testGetWindowHeight() {
        if (glfwInit() == false)
            throw new RuntimeException("Unable to initialize GLFW");
        testRenderer.createWindow(100, 200, "create window test");

        assertEquals(200, testRenderer.getWindowHeight());
    }

    @Test
    public void testWindowIsOpen() {
        assertFalse(testRenderer.windowIsOpen()); //null window

        if (glfwInit() == false)
            throw new RuntimeException("Unable to initialize GLFW");
        testRenderer.createWindow(100, 200, "create window test");

        assertTrue(testRenderer.windowIsOpen());
    }

    @Test(expected = NullPointerException.class)
    public void testCenterNullWindow() {
        testRenderer.centerWindow();
    }

    @Test
    //Shouldn't throw anything, actual centering verified in WindowTest
    public void testCenterWindow() {
        if (glfwInit() == false)
            throw new RuntimeException("Unable to initialize GLFW");
        testRenderer.createWindow(100, 200, "create window test");

        testRenderer.centerWindow();
    }

    @Test(expected = NullPointerException.class)
    public void testDestroyNullWindow() {
        testRenderer.destroyWindow();
    }

    @Test
    public void testDestroyWindow() {
        if (glfwInit() == false)
            throw new RuntimeException("Unable to initialize GLFW");
        testRenderer.createWindow(100, 200, "create window test");

        assertNotEquals(0, testRenderer.getWindowHandle());

        testRenderer.destroyWindow();

        assertEquals(0, testRenderer.getWindowHandle());
    }

    @Test
    public void testBeginFrame() {
        if (glfwInit() == false)
            throw new RuntimeException("Unable to initialize GLFW");
        testRenderer.createWindow(100, 200, "create window test");
        testRenderer.initAssets();

        //Shouldn't throw anything
        testRenderer.beginFrame();
    }

    @Test
    public void testSetBackgroundColor() {
        if (glfwInit() == false)
            throw new RuntimeException("Unable to initialize GLFW");
        testRenderer.createWindow(100, 200, "create window test");
        testRenderer.setBackgroundColor(0.1f, 0.2f, 0.3f);
        //Should set OpenGL clear color

        float[] actualColors = new float[4];
        glGetFloatv(GL_COLOR_CLEAR_VALUE, actualColors);

        assertEquals(0.1f, actualColors[0], Float.MIN_NORMAL);
        assertEquals(0.2f, actualColors[1], Float.MIN_NORMAL);
        assertEquals(0.3f, actualColors[2], Float.MIN_NORMAL);
    }

    @Test
    public void testSetPerspective() {
        if (glfwInit() == false)
            throw new RuntimeException("Unable to initialize GLFW");
        testRenderer.createWindow(100, 200, "create window test");
        testRenderer.initAssets();
        Camera camera = new Camera(0.0f, 0.0f, 0.0f);

        //Shouldn't throw anything
        testRenderer.setPerspective(camera);

        assertTrue(glIsEnabled(GL_DEPTH_TEST));
    }

    @Test
    public void testSetOrthographic() {
        if (glfwInit() == false)
            throw new RuntimeException("Unable to initialize GLFW");
        testRenderer.createWindow(100, 200, "create window test");
        testRenderer.initAssets();

        //Shouldn't throw anything
        testRenderer.setOrthographic();

        assertFalse(glIsEnabled(GL_DEPTH_TEST));
    }


    @Test(expected = NullPointerException.class)
    public void testDrawObject() {
        GraphicObject object = null;
        testRenderer.draw(object);
    }

    @Test(expected = NullPointerException.class)
    public void testDrawBatch() {
        GraphicBatch batch = null;
        testRenderer.draw(batch);
    }

    @Test(expected = NullPointerException.class)
    public void testDrawWireframe() {
        GraphicObject object = null;
        testRenderer.drawWireframe(object);
    }

    @Test
    //Tests frame drawing process for lack of exceptions and proper binding
    //-> actual drawing verified during system tests
    public void testFrame() {
        if (glfwInit() == false)
            throw new RuntimeException("Unable to initialize GLFW");
        testRenderer.createWindow(100, 200, "create window test");
        testRenderer.initAssets();
        testRenderer.setBackgroundColor(0.0f, 0.0f, 0.0f);
        int[] idBuffer = new int[1];

        testRenderer.beginFrame();
        glGetIntegerv(GL_CURRENT_PROGRAM, idBuffer);

        assertNotEquals(0, idBuffer[0]);

        Mesh placeholderMesh = new Mesh(new float[6], new float[4], new int[10]);
        testRenderer.drawMesh(placeholderMesh);

        testRenderer.endFrame();
        glGetIntegerv(GL_CURRENT_PROGRAM, idBuffer);

        assertEquals(0, idBuffer[0]);
    }

    @Test
    public void testCleanup() {
        if (glfwInit() == false)
            throw new RuntimeException("Unable to initialize GLFW");
        testRenderer.createWindow(100, 200, "create window test");
        testRenderer.initAssets();
        int[] idBuffer = new int[1];

        testRenderer.cleanup();
        testRenderer.beginFrame();
        glGetIntegerv(GL_CURRENT_PROGRAM, idBuffer);

        assertEquals(0, idBuffer[0]);
    }
}
