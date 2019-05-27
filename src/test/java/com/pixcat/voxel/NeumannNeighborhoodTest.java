package com.pixcat.voxel;

import org.junit.Test;

import static org.junit.Assert.*;

public class NeumannNeighborhoodTest {
    private SpatialStructure chunkArray = new VirtualArray(8);

    @Test
    public void testEmptyChunkArray() {
        NeumannNeighborhood testNeighborhood = new NeumannNeighborhood(chunkArray, new Coord3Int(0, 0, 0));

        assertNull(testNeighborhood.central);
        assertNull(testNeighborhood.bottom);
        assertNull(testNeighborhood.top);
        assertNull(testNeighborhood.west);
        assertNull(testNeighborhood.east);
        assertNull(testNeighborhood.south);
        assertNull(testNeighborhood.north);
    }

    @Test
    public void testOnlyCentralNotNull() {
        chunkArray.putChunk(0, 0, 0, new ArrayChunk());

        NeumannNeighborhood testNeighborhood = new NeumannNeighborhood(chunkArray, new Coord3Int(0, 0, 0));

        assertNotNull(testNeighborhood.central);
        assertNull(testNeighborhood.bottom);
        assertNull(testNeighborhood.top);
        assertNull(testNeighborhood.west);
        assertNull(testNeighborhood.east);
        assertNull(testNeighborhood.south);
        assertNull(testNeighborhood.north);
    }

    @Test
    public void testTypicalChunks() {
        chunkArray.putChunk(0, 0, 0, new ArrayChunk());
        chunkArray.putChunk(-1, 0, 0, new ArrayChunk());
        chunkArray.putChunk(1, 0, 0, new ArrayChunk());
        chunkArray.putChunk(0, -1, 0, new ArrayChunk());
        chunkArray.putChunk(0, 1, 0, new ArrayChunk());
        chunkArray.putChunk(0, 0, -1, new ArrayChunk());
        chunkArray.putChunk(0, 0, 1, new ArrayChunk());

        NeumannNeighborhood testNeighborhood = new NeumannNeighborhood(chunkArray, new Coord3Int(0, 0, 0));

        assertNotNull(testNeighborhood.central);
        assertNotNull(testNeighborhood.bottom);
        assertNotNull(testNeighborhood.top);
        assertNotNull(testNeighborhood.west);
        assertNotNull(testNeighborhood.east);
        assertNotNull(testNeighborhood.south);
        assertNotNull(testNeighborhood.north);
        assertEquals(new Coord3Int(0, 0, 0), testNeighborhood.central.getWorldPosition());
        assertEquals(new Coord3Int(-1, 0, 0), testNeighborhood.bottom.getWorldPosition());
        assertEquals(new Coord3Int(1, 0, 0), testNeighborhood.top.getWorldPosition());
        assertEquals(new Coord3Int(0, -1, 0), testNeighborhood.west.getWorldPosition());
        assertEquals(new Coord3Int(0, 1, 0), testNeighborhood.east.getWorldPosition());
        assertEquals(new Coord3Int(0, 0, -1), testNeighborhood.south.getWorldPosition());
        assertEquals(new Coord3Int(0, 0, 1), testNeighborhood.north.getWorldPosition());
    }
}
