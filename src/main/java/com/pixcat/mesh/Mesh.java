package com.pixcat.mesh;

import java.nio.FloatBuffer;
import java.nio.IntBuffer;

import org.lwjgl.system.MemoryUtil;

import static org.lwjgl.opengl.GL33.*;

public class Mesh {
    private final int vaoID;
    private final int vertexVboID;
    private final int indexVboID;
    private final int vertexCount;

    public Mesh(float[] positions, int[] indices) {
        FloatBuffer vertexBuffer = null;
        IntBuffer indexBuffer = null;
        try {
            vertexBuffer = MemoryUtil.memAllocFloat(positions.length);
            vertexBuffer.put(positions).flip();
            indexBuffer = MemoryUtil.memAllocInt(indices.length);
            indexBuffer.put(indices).flip();
            vertexCount = indices.length;

            vaoID = glGenVertexArrays();
            glBindVertexArray(vaoID);
            vertexVboID = createVertexVBO(vertexBuffer);
            indexVboID = createIndexVBO(indexBuffer);
            glBindVertexArray(0);
        } finally {
            if (vertexBuffer != null)
                MemoryUtil.memFree(vertexBuffer);
            if (indexBuffer != null)
                MemoryUtil.memFree(indexBuffer);
        }
    }

    private int createVertexVBO(FloatBuffer vertexBuffer) {
        int vboID = glGenBuffers();
        glBindBuffer(GL_ARRAY_BUFFER, vboID);
        glBufferData(GL_ARRAY_BUFFER, vertexBuffer, GL_STATIC_DRAW);
        glVertexAttribPointer(0, 3, GL_FLOAT, false, 0, 0);
        glBindBuffer(GL_ARRAY_BUFFER, 0);
        return vboID;
    }

    private int createIndexVBO(IntBuffer indexBuffer) {
        int vboID = glGenBuffers();
        glBindBuffer(GL_ELEMENT_ARRAY_BUFFER, vboID);
        glBufferData(GL_ELEMENT_ARRAY_BUFFER, indexBuffer, GL_STATIC_DRAW);
        return vboID;
    }

    public int getVaoID() {
        return vaoID;
    }

    public int getVertexCount() {
        return vertexCount;
    }

    public void cleanup() {
        deleteVBO();
        deleteVAO();
    }

    private void deleteVBO() {
        glDisableVertexAttribArray(0);
        glBindBuffer(GL_ARRAY_BUFFER, 0);
        glDeleteBuffers(vertexVboID);
        glDeleteBuffers(indexVboID);
    }

    private void deleteVAO() {
        glBindVertexArray(0);
        glDeleteVertexArrays(vaoID);
    }
}
