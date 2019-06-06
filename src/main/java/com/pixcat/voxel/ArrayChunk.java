package com.pixcat.voxel;

import com.pixcat.graphics.GraphicBatch;
import com.pixcat.graphics.Texture;
import com.pixcat.mesh.Mesher;

public class ArrayChunk implements Chunk {
    private static final int size = Chunk.getSize();
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
    public boolean needRebuild() {
        return dirtyFlag;
    }

    @Override
    public void applyBuild(Mesher mesher, NeumannNeighborhood neighbors, Texture[] materials) {
        if (dirtyFlag) {
            cleanup();
            graphicBatch = mesher.processChunk(neighbors, materials);
            dirtyFlag = false;
        }
    }

    @Override
    public GraphicBatch getGraphic() {
        return graphicBatch;
    }

    @Override
    public void cleanup() {
        dirtyFlag = false;
        if (graphicBatch != null) {
            graphicBatch.cleanup();
            graphicBatch = null;
        }
    }
}
