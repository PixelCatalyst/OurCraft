package com.pixcat.voxel;

import com.pixcat.mesh.GreedyMesher;
import com.pixcat.mesh.Mesher;
import org.junit.Test;

import static org.junit.Assert.*;

public class ArrayChunkTest {
    private Chunk testChunk = new ArrayChunk();

    @Test
    public void testInitialWorldPositionIsNull() {
        assertNull(testChunk.getWorldPosition());
    }

    @Test
    public void testSetWorldPositionToNull() {
        testChunk.setWorldPosition(null);

        assertNull(testChunk.getWorldPosition());
    }

    @Test
    public void testSetWorldPosition() {
        Coord3Int expectedPosition = new Coord3Int(8, 9, 10);
        testChunk.setWorldPosition(expectedPosition);

        Coord3Int actualPosition = testChunk.getWorldPosition();

        assertEquals(expectedPosition.x, actualPosition.x);
        assertEquals(expectedPosition.y, actualPosition.y);
        assertEquals(expectedPosition.z, actualPosition.z);
    }

    @Test
    public void testDefaultIDZero() {
        for (int y = 0; y < testChunk.getSize(); ++y) {
            for (int x = 0; x < testChunk.getSize(); ++x) {
                for (int z = 0; z < testChunk.getSize(); ++z) {
                    assertEquals(testChunk.getVoxelID(y, x, z), (byte) 0);
                }
            }
        }
    }

    @Test
    public void testSetAllChunkToID() {
        final byte ID = 2;
        for (int y = 0; y < testChunk.getSize(); ++y) {
            for (int x = 0; x < testChunk.getSize(); ++x) {
                for (int z = 0; z < testChunk.getSize(); ++z) {
                    testChunk.setVoxelID(y, x, z, ID);

                    assertEquals(ID, testChunk.getVoxelID(y, x, z));
                }
            }
        }
    }

    @Test(expected = ArrayIndexOutOfBoundsException.class)
    public void testOutOfBounds() {
        testChunk.setVoxelID(testChunk.getSize() + 15, 0, 0, (byte) 0);
    }

    @Test
    public void testSizeIsGreaterThanZero() {
        int size = testChunk.getSize();

        assertTrue(size > 0);
    }

    @Test
    public void needRebuild() {
        //TODO
    }

    @Test
    public void testInitialGraphicIsNull() {
        assertNull(testChunk.getGraphic());
    }

    @Test
    public void testCleanupWhenNotInitialized() {
        testChunk.cleanup();

        assertFalse(testChunk.needRebuild());
        assertNull(testChunk.getGraphic());
    }
}
