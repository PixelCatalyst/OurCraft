package com.pixcat.voxel;

import java.util.ArrayList;

public interface SpatialStructure {

    public void getAll(ArrayList<Chunk> vis);

    void addVoxelType(Block block);

    Block getVoxelFromID(byte ID);

    int getTypeCount();

    void updateBlocks(double elapsedTime);

    Chunk getChunk(int y, int x, int z);

    void putChunk(int y, int x, int z, Chunk toPut);

    int getDiameter();

    int getHeight();
}
