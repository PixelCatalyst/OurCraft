package com.pixcat.graphics;

import static org.lwjgl.opengl.GL33.*;

public class Texture {
    private int ID;

    public Texture(int ID) {
        this.ID = ID;
    }

    public void bind() {
        glActiveTexture(GL_TEXTURE0);
        glBindTexture(GL_TEXTURE_2D, ID);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o)
            return true;
        if (o == null || getClass() != o.getClass())
            return false;
        Texture that = (Texture) o;
        return this.ID == that.ID;
    }

    public int compareID(Texture other) {
        return this.ID - other.ID;
    }

    public void cleanup() {
        glDeleteTextures(ID);
        ID = 0;
    }
}
