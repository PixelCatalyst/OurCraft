package com.pixcat.graphics;

import org.joml.Matrix4f;

public class Transformation {
    private Matrix4f projection;
    private Matrix4f model;

    public Transformation() {
        projection = new Matrix4f();
        model = new Matrix4f();
    }

    public Matrix4f getPerspectiveProjection(float fieldOfView, float aspectRatio, float zNear, float zFar) {
        projection.identity();
        projection.perspective(fieldOfView, aspectRatio, zNear, zFar);
        return projection;
    }

    public Matrix4f getOrthographicProjection(float width, float height) {
        projection.identity();
        projection.setOrtho2D(0.0f, width, height, 0.0f);
        return projection;
    }

    public Matrix4f getModelTrans(Matrix4f projection, Matrix4f world) {
        model.identity();
        model.mul(projection).mul(world);
        return model;
    }
}
