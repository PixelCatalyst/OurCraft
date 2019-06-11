package com.pixcat.gameplay;

import com.pixcat.core.FileManager;
import com.pixcat.graphics.Renderer;
import com.pixcat.voxel.*;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import static org.lwjgl.glfw.GLFW.glfwInit;

public class BlockCursorBarTest {
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
    public void testDrawMaterialBar() {
        renderer.beginFrame();
        renderer.setOrthographic();
        testCursor.drawMaterialBar(renderer, placeholderVoxels); //Shouldn't throw anything
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
