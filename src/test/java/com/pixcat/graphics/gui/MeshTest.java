package com.pixcat.graphics.gui;
import static org.junit.Assert.*;
import static org.lwjgl.glfw.GLFW.glfwInit;

import com.pixcat.core.FileManager;
import com.pixcat.graphics.Texture;
import com.pixcat.graphics.Window;
import com.pixcat.mesh.Mesh;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

public class MeshTest {
    private Texture texture;
    private Window placeholderWindow;

    float[] positions = new float[]{
            0.0f, 0.0f, 0.0f,
            0.0f, 1.0f, 0.0f,
            1.0f, 1.0f, 0.0f,
            1.0f, 0.0f, 0.0f,
    };
    float[] texCoords = new float[]{
            0.0f, 0.0f,
            0.0f, 1.0f,
            1.0f, 1.0f,
            1.0f, 0.0f
    };
    int[] indices = new int[]{
            0, 1, 3, 3, 1, 2,
    };


    @Before
    public void setUp() {
        if (glfwInit() == false)
            throw new RuntimeException("Unable to initialize GLFW");
        placeholderWindow = new Window(100, 100, "placeholder");
        placeholderWindow.bindAsCurrent();
        texture = FileManager.getInstance().loadTexture("water.png");
    }

    @Test
    public void testMeshCreation(){
        Mesh modelMesh = new Mesh(positions, texCoords, indices);
        modelMesh.addReference();
        assertNotEquals(0, modelMesh.getVaoID());
        assertTrue(modelMesh.getReferenceCount() == 1);
    }

    @Test
    public void testMeshCleanup(){
        Mesh modelMesh = new Mesh(positions, texCoords, indices);
        modelMesh.addReference();
        modelMesh.cleanup();
        assertTrue(modelMesh.getVaoID() == 0);
        assertTrue(modelMesh.getReferenceCount() == 0);
    }

    @After
    public void tearDown() {
        placeholderWindow.destroy();
    }
}
