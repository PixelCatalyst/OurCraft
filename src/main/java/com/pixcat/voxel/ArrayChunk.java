package com.pixcat.voxel;

public class ArrayChunk implements Chunk {
    private static final int size = 16;
    private byte[][][] voxels;

    public ArrayChunk() {
        voxels = new byte[size][size][size];
    }

    @Override
    public byte getVoxelID(int y, int x, int z) {
        return voxels[x][y][z];
    }

    public void setVoxelID(int y, int x, int z, byte ID) {
        voxels[x][y][z] = ID;
    }

    @Override
    public int getSize() {
        return size;
    }
}
