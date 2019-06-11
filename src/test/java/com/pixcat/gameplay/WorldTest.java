package com.pixcat.gameplay;

import com.pixcat.core.FileManager;
import com.pixcat.core.InputBuffer;
import com.pixcat.core.MouseAction;
import com.pixcat.graphics.Renderer;
import com.pixcat.graphics.Window;
import com.pixcat.voxel.Chunk;
import org.joml.Vector3f;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.lwjgl.system.CallbackI;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;
import static org.lwjgl.glfw.GLFW.glfwInit;

public class WorldTest {
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
    public void testAddObserver() {
        assertEquals(0, testWorld.observers.size());

        testWorld.addObserver(null);

        assertEquals(1, testWorld.observers.size());
    }

    @Test(expected = NullPointerException.class)
    public void testNotifyAllObservers() {
        testWorld.notifyAllObservers();
        testWorld.addObserver(null);
        testWorld.notifyAllObservers();
    }

    @Test
    public void testRotatePlayer() {
        testWorld.rotatePlayer(0.0f, 0.0f, 10.0f);

        Vector3f rotation = testWorld.getPlayerCamera().getRotation();

        assertEquals(0.0f, rotation.x, Float.MIN_NORMAL);
        assertEquals(0.0f, rotation.y, Float.MIN_NORMAL);
    }

    @Test
    public void testUpdateChunks() {
        testWorld.beginGeneration("seed");
        testWorld.getPlayerCamera().movePosition(10.0f, 10.0f, 10.0f);
        testWorld.updateChunks();

        ArrayList<Chunk> chunks = new ArrayList<>();
        testWorld.voxels.getAll(chunks);

        assertEquals(5 * 5 * 8, chunks.size()); //5 should be the worlds diameter
    }

    @Test
    public void testGetSkyColor() {
        Vector3f expectedColor = new Vector3f(0.51f, 0.79f, 1.0f);
        Vector3f actualColor = testWorld.getSkyColor();

        assertEquals(expectedColor.x, actualColor.x, Float.MIN_NORMAL);
        assertEquals(expectedColor.y, actualColor.y, Float.MIN_NORMAL);
        assertEquals(expectedColor.z, actualColor.z, Float.MIN_NORMAL);
    }

    @Test
    public void testDrawChunks() {
        testWorld.beginGeneration("seed1");

        ArrayList<Chunk> chunks = new ArrayList<>();
        testWorld.voxels.getAll(chunks);

        assertEquals(5 * 5 * 8, chunks.size()); //5 should be the worlds diameter

        renderer.beginFrame();
        renderer.setPerspective(testWorld.getPlayerCamera());
        testWorld.drawChunks(renderer); //Shouldn't throw anything. Proper testing during system tests
        renderer.endFrame();
    }

    @After
    public void tearDown() {
        testWorld.cleanup();
        renderer.destroyWindow();
        renderer.cleanup();
    }
}
