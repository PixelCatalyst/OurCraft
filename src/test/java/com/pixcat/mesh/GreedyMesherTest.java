package com.pixcat.mesh;

import com.pixcat.core.FileManager;
import com.pixcat.graphics.GraphicBatch;
import com.pixcat.graphics.Texture;
import com.pixcat.graphics.Window;
import com.pixcat.voxel.*;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import static junit.framework.TestCase.assertNotNull;
import static org.junit.Assert.assertEquals;
import static org.lwjgl.glfw.GLFW.glfwInit;

public class GreedyMesherTest {
    private Window placeholderWindow;
    private GreedyMesher testGreedy;

    @Before
    public void setUp() {
        if (glfwInit() == false)
            throw new RuntimeException("Unable to initialize GLFW");
        placeholderWindow = new Window(200, 200, "placeholder");
        placeholderWindow.bindAsCurrent();

        testGreedy = new GreedyMesher();
    }

    @Test
    public void testProcessChunk() {
        FileManager fm = FileManager.getInstance();
        Texture[] materials = new Texture[]{fm.loadTexture("dirt.png")};
        SpatialStructure voxels = new VirtualArray(3);
        Block testBlock = new SolidBlock((byte) 1, "test", materials[0]);
        voxels.addVoxelType(testBlock);

        Chunk testChunk = new ArrayChunk();
        int i = 0;
        for (int y = 0; y < Chunk.getSize(); ++y) {
            for (int x = 0; x < Chunk.getSize(); ++x) {
                for (int z = 0; z < Chunk.getSize(); ++z) {
                    if ((i % 2) == 0)
                        testChunk.setVoxelID(y, x, z, (byte) 1);
                    ++i;
                }
            }
        }
        voxels.putChunk(0, 0, 0, testChunk);
        voxels.putChunk(-1, 0, 0, testChunk);
        voxels.putChunk(1, 0, 0, testChunk);
        voxels.putChunk(0, -1, 0, testChunk);
        voxels.putChunk(0, 1, 0, testChunk);
        voxels.putChunk(0, 0, -1, testChunk);
        voxels.putChunk(0, 0, 1, testChunk);
        NeumannNeighborhood chunkWithNeighbors = new NeumannNeighborhood(voxels, new Coord3Int(0, 0, 0));

        GraphicBatch resultBatch = testGreedy.processChunk(chunkWithNeighbors, materials);

        assertNotNull(resultBatch);
        assertEquals(8 * 2, resultBatch.size());
    }

    @Test(expected = NullPointerException.class)
    public void testProcessNullChunk() {
        NeumannNeighborhood chunkWithNeighbors
                = new NeumannNeighborhood(new VirtualArray(3), new Coord3Int(0, 0, 0));

        testGreedy.processChunk(chunkWithNeighbors, null);
    }

    @After
    public void tearDown() {
        placeholderWindow.destroy();
    }
}
