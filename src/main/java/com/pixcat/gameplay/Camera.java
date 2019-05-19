package com.pixcat.gameplay;

import org.joml.Vector3f;
import org.joml.Matrix4f;

public class Camera {
    private float fieldOfView;
    private float nearClippingDist;
    private float farClippingDist;

    private Vector3f position;
    private Vector3f rotation;
    private Matrix4f viewMatrix;

    public Camera() {
        fieldOfView = (float) Math.toRadians(60.0);
        nearClippingDist = 0.01f;
        farClippingDist = 1000.0f;

        position = new Vector3f(0.0f, 0.0f, 0.0f);
        rotation = new Vector3f(0.0f, 0.0f, 0.0f);
        viewMatrix = new Matrix4f();
    }

    public float getFieldOfView() {
        return fieldOfView;
    }

    public float getNearClippingDist() {
        return nearClippingDist;
    }

    public float getFarClippingDist() {
        return farClippingDist;
    }

    public Vector3f getPosition() {
        return position;
    }

    public void setPosition(float x, float y, float z) {
        position.x = x;
        position.y = y;
        position.z = z;
    }

    public void movePosition(float offsetX, float offsetY, float offsetZ) {
        if (offsetZ != 0) {
            position.x += (float) Math.sin(Math.toRadians(rotation.y)) * -1.0f * offsetZ;
            position.z += (float) Math.cos(Math.toRadians(rotation.y)) * offsetZ;
        }
        if (offsetX != 0) {
            position.x += (float) Math.sin(Math.toRadians(rotation.y - 90)) * -1.0f * offsetX;
            position.z += (float) Math.cos(Math.toRadians(rotation.y - 90)) * offsetX;
        }
        position.y += offsetY;
    }

    public Vector3f getRotation() {
        return rotation;
    }

    public void setRotation(float x, float y, float z) {
        rotation.x = x;
        rotation.y = y;
        rotation.z = z;
    }

    public void moveRotation(float offsetX, float offsetY, float offsetZ) {
        rotation.x += offsetX;
        rotation.y += offsetY;
        rotation.z += offsetZ;
    }

    public Matrix4f getViewMatrix() {
        viewMatrix
            .identity()
            .rotate((float) Math.toRadians(rotation.x), new Vector3f(1, 0, 0))
            .rotate((float) Math.toRadians(rotation.y), new Vector3f(0, 1, 0))
            .translate(-position.x, -position.y, -position.z);

        return viewMatrix;
    }
}
