package com.pixcat.voxel;

public interface Chunk {

    byte getVoxelID(int x, int y, int z);

    void setVoxelID(int x, int y, int z, byte id);

    int getSize();
}
