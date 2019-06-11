package com.pixcat.voxel;

import java.util.ArrayList;

public interface SpatialStructure {

    void getAll(ArrayList<Chunk> vis);

    void addVoxelType(Block block);

    Block getVoxelFromID(byte ID);

    byte getTypeID(int index);

    int getTypeCount();

    void updateBlocks(double elapsedTime);

    void updateChunk(Coord3Int chunkCoord, int voxelY, int voxelX, int voxelZ);

    Chunk getChunk(int y, int x, int z);

    void putChunk(int y, int x, int z, Chunk toPut);

    int getDiameter();

    int getHeight();

    void cleanup();
}
