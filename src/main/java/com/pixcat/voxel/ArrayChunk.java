package com.pixcat.voxel;

public class ArrayChunk implements Chunk {
    private static final int size = 16;
    private byte[][][] voxels;

    public ArrayChunk() {
        voxels = new byte[size][size][size];
    }

    @Override
    public byte getVoxelID(int x, int y, int z) {
        return voxels[x][y][z];
    }

    public void setVoxelID(int x, int y, int z, byte id) {
        voxels[x][y][z] = id;
    }

    @Override
    public int getSize() {
        return size;
    }
}
