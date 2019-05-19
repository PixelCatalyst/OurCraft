package com.pixcat.voxel;

import com.pixcat.graphics.GraphicBatch;
import com.pixcat.graphics.Texture;
import com.pixcat.mesh.Mesher;

public interface Chunk {

    byte getVoxelID(int y, int x, int z);

    void setVoxelID(int y, int x, int z, byte ID);

    int getSize();

    void build(Mesher mesher, Texture[] materials);

    GraphicBatch getGraphic();
}
