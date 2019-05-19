package com.pixcat.voxel;

public interface SpatialStructure {

    void addVoxelType(Block block);

    Block getVoxelFromID(byte ID);

    int getTypeCount();

    void updateBlocks(double elapsedTime);

    Chunk getChunk(int y, int x, int z);

    void putChunk(int y, int x, int z, Chunk toPut);

    int getDiameter();
}
