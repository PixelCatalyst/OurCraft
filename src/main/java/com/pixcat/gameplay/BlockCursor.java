package com.pixcat.gameplay;

import com.pixcat.graphics.GraphicObject;
import com.pixcat.graphics.Renderer;
import com.pixcat.mesh.Mesh;
import com.pixcat.voxel.Chunk;
import com.pixcat.voxel.SpatialStructure;
import org.joml.Vector3f;
import org.joml.Vector3i;

public class BlockCursor {
    private final int maxRayLength;
    private byte currentID;
    private Vector3i currentChunkPos;
    private Vector3i currentVoxelPos;
    private Vector3i previousChunkPos;
    private Vector3i previousVoxelPos;
    private boolean traversedEmpty;
    private GraphicObject indicationBox;

    public BlockCursor() {
        maxRayLength = 6;
        currentChunkPos = new Vector3i(0, 0, 0);
        currentVoxelPos = new Vector3i(0, 0, 0);
        previousChunkPos = new Vector3i(0, 0, 0);
        previousVoxelPos = new Vector3i(0, 0, 0);
        createIndicationBox();
    }

    private void createIndicationBox() {
        float[] boxPositions = new float[]{
                0.0f, 0.0f, 0.0f,
                1.0f, 0.0f, 0.0f,
                0.0f, 0.0f, 1.0f,
                1.0f, 0.0f, 1.0f,
                0.0f, 1.0f, 0.0f,
                1.0f, 1.0f, 0.0f,
                0.0f, 1.0f, 1.0f,
                1.0f, 1.0f, 1.0f
        };
        float[] boxTexCoords = new float[12 * 2];
        int[] boxIndices = new int[]{
                0, 1,
                0, 2,
                2, 3,
                3, 1,
                0, 4,
                1, 5,
                2, 6,
                3, 7,
                4, 5,
                4, 6,
                6, 7,
                7, 5
        };
        Mesh boxMesh = new Mesh(boxPositions, boxTexCoords, boxIndices);
        indicationBox = new GraphicObject(boxMesh);
    }

    public void castRay(Vector3f startPos, Vector3f cameraDir, SpatialStructure voxels) {
        Vector3i position = createPositionVector(startPos);
        Vector3i step = createStepVector(cameraDir);
        Vector3f deltaDist = createDeltaVector(step, cameraDir);
        Vector3f sideDist = createSideVector(step, deltaDist, startPos);

        currentID = 0;
        traversedEmpty = false;
        for (int rayLength = 0; (currentID == 0) && (rayLength <= maxRayLength); ++rayLength) {
            advanceRay(sideDist, deltaDist, position, step);
            Chunk currentChunk = getCurrentChunk(position, voxels);
            if (currentChunk != null) {
                currentID = currentChunk.getVoxelID(currentVoxelPos.y, currentVoxelPos.x, currentVoxelPos.z);
                if (currentID == 0) {
                    previousChunkPos.set(currentChunkPos);
                    previousVoxelPos.set(currentVoxelPos);
                    traversedEmpty = true;
                }
            }
        }
    }

    private Vector3i createPositionVector(Vector3f startPos) {
        return new Vector3i(
                (startPos.x < 0.0f ? (int) Math.floor(startPos.x) : (int) (startPos.x)),
                (startPos.y < 0.0f ? (int) Math.floor(startPos.y) : (int) (startPos.y)),
                (startPos.z < 0.0f ? (int) Math.floor(startPos.z) : (int) (startPos.z)));
    }

    private Vector3i createStepVector(Vector3f cameraDir) {
        return new Vector3i(
                Float.compare(cameraDir.x, 0.0f),
                Float.compare(cameraDir.y, 0.0f),
                Float.compare(cameraDir.z, 0.0f));
    }

    private Vector3f createDeltaVector(Vector3i step, Vector3f cameraDir) {
        return new Vector3f(
                (step.x == 0 ? Float.MAX_VALUE : Math.min(step.x / cameraDir.x, Float.MAX_VALUE)),
                (step.y == 0 ? Float.MAX_VALUE : Math.min(step.y / cameraDir.y, Float.MAX_VALUE)),
                (step.z == 0 ? Float.MAX_VALUE : Math.min(step.z / cameraDir.z, Float.MAX_VALUE)));
    }

    private Vector3f createSideVector(Vector3i step, Vector3f deltaDist, Vector3f startPos) {
        return new Vector3f(
                (step.x > 0 ? deltaDist.x * (1.0f - fraction(startPos.x)) : deltaDist.x * fraction(startPos.x)),
                (step.y > 0 ? deltaDist.y * (1.0f - fraction(startPos.y)) : deltaDist.y * fraction(startPos.y)),
                (step.z > 0 ? deltaDist.z * (1.0f - fraction(startPos.z)) : deltaDist.z * fraction(startPos.z)));
    }

    private float fraction(float number) {
        return number - (float) Math.floor(number);
    }

    private void advanceRay(Vector3f sideDist, Vector3f deltaDist, Vector3i position, Vector3i step) {
        if (sideDist.x < sideDist.y && sideDist.x < sideDist.z) {
            sideDist.x += deltaDist.x;
            position.x += step.x;
        } else if (sideDist.y < sideDist.z) {
            sideDist.y += deltaDist.y;
            position.y += step.y;
        } else {
            sideDist.z += deltaDist.z;
            position.z += step.z;
        }
    }

    private Chunk getCurrentChunk(Vector3i position, SpatialStructure voxels) {
        final int chunkSize = Chunk.getSize();
        currentVoxelPos.x = Math.floorMod(position.x, chunkSize);
        currentVoxelPos.y = Math.floorMod(position.y, chunkSize);
        currentVoxelPos.z = Math.floorMod(position.z, chunkSize);
        currentChunkPos.x = (position.x >= 0 ? position.x / chunkSize : (position.x - chunkSize + 1) / chunkSize);
        currentChunkPos.y = (position.y >= 0 ? position.y / chunkSize : (position.y - chunkSize + 1) / chunkSize);
        currentChunkPos.z = (position.z >= 0 ? position.z / chunkSize : (position.z - chunkSize + 1) / chunkSize);
        return voxels.getChunk(currentChunkPos.y, currentChunkPos.x, currentChunkPos.z);
    }

    public byte deleteCurrentBlock(SpatialStructure voxels) {
        byte deletedID = 0;
        if ((voxels != null) && (currentID != 0)) {
            Chunk toDelete = voxels.getChunk(currentChunkPos.y, currentChunkPos.x, currentChunkPos.z);
            toDelete.setVoxelID(currentVoxelPos.y, currentVoxelPos.x, currentVoxelPos.z, (byte) 0);
            deletedID = currentID;
            currentID = 0;
        }
        return deletedID;
    }

    public void placeNewBlock(SpatialStructure voxels) {
        if ((voxels != null) && (currentID != 0) && traversedEmpty) {
            Chunk toPlace = voxels.getChunk(previousChunkPos.y, previousChunkPos.x, previousChunkPos.z);
            toPlace.setVoxelID(previousVoxelPos.y, previousVoxelPos.x, previousVoxelPos.z, (byte) 1); //TODO material change
            currentID = 0;
            traversedEmpty = true;
        }
    }

    public void draw(Renderer renderer) {
        if (currentID != 0) {
            indicationBox.setPosition(
                    currentChunkPos.x * Chunk.getSize() + currentVoxelPos.x,
                    currentChunkPos.y * Chunk.getSize() + currentVoxelPos.y,
                    currentChunkPos.z * Chunk.getSize() + currentVoxelPos.z);
            renderer.drawWireframe(indicationBox);
        }
    }
}
