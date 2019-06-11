package com.pixcat.gameplay;

import com.pixcat.graphics.Renderer;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.lwjgl.glfw.GLFW.glfwInit;

public class AchievementsTest {
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

    @Test
    public void testOnUpdateInitial() {
        Metrics status = new Metrics(21.0 * 60.0, 100, 100.0f); //every ach

        testAchievements.onUpdate(status);

        assertEquals(0, testAchievements.awardQueue.size());

        status.addSecondsInGame(1.0); //make non initial
        testAchievements.onUpdate(status);

        assertEquals(0, testAchievements.awardQueue.size());
    }

    @Test
    public void testOnUpdateNonInitial() {
        Metrics status = new Metrics(21.0 * 60.0, 100, 100.0f); //every ach
        status.addSecondsInGame(1.0); //make non initial

        assertEquals(0, testAchievements.awardQueue.size());

        testAchievements.onUpdate(status);

        assertEquals(3, testAchievements.awardQueue.size());
    }

    @Test
    public void testUpdateTimeToLive() {
        Metrics status = new Metrics(21.0 * 60.0, 100, 100.0f); //every ach
        status.addSecondsInGame(1.0); //make non initial

        assertEquals(0, testAchievements.awardQueue.size());

        testAchievements.onUpdate(status);
        testAchievements.updateTimeToLive(100.0);

        assertEquals(2, testAchievements.awardQueue.size());

        testAchievements.updateTimeToLive(100.0);

        assertEquals(1, testAchievements.awardQueue.size());

        testAchievements.updateTimeToLive(100.0);

        assertEquals(0, testAchievements.awardQueue.size());
    }

    //Draw test can only confirm that no exception are thrown
    //-> visual correctness can only be ensured through system tests
    @Test
    public void testDrawEmpty() {
        placeholderRenderer.beginFrame();
        testAchievements.draw(placeholderRenderer);
        placeholderRenderer.endFrame();
    }

    @After
    public void tearDown() {
        placeholderRenderer.destroyWindow();
        placeholderRenderer.cleanup();
    }
}
