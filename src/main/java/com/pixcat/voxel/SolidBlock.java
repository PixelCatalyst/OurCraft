package com.pixcat.voxel;

import com.pixcat.graphics.Texture;

import java.util.Objects;

public class SolidBlock implements Block {
    private byte ID;
    private String name;
    protected Texture texture;

    public SolidBlock(byte ID, String name, Texture texture) {
        if (ID == 0)
            throw new IllegalArgumentException("Block with ID=0 is reserved");
        this.ID = ID;
        if (texture == null)
            throw new IllegalArgumentException("Block must have non null texture");
        this.texture = texture;
        texture.addReference();
        if (name == null)
            this.name = "unknown";
        else
            this.name = name;
    }

    @Override
    public byte getID() {
        return ID;
    }

    @Override
    public String getName() {
        return name;
    }

    public Texture getTexture() {
        return texture;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o)
            return true;
        if (o == null || getClass() != o.getClass())
            return false;
        SolidBlock that = (SolidBlock) o;
        return (ID == that.ID) || Objects.equals(name, that.name);
    }

    @Override
    public int hashCode() {
        return Objects.hash(ID);
    }

    @Override
    public void cleanup() {
        texture.cleanup();
    }
}
