package com.pixcat.voxel;

public interface SpatialStructure {

    void addVoxelType(Block block);

    Block getVoxelFromID(byte ID);

    Block getVoxelFromName(String name);

    void updateBlocks(double elapsedTime);

    Chunk getChunk(int x, int y, int z);

    void putChunk(int x, int y, int z, Chunk toPut);

    int getDiameter();
}
