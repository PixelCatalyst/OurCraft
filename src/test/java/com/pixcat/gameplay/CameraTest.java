package com.pixcat.gameplay;

import com.pixcat.voxel.ArrayChunk;
import com.pixcat.voxel.Chunk;
import com.pixcat.voxel.SpatialStructure;
import com.pixcat.voxel.VirtualArray;
import org.joml.Matrix4f;
import org.joml.Vector3f;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Arrays;

import static org.junit.Assert.*;

public class CameraTest {
    private Camera testCamera;

    @Before
    public void setUp() {
        testCamera = new Camera(0.0f, 0.0f, 0.0f);
    }

    @Test
    public void testFieldOfViewInGoodRange() {
        float FOV = testCamera.getFieldOfView();

        assertTrue(FOV > 1.0f && FOV < 360.0f);
    }

    @Test
    public void testNearClippingInGoodRange() {
        float nearClippingDist = testCamera.getNearClippingDist();

        assertTrue(nearClippingDist > 0.0f && nearClippingDist <= 1.0f);
    }

    @Test
    public void testFarClippingInGoodRange() {
        float farClippingDist = testCamera.getFarClippingDist();

        assertTrue(farClippingDist > 100.0f && farClippingDist < Float.POSITIVE_INFINITY);
    }

    @Test
    public void testGetPosition() {
        Vector3f position = testCamera.getPosition();

        // Verify the results
        assertNotNull(position);
        assertEquals(0.0f, position.x, Float.MIN_NORMAL);
        assertEquals(0.0f, position.y, Float.MIN_NORMAL);
        assertEquals(0.0f, position.z, Float.MIN_NORMAL);
        position.set(100.0f, 100.0f, 100.0f);

        position = testCamera.getPosition();
        assertEquals(0.0f, position.x, Float.MIN_NORMAL);
        assertEquals(0.0f, position.y, Float.MIN_NORMAL);
        assertEquals(0.0f, position.z, Float.MIN_NORMAL);
    }

    @Test
    public void testSetPosition() {
        testCamera.setPosition(13.0f, 14.0f, -90.0f);
        Vector3f position = testCamera.getPosition();

        assertEquals(13.0f, position.x, Float.MIN_NORMAL);
        assertEquals(14.0f, position.y, Float.MIN_NORMAL);
        assertEquals(-90.0f, position.z, Float.MIN_NORMAL);
    }

    @Test
    public void testMovePosition() {
        testCamera.movePosition(0.0f, 0.0f, 0.0f);

        Vector3f position = testCamera.getPosition();
        assertEquals(0.0f, position.x, Float.MIN_NORMAL);
        assertEquals(0.0f, position.y, Float.MIN_NORMAL);
        assertEquals(0.0f, position.z, Float.MIN_NORMAL);

        testCamera.movePosition(100.0f, 100.0f, 100.0f);
        position = testCamera.getPosition();
        assertEquals(100.0f, position.x, Float.MIN_NORMAL);
        assertEquals(100.0f, position.y, Float.MIN_NORMAL);
        assertEquals(100.0f, position.z, Float.MIN_NORMAL);
    }

    @Test
    public void testGetDirection() {
        final Vector3f direction = testCamera.getDirection();

        assertEquals(0.0f, direction.x, Float.MIN_NORMAL);
        assertEquals(0.0f, direction.y, Float.MIN_NORMAL);
        assertEquals(-1.0f, direction.z, Float.MIN_NORMAL);
    }

    @Test
    public void testGetRotation() {
        final Vector3f rotation = testCamera.getRotation();

        assertEquals(0.0f, rotation.x, Float.MIN_NORMAL);
        assertEquals(0.0f, rotation.y, Float.MIN_NORMAL);
        assertEquals(0.0f, rotation.z, Float.MIN_NORMAL);
    }

    @Test
    public void testSetRotation() {
        testCamera.setRotation(45.0f, 12.0f, 123.0f);

        final Vector3f rotation = testCamera.getRotation();

        assertEquals(45.0f, rotation.x, Float.MIN_NORMAL);
        assertEquals(12.0f, rotation.y, Float.MIN_NORMAL);
        assertEquals(123.0f, rotation.z, Float.MIN_NORMAL);
    }

    @Test
    public void testMoveRotation() {
        testCamera.moveRotation(45.0f, 90.0f, -1.0f);

        Vector3f rotation = testCamera.getRotation();

        //Moved from (0,0,0), so equal to setRotation
        assertEquals(45.0f, rotation.x, Float.MIN_NORMAL);
        assertEquals(90.0f, rotation.y, Float.MIN_NORMAL);
        assertEquals(-1.0f, rotation.z, Float.MIN_NORMAL);

        testCamera.moveRotation(190.0f, 90.0f, 5.0f);
        rotation = testCamera.getRotation();

        //Expecting max x-angle to be 90 deg
        assertEquals(90.0f, rotation.x, Float.MIN_NORMAL);
        assertEquals(180.0f, rotation.y, Float.MIN_NORMAL);
        assertEquals(4.0f, rotation.z, Float.MIN_NORMAL);

        //Again with negative x-angle
        testCamera.moveRotation(-999999.0f, 0.0f, 0.0f);
        assertEquals(90.0f, rotation.x, Float.MIN_NORMAL);
        assertEquals(180.0f, rotation.y, Float.MIN_NORMAL);
        assertEquals(4.0f, rotation.z, Float.MIN_NORMAL);
    }

    @Test
    public void testGetVisibleChunks() {
        final ArrayList<Chunk> chunks = testCamera.getVisibleChunks(null);

        assertNotNull(chunks);
        assertEquals(0, chunks.size());
    }

    @Test
    public void testGetVisibleChunksWithPreSetup() {
        SpatialStructure voxels = new VirtualArray(4);
        Chunk testChunk = new ArrayChunk();
        voxels.putChunk(0, 0, 0, testChunk);
        ArrayList<Chunk> chunks = testCamera.getVisibleChunks(voxels);

        assertNotNull(chunks);
        assertEquals(1, chunks.size());
        assertEquals(testChunk, chunks.get(0));

        voxels.putChunk(0, 0, 1, testChunk);
        chunks = testCamera.getVisibleChunks(voxels);

        assertNotNull(chunks);
        assertEquals(2, chunks.size());
        assertEquals(testChunk, chunks.get(0));
        assertEquals(testChunk, chunks.get(1));
    }

    @Test
    public void testGetViewMatrix() {
        final Matrix4f matrix = testCamera.getViewMatrix();

        assertNotNull(matrix);
        assertEquals(1.0f, matrix.m00(), 0.000001f);
        assertEquals(1.0f, matrix.m11(), 0.000001f);
        assertEquals(1.0f, matrix.m22(), 0.000001f);
    }
}
