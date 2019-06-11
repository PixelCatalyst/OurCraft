package com.pixcat.gameplay;

import com.pixcat.core.FileManager;
import com.pixcat.graphics.Renderer;
import com.pixcat.voxel.*;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.lwjgl.glfw.GLFW.glfwInit;

public class BlockCursorTest {
    private BlockCursor testCursor;
    private SpatialStructure placeholderVoxels;
    private Renderer renderer;

    @Before
    public void setUp() {
        if (glfwInit() == false)
            throw new RuntimeException("Unable to initialize GLFW");
        renderer = new Renderer(3, 3);
        renderer.createWindow(100, 100, "window");
        renderer.initAssets();
        placeholderVoxels = new VirtualArray(4);

        Block testBlock1 = new SolidBlock((byte) 1, "test",
                FileManager.getInstance().loadTexture("dirt.png"));
        Block testBlock2 = new SolidBlock((byte) 2, "test",
                FileManager.getInstance().loadTexture("grass.png"));
        placeholderVoxels.addVoxelType(testBlock1);
        placeholderVoxels.addVoxelType(testBlock2);
        Chunk testChunk = new ArrayChunk();
        for (int i = 0; i < Chunk.getSize(); ++i) {
            for (int j = 0; j < Chunk.getSize(); ++j) {
                for (int k = 0; k < Chunk.getSize(); ++k) {
                    testChunk.setVoxelID(i, j, k, (byte) 1);
                }
            }
        }
        placeholderVoxels.putChunk(0, 0, 0, testChunk);
        placeholderVoxels.putChunk(0, 0, -1, new ArrayChunk());

        testCursor = new BlockCursor();
    }

    @Test
    public void testDeleteCurrentBlock() {
        Camera camera = new Camera(1.0f, 0.0f, 1.0f);
        testCursor.castRay(camera.getPosition(), camera.getDirection(), placeholderVoxels);
        byte deleted = testCursor.deleteCurrentBlock(placeholderVoxels);

        assertEquals(0, testCursor.getCurrentID());
        assertEquals(1, deleted);

        camera.moveRotation(20.0f, 10.0f, 0.0f);
        testCursor.castRay(camera.getPosition(), camera.getDirection(), placeholderVoxels);
        deleted = testCursor.deleteCurrentBlock(placeholderVoxels);

        assertEquals(0, testCursor.getCurrentID());
        assertEquals(0, deleted);

        Camera outboundCamera = new Camera(-0.1f, 1.0f, -1.0f);
        outboundCamera.moveRotation(20.0f, 10.0f, 0.0f);
        testCursor.castRay(outboundCamera.getPosition(), outboundCamera.getDirection(), placeholderVoxels);

        assertEquals(0, testCursor.getCurrentID());
    }

    @Test
    public void testPlaceNewBlock() {
        Camera camera = new Camera(1.0f, 0.0f, 1.0f);
        testCursor.castRay(camera.getPosition(), camera.getDirection(), placeholderVoxels);
        testCursor.placeNewBlock(placeholderVoxels);

        assertEquals(1, testCursor.getCurrentID());

        camera.moveRotation(20.0f, 10.0f, 0.0f);
        testCursor.castRay(camera.getPosition(), camera.getDirection(), placeholderVoxels);
        testCursor.placeNewBlock(placeholderVoxels);

        assertEquals(1, testCursor.getCurrentID());

        Camera outboundCamera = new Camera(-0.1f, 1.0f, -1.0f);
        outboundCamera.moveRotation(20.0f, 10.0f, 0.0f);
        testCursor.castRay(outboundCamera.getPosition(), outboundCamera.getDirection(), placeholderVoxels);

        assertEquals(0, testCursor.getCurrentID());
    }

    @Test
    public void testDraw() {
        renderer.beginFrame();
        renderer.setPerspective(new Camera(0, 0, 0));
        testCursor.draw(renderer); //Shouldn't throw anything
        renderer.endFrame();
    }

    @After
    public void tearDown() {
        renderer.destroyWindow();
        renderer.cleanup();
        placeholderVoxels.cleanup();
        testCursor.cleanup();
    }
}
