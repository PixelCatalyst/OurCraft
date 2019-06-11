package com.pixcat.voxel;

import com.pixcat.core.FileManager;
import com.pixcat.graphics.Window;
import com.pixcat.mesh.GreedyMesher;
import com.pixcat.mesh.Mesher;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;

import static org.junit.Assert.*;
import static org.lwjgl.glfw.GLFW.glfwInit;

public class VirtualArrayTest {
    private Window placeholderWindow;
    private Mesher testMesher;
    private VirtualArray testArray;

    @Before
    public void setUp() {
        if (glfwInit() == false)
            throw new RuntimeException("Unable to initialize GLFW");
        placeholderWindow = new Window(200, 200, "window");
        placeholderWindow.bindAsCurrent();
        testMesher = new GreedyMesher();

        testArray = new VirtualArray(3);
    }

    @Test
    public void testGetAll() {
        ArrayList<Chunk> visible = new ArrayList<>();

        testArray.getAll(visible);

        assertEquals(0, visible.size());

        Chunk[] testChunks = new ArrayChunk[7];
        for (int i = 0; i < testChunks.length; ++i)
            testChunks[i] = new ArrayChunk();
        for (int j = 0; j < testChunks.length; ++j)
            testArray.putChunk(0, j, j, testChunks[j]);
        testArray.getAll(visible);

        assertEquals(3, visible.size());
    }

    @Test
    public void testAddNullVoxelType() {
        assertEquals(0, testArray.getTypeCount());
        testArray.addVoxelType(null);
        testArray.addVoxelType(null);
        assertEquals(0, testArray.getTypeCount());
    }
    //addVoxelType() was tested extensively in other tests below

    @Test
    public void testGetVoxelFromID() {
        for (byte ID = 0; ID < 10; ++ID)
            assertNull(testArray.getVoxelFromID(ID));

        FileManager fm = FileManager.getInstance();
        Block testTypeOne = new SolidBlock((byte) 1, "test1", fm.loadTexture("dirt.png"));
        Block testTypeTwo = new SolidBlock((byte) 22, "test2", fm.loadTexture("dirt.png"));
        testArray.addVoxelType(testTypeOne);
        testArray.addVoxelType(testTypeTwo);

        assertEquals(testTypeOne, testArray.getVoxelFromID((byte) 1));
        assertEquals(testTypeTwo, testArray.getVoxelFromID((byte) 22));
    }

    @Test
    public void testGetTypeID() {
        byte actualID = testArray.getTypeID(12);

        assertEquals(0, actualID);

        FileManager fm = FileManager.getInstance();
        Block testTypeOne = new SolidBlock((byte) 1, "test1", fm.loadTexture("dirt.png"));
        Block testTypeTwo = new SolidBlock((byte) 22, "test2", fm.loadTexture("dirt.png"));
        testArray.addVoxelType(testTypeOne);
        testArray.addVoxelType(testTypeTwo);

        assertEquals(1, testArray.getTypeID(0));
        assertEquals(22, testArray.getTypeID(1));
        assertEquals(0, testArray.getTypeID(100));
    }

    @Test
    public void testGetTypeCount() {
        int typeCount = testArray.getTypeCount();

        assertEquals(0, typeCount);
    }

    @Test
    public void testUpdateBlocks() {
        FileManager fm = FileManager.getInstance();
        Block testTypeOne = new SolidBlock((byte) 1, "test1", fm.loadTexture("dirt.png"));
        Block testTypeTwo = new SolidBlock((byte) 2, "test2", fm.loadTexture("dirt.png"));
        testArray.addVoxelType(testTypeOne);
        testArray.addVoxelType(testTypeTwo);

        testArray.updateBlocks(0.3);

        assertEquals(2, testArray.getTypeCount());
    }

    @Test
    public void testUpdateChunk() {
        Chunk[] testChunks = new ArrayChunk[7];
        for (int i = 0; i < testChunks.length; ++i)
            testChunks[i] = new ArrayChunk();

        testArray.putChunk(0, 0, 0, testChunks[0]);
        testArray.putChunk(-1, 0, 0, testChunks[1]);
        testArray.putChunk(1, 0, 0, testChunks[2]);
        testArray.putChunk(0, -1, 0, testChunks[3]);
        testArray.putChunk(0, 1, 0, testChunks[4]);
        testArray.putChunk(0, 0, -1, testChunks[5]);
        testArray.putChunk(0, 0, 1, testChunks[6]);

        for (Chunk testChunk : testChunks) {
            testChunk.applyBuild(
                    testMesher,
                    new NeumannNeighborhood(testArray, new Coord3Int(0, 0, 0)),
                    null);
        }

        for (Chunk testChunk : testChunks)
            assertFalse(testChunk.needRebuild());

        testArray.updateChunk(new Coord3Int(0, 0, 0), 0, 0, 0);

        assertTrue(testChunks[0].needRebuild());
        assertTrue(testChunks[1].needRebuild());
        assertTrue(testChunks[3].needRebuild());
        assertTrue(testChunks[5].needRebuild());
        assertFalse(testChunks[2].needRebuild());
        assertFalse(testChunks[4].needRebuild());
        assertFalse(testChunks[6].needRebuild());

        final int maxIndex = Chunk.getSize() - 1;
        testArray.updateChunk(new Coord3Int(0, 0, 0), maxIndex, maxIndex, maxIndex);
        assertTrue(testChunks[0].needRebuild());
        assertTrue(testChunks[2].needRebuild());
        assertTrue(testChunks[4].needRebuild());
        assertTrue(testChunks[6].needRebuild());
    }

    @Test
    public void testGetChunk() {
        Chunk nullChunk = testArray.getChunk(1, 11, 12);

        assertNull(nullChunk);

        Chunk testChunk = new ArrayChunk();
        testArray.putChunk(6, 7, 8, testChunk);
        Chunk actualChunk = testArray.getChunk(6, 7, 8);

        assertEquals(testChunk, actualChunk);
        assertNull(testArray.getChunk(6, 7, 9));
    }

    @Test
    public void testPutChunk() {
        Chunk testChunk = new ArrayChunk();
        testArray.putChunk(0, 0, 0, testChunk);
        Chunk actualChunk = testArray.getChunk(0, 0, 0);

        assertEquals(testChunk, actualChunk);

        testArray.putChunk(3, 3, 3, testChunk);
        actualChunk = testArray.getChunk(0, 0, 0);

        assertNull(actualChunk);
    }

    @Test(expected = NullPointerException.class)
    public void testPutNullChunk() {
        testArray.putChunk(0, 0, 0, null);
    }

    @Test
    public void testDiameter() {
        assertEquals(3, testArray.getDiameter());
    }

    @Test
    public void testHeightGreaterThanZero() {
        assertTrue(testArray.getHeight() > 0);
    }

    @Test
    public void testCleanup() {
        FileManager fm = FileManager.getInstance();
        Block testTypeOne = new SolidBlock((byte) 1, "test1", fm.loadTexture("dirt.png"));
        Block testTypeTwo = new SolidBlock((byte) 2, "test2", fm.loadTexture("dirt.png"));
        testArray.addVoxelType(testTypeOne);
        testArray.addVoxelType(testTypeTwo);
        testArray.putChunk(0, 0, 0, new ArrayChunk());
        testArray.putChunk(0, 0, -10, new ArrayChunk());
        ArrayList<Chunk> visible = new ArrayList<>();

        testArray.getAll(visible);

        assertEquals(2, visible.size());

        assertEquals(2, testArray.getTypeCount());
        assertEquals(1, testArray.getTypeID(0));
        assertEquals(2, testArray.getTypeID(1));

        testArray.cleanup();

        assertEquals(0, testArray.getTypeCount());
        assertEquals(0, testArray.getTypeID(0));
        assertEquals(0, testArray.getTypeID(1));

        visible.clear();
        testArray.getAll(visible);

        assertEquals(0, visible.size());
    }

    @After
    public void tearDown() {
        placeholderWindow.destroy();
    }
}
