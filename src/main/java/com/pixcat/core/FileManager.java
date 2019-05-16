package com.pixcat.core;

import com.pixcat.graphics.Texture;
import org.lwjgl.system.MemoryStack;

import java.io.FileReader;
import java.io.BufferedReader;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.IntBuffer;

import static org.lwjgl.opengl.GL33.*;
import static org.lwjgl.stb.STBImage.*;

public class FileManager {
    private static FileManager instance = null;

    private String rootDir;

    private FileManager() {
        rootDir = "resources\\";
    }

    public static FileManager getInstance() {
        if (instance == null) {
            synchronized (FileManager.class) {
                if (instance == null)
                    instance = new FileManager();
            }
        }
        return instance;
    }

    public String loadText(String fileName) {
        StringBuilder builder = new StringBuilder();
        try {
            FileReader reader = new FileReader(rootDir + fileName);
            BufferedReader buffer = new BufferedReader(reader);
            String currentLine = buffer.readLine();
            while (currentLine != null) {
                builder.append(currentLine);
                builder.append("\n");
                currentLine = buffer.readLine();
            }
        } catch (IOException exp) {
            System.err.println(exp.getMessage());
        }
        return builder.toString();
    }

    public Texture loadTexture(String fileName) throws IllegalStateException {
        int width;
        int height;
        ByteBuffer buffer;
        try (MemoryStack stack = MemoryStack.stackPush()) {
            String filePath = System.getProperty("user.dir") + "\\" + rootDir + fileName;
            IntBuffer widthBuffer = stack.mallocInt(1);
            IntBuffer heightBuffer = stack.mallocInt(1);
            IntBuffer channels = stack.mallocInt(1);
            final int channelCount = 4;
            buffer = stbi_load(filePath, widthBuffer, heightBuffer, channels, channelCount);
            if (buffer == null)
                throw new IllegalStateException("Image file" + fileName + "not loaded: " + stbi_failure_reason());
            width = widthBuffer.get();
            height = heightBuffer.get();
        }
        int textureID = createTextureFromBytes(buffer, width, height);
        stbi_image_free(buffer);
        return new Texture(textureID);
    }

    private int createTextureFromBytes(ByteBuffer buffer, int width, int height) {
        int textureID = glGenTextures();
        glBindTexture(GL_TEXTURE_2D, textureID);
        final int componentSize = 1; //1 byte per channel
        glPixelStorei(GL_UNPACK_ALIGNMENT, componentSize);
        glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MIN_FILTER, GL_NEAREST);
        glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MAG_FILTER, GL_NEAREST);
        glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_WRAP_S, GL_REPEAT);
        glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_WRAP_T, GL_REPEAT);
        glTexImage2D(GL_TEXTURE_2D, 0, GL_RGBA, width, height, 0, GL_RGBA, GL_UNSIGNED_BYTE, buffer);
        return textureID;
    }
}
