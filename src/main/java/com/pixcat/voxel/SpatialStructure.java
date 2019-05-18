package com.pixcat.voxel;

public interface SpatialStructure {

    void addVoxelType(Block block);

    Block getVoxelFromID(byte ID);

    Block getVoxelFromName(String name);

    void updateBlocks(double elapsedTime);

    Chunk getChunk(int y, int x, int z);

    void putChunk(int y, int x, int z, Chunk toPut);

    int getDiameter();
}
