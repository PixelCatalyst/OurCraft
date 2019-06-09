package com.pixcat.mesh;

import java.nio.FloatBuffer;
import java.nio.IntBuffer;

import org.lwjgl.system.MemoryUtil;

import static org.lwjgl.opengl.GL33.*;

public class Mesh {
    private int vaoID;
    private final int vertexVboID;
    private final int texCoordVboID;
    private final int indexVboID;
    private final int vertexCount;
    private int referenceCount;

    public Mesh(float[] positions, float[] texCoords, int[] indices) {
        FloatBuffer vertexBuffer = null;
        FloatBuffer texCoordsBuffer = null;
        IntBuffer indexBuffer = null;
        try {
            vertexBuffer = MemoryUtil.memAllocFloat(positions.length);
            vertexBuffer.put(positions).flip();
            texCoordsBuffer = MemoryUtil.memAllocFloat(texCoords.length);
            texCoordsBuffer.put(texCoords).flip();
            indexBuffer = MemoryUtil.memAllocInt(indices.length);
            indexBuffer.put(indices).flip();
            vertexCount = indices.length;

            vaoID = glGenVertexArrays();
            glBindVertexArray(vaoID);
            vertexVboID = createFloatVBO(vertexBuffer, 0, 3);
            texCoordVboID = createFloatVBO(texCoordsBuffer, 1, 2);
            indexVboID = createIndexVBO(indexBuffer);
            glBindVertexArray(0);
        } finally {
            if (vertexBuffer != null)
                MemoryUtil.memFree(vertexBuffer);
            if (texCoords != null)
                MemoryUtil.memFree(texCoordsBuffer);
            if (indexBuffer != null)
                MemoryUtil.memFree(indexBuffer);
        }
    }

    private int createFloatVBO(FloatBuffer buffer, int index, int elementSize) {
        int vboID = glGenBuffers();
        glBindBuffer(GL_ARRAY_BUFFER, vboID);
        glBufferData(GL_ARRAY_BUFFER, buffer, GL_STATIC_DRAW);
        glVertexAttribPointer(index, elementSize, GL_FLOAT, false, 0, 0);
        glBindBuffer(GL_ARRAY_BUFFER, 0);
        return vboID;
    }

    private int createIndexVBO(IntBuffer indexBuffer) {
        int vboID = glGenBuffers();
        glBindBuffer(GL_ELEMENT_ARRAY_BUFFER, vboID);
        glBufferData(GL_ELEMENT_ARRAY_BUFFER, indexBuffer, GL_STATIC_DRAW);
        return vboID;
    }

    public void addReference() {
        ++referenceCount;
    }

    public int getVaoID() {
        return vaoID;
    }

    public int getVertexCount() {
        return vertexCount;
    }

    public void cleanup() {
        if (referenceCount > 0)
            --referenceCount;
        if (referenceCount == 0) {
            deleteVBO();
            deleteVAO();
        }
    }

    public int getReferenceCount() {
        return referenceCount;
    }

    private void deleteVBO() {
        glDisableVertexAttribArray(0);
        glBindBuffer(GL_ARRAY_BUFFER, 0);
        glDeleteBuffers(vertexVboID);
        glDeleteBuffers(texCoordVboID);
        glDeleteBuffers(indexVboID);
    }

    private void deleteVAO() {
        glBindVertexArray(0);
        glDeleteVertexArrays(vaoID);
        vaoID = 0;
    }
}
