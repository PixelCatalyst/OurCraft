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
    public Block getVoxelFromID(byte ID) { //TODO possible unification with getVoxelFromName
        Iterator<Block> iterator = voxelTypes.iterator();
        for (int i = 0; i < voxelTypes.size(); ++i) {
            Block currentBlock = iterator.next();
            if (currentBlock.getID() == ID)
                return currentBlock;
        }
        return null;
    }

    @Override
    public Block getVoxelFromName(String name) {
        Iterator<Block> iterator = voxelTypes.iterator();
        for (int i = 0; i < voxelTypes.size(); ++i) {
            Block currentBlock = iterator.next();
            if (currentBlock.getName().equals(name))
                return currentBlock;
        }
        return null;
    }

    @Override
    public Chunk getChunk(int x, int y, int z) {
        return chunks.get(new Coord3Int(x, y, z));
    }

    @Override
    public void putChunk(int x, int y, int z, Chunk toPut) {
        chunks.put(new Coord3Int(x, y, z), toPut);
        //TODO unloading excessive chunks
    }

    @Override
    public int getDiameter() {
        return diameter;
    }
}
