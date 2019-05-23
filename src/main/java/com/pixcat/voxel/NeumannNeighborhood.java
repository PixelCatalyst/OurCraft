package com.pixcat.voxel;

public class NeumannNeighborhood {
    final public Chunk central;
    final public Chunk bottom;
    final public Chunk top;
    final public Chunk west;
    final public Chunk east;
    final public Chunk south;
    final public Chunk north;

    public NeumannNeighborhood(SpatialStructure voxels, Coord3Int position) {
        this.central = voxels.getChunk(position.y, position.x, position.z);
        this.bottom = voxels.getChunk(position.y - 1, position.x, position.z);
        this.top = voxels.getChunk(position.y + 1, position.x, position.z);
        this.west = voxels.getChunk(position.y, position.x - 1, position.z);
        this.east = voxels.getChunk(position.y, position.x + 1, position.z);
        this.south = voxels.getChunk(position.y, position.x, position.z - 1);
        this.north = voxels.getChunk(position.y, position.x, position.z + 1);
    }
}
