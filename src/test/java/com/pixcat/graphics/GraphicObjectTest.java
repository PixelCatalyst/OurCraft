package com.pixcat.graphics;

import com.pixcat.core.FileManager;
import com.pixcat.mesh.Mesh;
import org.joml.Matrix4f;
import org.joml.Vector3f;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;
import static org.lwjgl.glfw.GLFW.glfwInit;

public class GraphicObjectTest {
    private GraphicObject testObject;
    Window placeholderWindow;
    private Mesh placeholderMesh;

    @Before
    public void setUp() {
        if (glfwInit() == false)
            throw new RuntimeException("Unable to initialize GLFW");
        placeholderWindow = new Window(200, 200, "placeholder");
        placeholderWindow.bindAsCurrent();

        placeholderMesh = new Mesh(new float[6], new float[4], new int[10]);

        testObject = new GraphicObject(placeholderMesh);
    }

    @After
    public void destroyWindow() {
        placeholderWindow.destroy();
    }


    @Test(expected = IllegalArgumentException.class)
    public void testInvalidCreation() {
        GraphicObject invalid = new GraphicObject(null);
    }

    @Test
    public void testSetTexture() {
        testObject.setTexture(null);

        assertNull(testObject.getTexture());

        Texture testTexture = FileManager.getInstance().loadTexture("dirt.png");
        testObject.setTexture(testTexture);

        assertEquals(1, testTexture.getReferenceCount());
        assertEquals(testTexture, testObject.getTexture());
    }

    @Test
    public void testCompareTextures() {
        GraphicObject otherObject = new GraphicObject(placeholderMesh);
        Texture testTexture = FileManager.getInstance().loadTexture("dirt.png");
        otherObject.setTexture(testTexture);
        testObject.setTexture(testTexture);

        int comp = testObject.compareTextures(otherObject);

        assertEquals(0, comp);

        testObject.setTexture(null);
        comp = testObject.compareTextures(otherObject);

        assertEquals(0, comp);
    }

    @Test
    public void testGetPosition() {
        Vector3f position = testObject.getPosition();

        assertEquals(0.0f, position.x, Float.MIN_NORMAL);
        assertEquals(0.0f, position.y, Float.MIN_NORMAL);
        assertEquals(0.0f, position.z, Float.MIN_NORMAL);
    }

    @Test
    public void testSetPosition() {
        testObject.setPosition(101.f, -101.0f, 1231.3f);

        final Vector3f position = testObject.getPosition();

        assertEquals(101.0f, position.x, Float.MIN_NORMAL);
        assertEquals(-101.0f, position.y, Float.MIN_NORMAL);
        assertEquals(1231.3f, position.z, Float.MIN_NORMAL);
    }

    @Test
    public void testGetScale() {
        float scale = testObject.scale;

        assertEquals(1.0f, scale, Float.MIN_NORMAL);
    }

    @Test
    public void testSetScale() {
        testObject.setScale(4555.0f);
        float scale = testObject.scale;

        assertEquals(4555.0f, scale, Float.MIN_NORMAL);
    }

    @Test
    public void testGetWorldMatrix() {
        Matrix4f matrix = testObject.getWorldMatrix();

        assertNotNull(matrix);
        assertEquals(1.0f, matrix.m00(), 0.000001f);
        assertEquals(1.0f, matrix.m11(), 0.000001f);
        assertEquals(1.0f, matrix.m22(), 0.000001f);
    }

    @Test(expected = NullPointerException.class)
    public void testCleanup() {
        testObject.cleanup();

        assertNull(testObject.getTexture());
        assertNull(testObject.getMesh());
        assertNull(testObject.getWorldMatrix());

        assertNull(testObject.getPosition()); //throw null pointer exception
    }
}
