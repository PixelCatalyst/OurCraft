package com.pixcat.gameplay;

import com.pixcat.graphics.Renderer;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import static org.lwjgl.glfw.GLFW.glfwInit;

public class AchievementDrawTest {
    private Achievements testAchievements;
    private Renderer placeholderRenderer;

    @Before
    public void setUp() {
        if (glfwInit() == false)
            throw new RuntimeException("Unable to initialize GLFW");
        placeholderRenderer = new Renderer(3, 3);
        placeholderRenderer.createWindow(100, 100, "window");
        placeholderRenderer.initAssets();

        testAchievements = new Achievements();
    }

    //For some reason this test only works in isolation, so it's in separate class
    @Test
    public void testDrawNotEmpty() {
        Metrics status = new Metrics(100.0, 100, 100.0f);
        status.addDugBlock((byte) 1); //to make non initial

        testAchievements.onUpdate(status);

        placeholderRenderer.beginFrame();
        placeholderRenderer.setOrthographic();
        testAchievements.draw(placeholderRenderer);
        placeholderRenderer.endFrame();
    }

    @After
    public void tearDown() {
        placeholderRenderer.destroyWindow();
        placeholderRenderer.cleanup();
    }
}
