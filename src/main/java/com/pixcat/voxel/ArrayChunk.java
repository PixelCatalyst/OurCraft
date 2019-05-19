package com.pixcat.voxel;

import com.pixcat.graphics.GraphicBatch;
import com.pixcat.graphics.Texture;
import com.pixcat.mesh.Mesher;

public class ArrayChunk implements Chunk {
    private static final int size = 16;
    private byte[][][] voxels;
    private Coord3Int position;
    private GraphicBatch graphicBatch;
    private boolean dirtyFlag;

    public ArrayChunk() {
        voxels = new byte[size][size][size];
        dirtyFlag = true;
    }

    @Override
    public Coord3Int getWorldPosition() {
        return position;
    }

    @Override
    public void setWorldPosition(Coord3Int position) {
        this.position = position;
    }

    @Override
    public byte getVoxelID(int y, int x, int z) {
        return voxels[x][y][z];
    }

    public void setVoxelID(int y, int x, int z, byte ID) {
        voxels[x][y][z] = ID;
        dirtyFlag = true;
    }

    @Override
    public int getSize() {
        return size;
    }

    @Override
    public void build(Mesher mesher, Texture[] materials) {
        if (dirtyFlag) {
            graphicBatch = mesher.processChunk(this, materials);
            dirtyFlag = false;
        }
    }

    @Override
    public GraphicBatch getGraphic() {
        return graphicBatch;
    }
}
