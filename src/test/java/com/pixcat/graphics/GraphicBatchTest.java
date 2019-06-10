package com.pixcat.graphics;

import com.pixcat.mesh.Mesh;
import org.joml.Matrix4f;
import org.joml.Vector3f;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;
import static org.lwjgl.glfw.GLFW.glfwInit;

public class GraphicBatchTest {
    private GraphicBatch testBatch;

    private Window placeholderWindow;
    private Mesh placeholderMesh;

    @Before
    public void setUp() {
        testBatch = new GraphicBatch();

        if (glfwInit() == false)
            throw new RuntimeException("Unable to initialize GLFW");
        placeholderWindow = new Window(100, 100, "placeholder");
        placeholderWindow.bindAsCurrent();
        placeholderMesh = new Mesh(new float[6], new float[4], new int[10]);
    }

    @Test
    public void testEmptySize() {
        int size = testBatch.size();

        assertEquals(0, size);
    }

    @Test
    public void testNotEmptySize() {
        final int expectedSize = 100;
        for (int i = 0; i < expectedSize; ++i)
            testBatch.addObject(new GraphicObject(placeholderMesh));

        int size = testBatch.size();

        assertEquals(expectedSize, size);
    }

    @Test
    public void testAddObject() {
        testBatch.addObject(null);
        testBatch.addObject(null);
        testBatch.addObject(new GraphicObject(placeholderMesh));

        assertEquals(3, testBatch.size());
        assertNull(testBatch.getObject(0));
        assertNull(testBatch.getObject(1));
        assertNotNull(testBatch.getObject(2));
    }

    @Test
    //Not possible to test every execution path, but application wouldn't render textures if bindTexture() is faulty
    //-> verified mostly through system tests
    public void testBakeTextures() {
        testBatch.bakeTextures();

        assertEquals(0, testBatch.getTextureChangesCount());
    }

    @Test
    //Testing beginIteration(), hasNext() and next() at the same time
    public void testIteration() {
        final int expectedSize = 100;
        for (int i = 0; i < expectedSize; ++i)
            testBatch.addObject(new GraphicObject(placeholderMesh));

        int actualSize = 0;
        testBatch.beginIteration();
        while (testBatch.hasNext()) {

            assertNotNull(testBatch.getObject(actualSize));

            ++actualSize;
            testBatch.next();
        }

        assertEquals(expectedSize, actualSize);
    }

    @Test
    public void testHasNext() {
        //Iteration wasn't started
        assertFalse(testBatch.hasNext());
    }

    @Test(expected = ArrayIndexOutOfBoundsException.class)
    public void testNext() {
        testBatch.next();
        //Should throw, because iteration not started
        testBatch.getMesh();
    }

    @Test(expected = IndexOutOfBoundsException.class)
    public void testGetObject() {
        testBatch.getObject();
    }

    @Test(expected = IndexOutOfBoundsException.class)
    public void testGetObjectIndexed() {
        testBatch.getObject(0);
    }

    @Test
    public void testGetMesh() {
        final int expectedSize = 100;
        for (int i = 0; i < expectedSize; ++i)
            testBatch.addObject(new GraphicObject(placeholderMesh));

        while (testBatch.hasNext()) {
            Mesh mesh = testBatch.getMesh();

            assertEquals(placeholderMesh, mesh);

            testBatch.next();
        }

        assertEquals(expectedSize, testBatch.size());
    }

    @Test
    //Not possible to test every execution path, but application wouldn't render textures if bindTexture() is faulty
    //-> verified mostly through system tests
    public void testBindTexture() {
        testBatch.bindTexture();
    }

    @Test
    public void testGetPosition() {
        final Vector3f position = testBatch.getPosition();

        assertEquals(0.0f, position.x, Float.MIN_NORMAL);
        assertEquals(0.0f, position.y, Float.MIN_NORMAL);
        assertEquals(0.0f, position.z, Float.MIN_NORMAL);
    }

    @Test
    public void testSetPosition() {
        testBatch.setPosition(10.f, -10.0f, 123.3f);

        final Vector3f position = testBatch.getPosition();

        assertEquals(10.0f, position.x, Float.MIN_NORMAL);
        assertEquals(-10.0f, position.y, Float.MIN_NORMAL);
        assertEquals(123.3f, position.z, Float.MIN_NORMAL);
    }

    @Test(expected = IndexOutOfBoundsException.class)
    public void testGetWorldMatrixEmpty() {
        testBatch.getWorldMatrix();
    }

    @Test(expected = IndexOutOfBoundsException.class)
    public void testGetWorldMatrixEmptyWithBegin() {
        testBatch.beginIteration();
        testBatch.getWorldMatrix();
    }

    @Test
    public void testGetWorldMatrix() {
        testBatch.addObject(new GraphicObject(placeholderMesh));
        testBatch.beginIteration();
        Matrix4f matrix = testBatch.getWorldMatrix();

        assertNotNull(matrix);
        assertEquals(1.0f, matrix.m00(), 0.000001f);
        assertEquals(1.0f, matrix.m11(), 0.000001f);
        assertEquals(1.0f, matrix.m22(), 0.000001f);
    }

    @Test
    public void testCleanup() {
        final int expectedSize = 100;
        for (int i = 0; i < expectedSize; ++i) {
            testBatch.addObject(new GraphicObject(placeholderMesh));
            testBatch.addObject(null);
        }

        assertEquals(expectedSize * 2, testBatch.size());
        testBatch.cleanup();
        assertEquals(0, testBatch.size());
        assertFalse(testBatch.hasNext());
    }
}
