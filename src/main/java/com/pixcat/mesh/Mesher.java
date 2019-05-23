package com.pixcat.mesh;

import com.pixcat.graphics.GraphicBatch;
import com.pixcat.graphics.Texture;
import com.pixcat.voxel.NeumannNeighborhood;

public interface Mesher {

    GraphicBatch processChunk(NeumannNeighborhood chunkWithNeighbors, Texture[] materials);
}
