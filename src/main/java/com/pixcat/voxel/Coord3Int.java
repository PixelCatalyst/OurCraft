package com.pixcat.voxel;

import java.util.Objects;

public final class Coord3Int {
    public final int y;
    public final int x;
    public final int z;

    public Coord3Int(int y, int x, int z) {
        this.y = y;
        this.x = x;
        this.z = z;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o)
            return true;
        if (o == null || getClass() != o.getClass())
            return false;
        Coord3Int that = (Coord3Int) o;
        return (y == that.y) && (x == that.x) && (z == that.z);
    }

    @Override
    public int hashCode() {
        return Objects.hash(y, x, z);
    }
}
