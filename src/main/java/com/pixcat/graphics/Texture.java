package com.pixcat.graphics;

import java.nio.ByteBuffer;

import static org.lwjgl.opengl.GL33.*;

public class Texture {
    private int ID;
    private int height;
    private int width;
    private int referenceCount;

    private Texture(int ID, int width, int height) {
        this.ID = ID;
        this.width = width;
        this.height = height;
    }

    public static Texture createFromBytes(ByteBuffer buffer, int width, int height) {
        int textureID = glGenTextures();
        glBindTexture(GL_TEXTURE_2D, textureID);
        final int componentSize = 1; //1 byte per channel
        glPixelStorei(GL_UNPACK_ALIGNMENT, componentSize);
        glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MIN_FILTER, GL_NEAREST);
        glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MAG_FILTER, GL_NEAREST);
        glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_WRAP_S, GL_REPEAT);
        glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_WRAP_T, GL_REPEAT);
        glTexImage2D(GL_TEXTURE_2D, 0, GL_RGBA, width, height, 0, GL_RGBA, GL_UNSIGNED_BYTE, buffer);
        return new Texture(textureID, width, height);
    }

    public int getReferenceCount() {
        return referenceCount;
    }

    public void addReference() {
        ++referenceCount;
    }

    public int getWidth() {
        return width;
    }

    public int getHeight() {
        return height;
    }

    public int getID() {
        return ID;
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
        if (referenceCount > 0)
            --referenceCount;
        if (referenceCount == 0) {
            glDeleteTextures(ID);
            ID = 0;
        }
    }
}
