package com.pixcat.mesh;

import com.pixcat.graphics.GraphicBatch;
import com.pixcat.graphics.Texture;
import com.pixcat.voxel.Chunk;

public interface Mesher {

    GraphicBatch processChunk(Chunk toMesh, Texture[] materials);
}
