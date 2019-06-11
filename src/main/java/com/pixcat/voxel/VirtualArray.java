package com.pixcat.voxel;

import com.pixcat.core.FileManager;

import java.util.*;

public class VirtualArray implements SpatialStructure {
    private Set<Block> voxelTypes;
    private Map<Coord3Int, Chunk> chunks;
    private final int diameter;
    private final int height;

    public VirtualArray(int diameter) {
        voxelTypes = new LinkedHashSet<>();
        chunks = new HashMap<>();
        this.diameter = diameter;
        this.height = 8;
    }

    public void getAll(ArrayList<Chunk> visible) {
        for (Map.Entry<Coord3Int, Chunk> pair : chunks.entrySet()) {
            visible.add(pair.getValue());
        }
    }

    @Override
    public void addVoxelType(Block block) {
        if (block != null)
            voxelTypes.add(block);
    }

    @Override
    public Block getVoxelFromID(byte ID) {
        Iterator<Block> iterator = voxelTypes.iterator();
        for (int i = 0; i < voxelTypes.size(); ++i) {
            Block currentBlock = iterator.next();
            if (currentBlock.getID() == ID)
                return currentBlock;
        }
        return null;
    }

    @Override
    public byte getTypeID(int index) {
        byte typeID = 0;
        if ((index >= 0) && (index < getTypeCount())) {
            Iterator<Block> it = voxelTypes.iterator();
            for (int i = 0; i <= index; ++i)
                typeID = it.next().getID();
        }
        return typeID;
    }

    @Override
    public int getTypeCount() {
        return voxelTypes.size();
    }


    @Override
    public void updateBlocks(double elapsedTime) {
        Iterator<Block> iterator = voxelTypes.iterator();
        for (int i = 0; i < voxelTypes.size(); ++i)
            iterator.next().update(elapsedTime);
    }

    @Override
    public void updateChunk(Coord3Int chunkCoord, int voxelY, int voxelX, int voxelZ) {
        Chunk updatedChunk = chunks.get(chunkCoord);
        if (updatedChunk != null) {
            updatedChunk.scheduleForRebuild();
            FileManager.getInstance().serializeChunkToDisk(updatedChunk);

            final int maxIndex = Chunk.getSize() - 1;
            if (voxelY == 0)
                scheduleRebuild(chunkCoord.y - 1, chunkCoord.x, chunkCoord.z);
            else if (voxelY == maxIndex)
                scheduleRebuild(chunkCoord.y + 1, chunkCoord.x, chunkCoord.z);

            if (voxelX == 0)
                scheduleRebuild(chunkCoord.y, chunkCoord.x - 1, chunkCoord.z);
            else if (voxelX == maxIndex)
                scheduleRebuild(chunkCoord.y, chunkCoord.x + 1, chunkCoord.z);

            if (voxelZ == 0)
                scheduleRebuild(chunkCoord.y, chunkCoord.x, chunkCoord.z - 1);
            else if (voxelZ == maxIndex)
                scheduleRebuild(chunkCoord.y, chunkCoord.x, chunkCoord.z + 1);
        }
    }

    private void scheduleRebuild(int chunkY, int chunkX, int chunkZ) {
        Chunk toRebuild = getChunk(chunkY, chunkX, chunkZ);
        if (toRebuild != null)
            toRebuild.scheduleForRebuild();
    }

    @Override
    public Chunk getChunk(int y, int x, int z) {
        return chunks.get(new Coord3Int(y, x, z));
    }

    @Override
    public void putChunk(int y, int x, int z, Chunk toPut) {
        Coord3Int worldPos = new Coord3Int(y, x, z);
        toPut.setWorldPosition(worldPos);
        chunks.put(worldPos, toPut);
        removeExcessNeighbor(x, z);
    }

    private void removeExcessNeighbor(int x, int z) {
        removeExcessColumn(x - diameter, z);
        removeExcessColumn(x + diameter, z);

        removeExcessColumn(x - diameter, z - diameter);
        removeExcessColumn(x - diameter, z + diameter);

        removeExcessColumn(x + diameter, z - diameter);
        removeExcessColumn(x + diameter, z + diameter);

        removeExcessColumn(x, z - diameter);
        removeExcessColumn(x, z + diameter);
    }

    private void removeExcessColumn(int x, int z) {
        for (int y = 0; y < height; ++y) {
            Chunk removed = chunks.remove(new Coord3Int(y, x, z));
            if (removed != null) {
                removed.cleanup();
            }
        }
    }

    @Override
    public int getDiameter() {
        return diameter;
    }

    public int getHeight() {
        return height;
    }

    @Override
    public void cleanup() {
        for (Map.Entry<Coord3Int, Chunk> pair : chunks.entrySet())
            pair.getValue().cleanup();
        for (int i = 0; i < voxelTypes.size(); ++i) {
            byte typeID = getTypeID(i);
            Block block = getVoxelFromID(typeID);
            block.cleanup();
        }
    }
}
