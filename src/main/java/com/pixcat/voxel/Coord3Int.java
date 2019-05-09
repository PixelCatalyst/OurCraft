package com.pixcat.voxel;

import java.util.Objects;

public class Coord3Int {
    public final int x;
    public final int y;
    public final int z;

    public Coord3Int(int x, int y, int z) {
        this.x = x;
        this.y = y;
        this.z = z;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o)
            return true;
        if (o == null || getClass() != o.getClass())
            return false;
        Coord3Int that = (Coord3Int) o;
        return (x == that.x) && (y == that.y) && (z == that.z);
    }

    @Override
    public int hashCode() {
        return Objects.hash(x, y, z);
    }
}
