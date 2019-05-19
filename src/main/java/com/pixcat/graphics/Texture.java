package com.pixcat.graphics;

import java.nio.ByteBuffer;

import static org.lwjgl.opengl.GL33.*;

public class Texture {
    private int ID;
    private int height;
    private int width;

    public Texture(int ID, int width, int height) {
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

    public int getWidth() {
        return width;
    }

    public int getHeight() {
        return height;
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
