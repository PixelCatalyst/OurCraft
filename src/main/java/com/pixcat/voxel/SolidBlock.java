package com.pixcat.voxel;

import java.util.Objects;

public class SolidBlock implements Block {
    private byte id;
    private String name;

    public SolidBlock(byte id, String name) {
        if (id == 0)
            throw new IllegalArgumentException("Block with ID=0 is reserved");
        this.id = id;
        if (name == null)
            this.name = "unknown";
        else
            this.name = name;
    }

    @Override
    public byte getID() {
        return id;
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o)
            return true;
        if (o == null || getClass() != o.getClass())
            return false;
        SolidBlock that = (SolidBlock) o;
        return (id == that.id) || Objects.equals(name, that.name);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}
