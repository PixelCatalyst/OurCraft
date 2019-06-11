package com.pixcat.gameplay;

import com.pixcat.core.FileManager;
import com.pixcat.core.MouseAction;
import com.pixcat.graphics.Renderer;
import com.pixcat.voxel.Chunk;
import org.joml.Vector3f;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;

import static org.junit.Assert.assertEquals;
import static org.lwjgl.glfw.GLFW.glfwInit;

public class WorldSecondTest {
    private World testWorld;
    private Renderer renderer;

    @Before
    public void setUp() {
        if (glfwInit() == false)
            throw new RuntimeException("Unable to initialize GLFW");
        renderer = new Renderer(3, 3);
        renderer.createWindow(100, 100, "window");
        renderer.initAssets();

        testWorld = new World();
    }

    @Test
    public void testRemoveObserver() {
        Achievements achievements = new Achievements();

        testWorld.addObserver(achievements);
        testWorld.removeObserver(achievements);

        assertEquals(0, testWorld.observers.size());
    }

    @Test
    public void testSaveRestore() {
        FileManager.getInstance().clearSaves();
        testWorld.beginGeneration("seed");
        testWorld.save();
        testWorld.cleanup();
        testWorld.restore(FileManager.getInstance().loadWorldInfo());

        ArrayList<Chunk> chunks = new ArrayList<>();
        testWorld.voxels.getAll(chunks);

        assertEquals(5 * 5 * 8, chunks.size()); //5 should be the worlds diameter

        FileManager.getInstance().clearSaves();
    }

    @Test
    public void testBeginGeneration() {
        testWorld.beginGeneration("seed");

        ArrayList<Chunk> chunks = new ArrayList<>();
        testWorld.voxels.getAll(chunks);

        assertEquals(5 * 5 * 8, chunks.size()); //5 should be the worlds diameter
    }

    @Test(expected = NullPointerException.class)
    public void testMovePlayer() {
        testWorld.movePlayer(null, 0.1f);
    }

    @Test
    public void testAddGameTime() {
        testWorld.addGameTime(19.0);

        assertEquals(19.0, testWorld.playerMetrics.getSecondsInGame(), Double.MIN_NORMAL);
    }

    @Test
    public void testUpdateBlockCursor() {
        testWorld.beginGeneration("seed");

        testWorld.updateBlockCursor(
                new MouseAction(MouseAction.Button.LEFT,
                        MouseAction.Event.PRESS, 0, 0), 1.0);
        testWorld.updateBlockCursor(
                new MouseAction(MouseAction.Button.RIGHT,
                        MouseAction.Event.PRESS, 0, 0), -1.0);
    }

    @Test
    public void testDrawStatusBar() {
        renderer.beginFrame();
        renderer.setPerspective(testWorld.getPlayerCamera());
        testWorld.drawStatusBar(renderer); //Shouldn't throw anything. Proper testing during system tests
        renderer.endFrame();
    }

    @Test
    public void testCleanup() {
        testWorld.cleanup();

        assertEquals(0, testWorld.voxels.getTypeCount());
    }

    @After
    public void tearDown() {
        testWorld.cleanup();
        renderer.destroyWindow();
        renderer.cleanup();
    }
}