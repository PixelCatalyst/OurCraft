package com.pixcat.graphics;

import org.joml.Matrix4f;
import org.joml.Vector3f;
import org.joml.Vector4f;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;
import static org.hamcrest.CoreMatchers.*;

public class TransformationTest {
    Transformation transformation;

    @Before
    public void beforeEach() {
        transformation = new Transformation();
    }

    @Test
    public void testPerspectiveProjection() {
        float fieldOfView = (float) Math.toRadians(60.0);
        float nearClippingDist = 0.01f;
        float farClippingDist = 1000.0f;

        Matrix4f projection = transformation.getPerspectiveProjection(fieldOfView, 16/9, nearClippingDist, farClippingDist);
        assertEquals(nearClippingDist, projection.perspectiveNear(), 0.1f);
        assertEquals(farClippingDist, projection.perspectiveFar(), 2.0f);
        assertEquals(fieldOfView, projection.perspectiveFov(), 0.1f);
    }

    @Test
    public void testOrthographicProjection() {
        float width = 100;
        float height = 100;

        Matrix4f projection = transformation.getOrthographicProjection(width, height);
        Matrix4f expected = new Matrix4f().identity().ortho2D(0.0f, width, height, 0.0f);
        assertTrue(projection.equals(expected));
    }


    @Test
    public void testGetModelTrans() {
        Matrix4f A = new Matrix4f(
                1.0f, 2.0f, 3.0f, 10.0f,
                4.0f, 5.0f, 6.0f, 10.0f,
                7.0f, 8.0f, 9.0f, 10.0f,
                10.0f, 11.0f, 12.0f, 10.0f
        );

        Matrix4f B = new Matrix4f(
                2.0f, 2.0f, 2.0f, 2.0f,
                3.0f, 3.0f, 3.0f, 3.0f,
                4.0f, 4.0f, 4.0f, 4.0f,
                5.0f, 5.0f, 5.0f, 5.0f
        );

        Matrix4f matrix = transformation.getModelTrans(A, B);
        Matrix4f expected = new Matrix4f().identity().mul(A).mul(B);

        assertTrue(matrix.equals(expected));
    }
}
