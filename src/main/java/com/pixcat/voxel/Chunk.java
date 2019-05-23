package com.pixcat.voxel;

import com.pixcat.graphics.GraphicBatch;
import com.pixcat.graphics.Texture;
import com.pixcat.mesh.Mesher;

public interface Chunk {

    Coord3Int getWorldPosition();

    void setWorldPosition(Coord3Int position);

    byte getVoxelID(int y, int x, int z);

    void setVoxelID(int y, int x, int z, byte ID);

    int getSize();

    boolean needRebuild();

    void applyBuild(Mesher mesher, NeumannNeighborhood neighbors, Texture[] materials);

    GraphicBatch getGraphic();
}
