package com.pixcat.mesh;

import com.pixcat.graphics.GraphicBatch;
import com.pixcat.graphics.GraphicObject;
import com.pixcat.graphics.Texture;
import com.pixcat.voxel.Chunk;
import com.pixcat.voxel.Coord3Int;
import com.pixcat.voxel.NeumannNeighborhood;

public class GreedyMesher implements Mesher {

    private static class VoxelFace {
        private static final int SOUTH = 0;
        private static final int NORTH = 1;
        private static final int EAST = 2;
        private static final int WEST = 3;
        private static final int TOP = 4;
        private static final int BOTTOM = 5;

        private boolean[] visible = new boolean[6];
        private boolean opaque = true;
        private byte type = 0;

        private boolean equals(final VoxelFace that) {
            return (this.opaque == that.opaque) && (this.type == that.type);
        }
    }

    private final int maxAxis;
    private final byte airID;
    private final VoxelFace[][][] voxels;
    private final VoxelFace[] mask;
    private final int[] backFaceIndices;
    private final int[] frontFaceIndices;
    private final float[] workingVertices;
    private final float[] workingTexCoords;

    public GreedyMesher() {
        int chunkSize = Chunk.getSize();
        if (chunkSize < 1)
            throw new IllegalArgumentException("Chunk size can not be less than 1");
        maxAxis = Chunk.getSize() - 1;
        voxels = new VoxelFace[chunkSize][chunkSize][chunkSize];
        airID = 0;
        mask = new VoxelFace[chunkSize * chunkSize];
        backFaceIndices = new int[]{1, 0, 3, 3, 2, 1};
        frontFaceIndices = new int[]{1, 2, 3, 3, 0, 1};
        workingVertices = new float[4 * 3]; //4 * 3-dimensional vertices
        workingTexCoords = new float[4 * 2]; //4 * 2-dimensional coords
    }

    public GraphicBatch processChunk(NeumannNeighborhood chunkWithNeighbors, Texture[] materials) {
        readChunk(chunkWithNeighbors);
        Chunk toMesh = chunkWithNeighbors.central;
        GraphicBatch batch = new GraphicBatch();

        int width, height, u, v, n, side;

        final int[] x = new int[]{0, 0, 0};
        final int[] q = new int[]{0, 0, 0};
        final int[] du = new int[]{0, 0, 0};
        final int[] dv = new int[]{0, 0, 0};

        VoxelFace firstFace;
        VoxelFace secondFace;
        for (boolean backFace = true, flip = false; flip != backFace; backFace = (backFace && flip), flip = !flip) {
            final int totalDimensions = 3;
            for (int dimension = 0; dimension < totalDimensions; dimension++) {

                u = (dimension + 1) % totalDimensions;
                v = (dimension + 2) % totalDimensions;

                x[0] = 0;
                x[1] = 0;
                x[2] = 0;

                q[0] = 0;
                q[1] = 0;
                q[2] = 0;
                q[dimension] = 1;

                if (dimension == 0)
                    side = (backFace ? VoxelFace.WEST : VoxelFace.EAST);
                else if (dimension == 1)
                    side = (backFace ? VoxelFace.BOTTOM : VoxelFace.TOP);
                else
                    side = (backFace ? VoxelFace.SOUTH : VoxelFace.NORTH);

                for (x[dimension] = -1; x[dimension] < Chunk.getSize(); ) {
                    n = 0;
                    for (x[v] = 0; x[v] < Chunk.getSize(); ++x[v]) {
                        for (x[u] = 0; x[u] < Chunk.getSize(); ++x[u]) {
                            if (x[dimension] >= 0)
                                firstFace = getVoxelFace(x[0], x[1], x[2], side);
                            else
                                firstFace = null;
                            if (x[dimension] < maxAxis)
                                secondFace = getVoxelFace(x[0] + q[0], x[1] + q[1], x[2] + q[2], side);
                            else
                                secondFace = null;

                            if (firstFace != null && secondFace != null && firstFace.equals(secondFace))
                                mask[n] = null;
                            else
                                mask[n] = (backFace ? secondFace : firstFace);
                            ++n;
                        }
                    }
                    ++x[dimension];

                    n = 0;
                    for (int j = 0; j < Chunk.getSize(); ++j) {
                        for (int i = 0; i < Chunk.getSize(); ) {
                            if (mask[n] != null) {

                                width = 1;
                                while ((i + width) < Chunk.getSize()) {
                                    VoxelFace currFace = mask[n + width];
                                    if (currFace == null || !currFace.equals(mask[n]))
                                        break;
                                    ++width;
                                }

                                boolean done = false;
                                for (height = 1; (j + height) < Chunk.getSize(); ++height) {
                                    for (int k = 0; k < width; ++k) {
                                        VoxelFace currFace = mask[n + k + (height * Chunk.getSize())];
                                        if (currFace == null || !currFace.equals(mask[n])) {
                                            done = true;
                                            break;
                                        }
                                    }
                                    if (done)
                                        break;
                                }

                                if (mask[n].opaque) {
                                    x[u] = i;
                                    x[v] = j;

                                    du[0] = du[1] = du[2] = 0;
                                    du[u] = width;

                                    dv[0] = dv[1] = dv[2] = 0;
                                    dv[v] = height;

                                    assembleVertices(x, du, dv);
                                    assembleTexCoords((float) width, (float) height, side);
                                    GraphicObject obj = createQuad(mask[n], backFace, materials);
                                    batch.addObject(obj);
                                }

                                for (int m = 0; m < height; ++m) {
                                    for (int k = 0; k < width; ++k)
                                        mask[n + k + (m * Chunk.getSize())] = null;
                                }

                                i += width;
                                n += width;

                            } else {
                                ++i;
                                ++n;
                            }
                        }
                    }
                }
            }
        }

        Coord3Int pos = toMesh.getWorldPosition();
        batch.setPosition(pos.x * Chunk.getSize(), pos.y * Chunk.getSize(), pos.z * Chunk.getSize());
        batch.bakeTextures();
        return batch;
    }

    private void readChunk(NeumannNeighborhood chunkWithNeighbors) {
        Chunk chunk = chunkWithNeighbors.central;
        for (int x = 0; x < Chunk.getSize(); ++x) {
            for (int y = 0; y < Chunk.getSize(); ++y) {
                for (int z = 0; z < Chunk.getSize(); ++z) {
                    VoxelFace face = new VoxelFace();
                    face.type = chunk.getVoxelID(y, x, z);
                    fillCulling(chunkWithNeighbors, y, x, z, face.visible);
                    voxels[x][y][z] = face;
                }
            }
        }
    }

    private void fillCulling(NeumannNeighborhood chunkWithNeighbors, int y, int x, int z, boolean[] visible) {
        for (int i = 0; i < 6; ++i)
            visible[i] = false;
        Chunk chunk = chunkWithNeighbors.central;

        if (chunk.getVoxelID(y, x, z) != airID) {
            cullAxisY(chunkWithNeighbors, y, x, z, visible);
            cullAxisX(chunkWithNeighbors, y, x, z, visible);
            cullAxisZ(chunkWithNeighbors, y, x, z, visible);
        }
    }

    private void cullAxisY(NeumannNeighborhood chunkWithNeighbors, int y, int x, int z, boolean[] visible) {
        Chunk chunk = chunkWithNeighbors.central;
        if (y == 0) {
            visible[VoxelFace.BOTTOM] = (chunkWithNeighbors.bottom == null) ||
                    (chunkWithNeighbors.bottom.getVoxelID(maxAxis, x, z) == airID);
            visible[VoxelFace.TOP] = (chunk.getVoxelID(y + 1, x, z) == airID);
        } else if (y == maxAxis) {
            visible[VoxelFace.BOTTOM] = (chunk.getVoxelID(y - 1, x, z) == airID);
            visible[VoxelFace.TOP] = (chunkWithNeighbors.top == null) ||
                    (chunkWithNeighbors.top.getVoxelID(0, x, z) == airID);
        } else {
            visible[VoxelFace.BOTTOM] = (chunk.getVoxelID(y - 1, x, z) == airID);
            visible[VoxelFace.TOP] = (chunk.getVoxelID(y + 1, x, z) == airID);
        }
    }

    private void cullAxisX(NeumannNeighborhood chunkWithNeighbors, int y, int x, int z, boolean[] visible) {
        Chunk chunk = chunkWithNeighbors.central;
        if (x == 0) {
            visible[VoxelFace.WEST] = (chunkWithNeighbors.west == null) ||
                    (chunkWithNeighbors.west.getVoxelID(y, maxAxis, z) == airID);
            visible[VoxelFace.EAST] = (chunk.getVoxelID(y, x + 1, z) == airID);
        } else if (x == maxAxis) {
            visible[VoxelFace.WEST] = (chunk.getVoxelID(y, x - 1, z) == airID);
            visible[VoxelFace.EAST] = (chunkWithNeighbors.east == null) ||
                    (chunkWithNeighbors.east.getVoxelID(y, 0, z) == airID);
        } else {
            visible[VoxelFace.WEST] = (chunk.getVoxelID(y, x - 1, z) == airID);
            visible[VoxelFace.EAST] = (chunk.getVoxelID(y, x + 1, z) == airID);
        }
    }

    private void cullAxisZ(NeumannNeighborhood chunkWithNeighbors, int y, int x, int z, boolean[] visible) {
        Chunk chunk = chunkWithNeighbors.central;
        if (z == 0) {
            visible[VoxelFace.SOUTH] = (chunkWithNeighbors.south == null) ||
                    (chunkWithNeighbors.south.getVoxelID(y, x, maxAxis) == airID);
            visible[VoxelFace.NORTH] = (chunk.getVoxelID(y, x, z + 1) == airID);
        } else if (z == maxAxis) {
            visible[VoxelFace.SOUTH] = (chunk.getVoxelID(y, x, z - 1) == airID);
            visible[VoxelFace.NORTH] = (chunkWithNeighbors.north == null) ||
                    (chunkWithNeighbors.north.getVoxelID(y, x, 0) == airID);
        } else {
            visible[VoxelFace.SOUTH] = (chunk.getVoxelID(y, x, z - 1) == airID);
            visible[VoxelFace.NORTH] = (chunk.getVoxelID(y, x, z + 1) == airID);
        }
    }

    private VoxelFace getVoxelFace(int x, int y, int z, int side) {
        VoxelFace voxelFace = voxels[x][y][z];
        voxelFace.opaque = voxelFace.visible[side];
        if (voxelFace.type == (byte) 0)
            voxelFace.opaque = false;
        return voxelFace;
    }

    private void assembleVertices(int[] x, int[] du, int[] dv) {
        workingVertices[0] = x[0];
        workingVertices[1] = x[1];
        workingVertices[2] = x[2];
        workingVertices[3] = x[0] + du[0];
        workingVertices[4] = x[1] + du[1];
        workingVertices[5] = x[2] + du[2];
        workingVertices[6] = x[0] + du[0] + dv[0];
        workingVertices[7] = x[1] + du[1] + dv[1];
        workingVertices[8] = x[2] + du[2] + dv[2];
        workingVertices[9] = x[0] + dv[0];
        workingVertices[10] = x[1] + dv[1];
        workingVertices[11] = x[2] + dv[2];
    }

    private void assembleTexCoords(float width, float height, int side) {
        for (int i = 0; i < workingTexCoords.length; ++i)
            workingTexCoords[i] = 0.0f;

        if ((side == VoxelFace.NORTH) || (side == VoxelFace.SOUTH)) {
            workingTexCoords[2] = width;
            workingTexCoords[4] = width;
            workingTexCoords[5] = height;
            workingTexCoords[7] = height;
        } else {
            workingTexCoords[1] = width;
            workingTexCoords[4] = height;
            workingTexCoords[6] = height;
            workingTexCoords[7] = width;
        }
    }

    private GraphicObject createQuad(final VoxelFace voxel, boolean backFace, Texture[] materials) {
        final int[] indices = (backFace ? backFaceIndices : frontFaceIndices);
        Mesh mesh = new Mesh(workingVertices, workingTexCoords, indices);
        GraphicObject quad = new GraphicObject(mesh);
        quad.setTexture(materials[voxel.type - 1]);
        return quad;
    }
}
