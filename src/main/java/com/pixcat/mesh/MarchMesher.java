package com.pixcat.mesh;

import com.pixcat.graphics.GraphicBatch;
import com.pixcat.graphics.GraphicObject;
import com.pixcat.graphics.Texture;
import com.pixcat.voxel.Chunk;
import com.pixcat.voxel.Coord3Int;
import com.pixcat.voxel.NeumannNeighborhood;

import java.util.HashMap;
import java.util.Map;

public class MarchMesher implements Mesher {

    private static class VisibleFaces {
        //from 0: back, front, left, right, bottom, top
        public boolean[] face = new boolean[6];

        public VisibleFaces makeCopy() {
            VisibleFaces copy = new VisibleFaces();
            System.arraycopy(this.face, 0, copy.face, 0, this.face.length);
            return copy;
        }

        public boolean nextPermutation() {
            int code = hashCode();
            ++code;
            if (code >= Math.pow(2, 6))
                return false;
            dehash(code);
            return true;
        }

        @Override
        public int hashCode() {
            int hash = 0;
            int j = 1;
            for (int i = 0; i < 6; ++i) {
                if (face[i])
                    hash += j;
                j *= 2;
            }
            return hash;
        }

        private void dehash(int code) {
            for (int i = 0; i < 6; ++i) {
                face[i] = (code % 2) > 0;
                code /= 2;
            }
        }

        public int countVisible() {
            int visible = 0;
            for (int i = 0; i < 6; ++i)
                visible += (face[i] ? 1 : 0);
            return visible;
        }
    }

    private Map<Integer, Mesh> faceMap;

    public MarchMesher() {
        faceMap = new HashMap<>();

        int[] templateIndices = new int[]{
                0, 1, 3, 3, 1, 2,
        };
        float[] templateTexCoords = new float[]{
                0.0f, 1.0f,
                0.0f, 0.0f,
                1.0f, 0.0f,
                1.0f, 1.0f
        };

        float[] back = new float[]{
                0.0f, 0.0f, 0.0f,
                0.0f, 1.0f, 0.0f,
                1.0f, 1.0f, 0.0f,
                1.0f, 0.0f, 0.0f,
        };
        float[] front = new float[]{
                0.0f, 0.0f, 1.0f,
                0.0f, 1.0f, 1.0f,
                1.0f, 1.0f, 1.0f,
                1.0f, 0.0f, 1.0f,
        };
        float[] left = new float[]{
                0.0f, 0.0f, 0.0f,
                0.0f, 1.0f, 0.0f,
                0.0f, 1.0f, 1.0f,
                0.0f, 0.0f, 1.0f,
        };
        float[] right = new float[]{
                1.0f, 0.0f, 0.0f,
                1.0f, 1.0f, 0.0f,
                1.0f, 1.0f, 1.0f,
                1.0f, 0.0f, 1.0f,
        };
        float[] bottom = new float[]{
                0.0f, 0.0f, 1.0f,
                0.0f, 0.0f, 0.0f,
                1.0f, 0.0f, 0.0f,
                1.0f, 0.0f, 1.0f,
        };
        float[] top = new float[]{
                0.0f, 1.0f, 1.0f,
                0.0f, 1.0f, 0.0f,
                1.0f, 1.0f, 0.0f,
                1.0f, 1.0f, 1.0f,
        };

        VisibleFaces vf = new VisibleFaces();
        while (vf.nextPermutation()) {
            int faceCount = vf.countVisible();
            float[] positions = new float[faceCount * 12];
            float[] texCoords = new float[faceCount * 8];
            int[] indices = new int[faceCount * 6];

            VisibleFaces copyVf = vf.makeCopy();
            for (int i = 0; i < faceCount; ++i) {
                if (copyVf.face[0]) {
                    System.arraycopy(back, 0, positions, i * 12, 12);
                    copyVf.face[0] = false;
                } else if (copyVf.face[1]) {
                    System.arraycopy(front, 0, positions, i * 12, 12);
                    copyVf.face[1] = false;
                } else if (copyVf.face[2]) {
                    System.arraycopy(left, 0, positions, i * 12, 12);
                    copyVf.face[2] = false;
                } else if (copyVf.face[3]) {
                    System.arraycopy(right, 0, positions, i * 12, 12);
                    copyVf.face[3] = false;
                } else if (copyVf.face[4]) {
                    System.arraycopy(bottom, 0, positions, i * 12, 12);
                    copyVf.face[4] = false;
                } else if (copyVf.face[5]) {
                    System.arraycopy(top, 0, positions, i * 12, 12);
                    copyVf.face[5] = false;
                }

                System.arraycopy(templateTexCoords, 0, texCoords, i * 8, 8);
                System.arraycopy(templateIndices, 0, indices, i * 6, 6);
            }
            for (int i = 1; i < faceCount; ++i) {
                for (int j = 0; j < 6; ++j)
                    indices[i * 6 + j] += i * 4;
            }
            faceMap.put(vf.hashCode(), new Mesh(positions, texCoords, indices));
        }
    }

    @Override
    public GraphicBatch processChunk(NeumannNeighborhood chunkWithNeighbors, Texture[] materials) {
        GraphicBatch chunkBatch = new GraphicBatch();
        Chunk toMesh = chunkWithNeighbors.central;
        VisibleFaces visibleFaces = new VisibleFaces();
        final byte air = (byte) 0;
        final int chunkSize = toMesh.getSize();

        for (int y = 0; y < chunkSize; ++y) {
            for (int x = 0; x < chunkSize; ++x) {
                for (int z = 0; z < chunkSize; ++z) {
                    byte voxelID = toMesh.getVoxelID(y, x, z);
                    if (voxelID != air) {
                        getVisibility(toMesh, y, x, z, visibleFaces);
                        Mesh face = faceMap.get(visibleFaces.hashCode());
                        if (face != null) {
                            GraphicObject blockFaces = new GraphicObject(face);
                            blockFaces.setTexture(materials[voxelID - 1]);
                            blockFaces.setPosition(x, y, z);
                            chunkBatch.addObject(blockFaces);
                        }
                    }
                }
            }
        }
        Coord3Int pos = toMesh.getWorldPosition();
        chunkBatch.setPosition(pos.x * chunkSize, pos.y * chunkSize, pos.z * chunkSize);
        chunkBatch.bakeTextures();
        return chunkBatch;
    }

    private void getVisibility(Chunk chunk, int y, int x, int z, VisibleFaces output) {
        //f0-back, 1-front, 2-left, 3-right, 4-bottom, 5-top
        int max = chunk.getSize() - 1;
        final byte air = (byte) 0;
        if (y == 0) {
            output.face[4] = true;
            output.face[5] = (chunk.getVoxelID(y + 1, x, z) == air);
        } else if (y == max) {
            output.face[4] = (chunk.getVoxelID(y - 1, x, z) == air);
            output.face[5] = true;
        } else {
            output.face[4] = (chunk.getVoxelID(y - 1, x, z) == air);
            output.face[5] = (chunk.getVoxelID(y + 1, x, z) == air);
        }

        if (x == 0) {
            output.face[2] = true;
            output.face[3] = (chunk.getVoxelID(y, x + 1, z) == air);
        } else if (x == max) {
            output.face[2] = (chunk.getVoxelID(y, x - 1, z) == air);
            output.face[3] = true;
        } else {
            output.face[2] = (chunk.getVoxelID(y, x - 1, z) == air);
            output.face[3] = (chunk.getVoxelID(y, x + 1, z) == air);
        }

        if (z == 0) {
            output.face[0] = true;
            output.face[1] = (chunk.getVoxelID(y, x, z + 1) == air);
        } else if (z == max) {
            output.face[0] = (chunk.getVoxelID(y, x, z - 1) == air);
            output.face[1] = true;
        } else {
            output.face[0] = (chunk.getVoxelID(y, x, z - 1) == air);
            output.face[1] = (chunk.getVoxelID(y, x, z + 1) == air);
        }
    }
}
