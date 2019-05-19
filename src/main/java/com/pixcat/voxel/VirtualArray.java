package com.pixcat.voxel;

import java.util.*;

public class VirtualArray implements SpatialStructure {
    private Set<Block> voxelTypes;
    private Map<Coord3Int, Chunk> chunks;
    private final int diameter;

    public VirtualArray(int diameter) {
        voxelTypes = new LinkedHashSet<>();
        chunks = new HashMap<>();
        this.diameter = diameter;
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
    public Chunk getChunk(int y, int x, int z) {
        return chunks.get(new Coord3Int(y, x, z));
    }

    @Override
    public void putChunk(int y, int x, int z, Chunk toPut) {
        chunks.put(new Coord3Int(y, x, z), toPut);
        //TODO unloading excessive chunks
    }

    @Override
    public int getDiameter() {
        return diameter;
    }
}
