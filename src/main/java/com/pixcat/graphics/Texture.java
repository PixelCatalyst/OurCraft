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

    public void cleanup() {
        glDeleteTextures(ID);
        ID = 0;
    }
}
