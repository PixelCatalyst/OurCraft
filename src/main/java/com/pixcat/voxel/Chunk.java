package com.pixcat.voxel;

public interface Chunk {

    byte getVoxelID(int y, int x, int z);

    void setVoxelID(int y, int x, int z, byte ID);

    int getSize();
}
