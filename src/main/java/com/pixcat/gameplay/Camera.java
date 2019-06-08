package com.pixcat.gameplay;

import com.pixcat.voxel.Chunk;
import com.pixcat.voxel.SpatialStructure;
import org.joml.Vector3f;
import org.joml.Matrix4f;

import java.util.ArrayList;

public class Camera {
    private float fieldOfView;
    private float nearClippingDist;
    private float farClippingDist;
    private float maxAngle;

    private Vector3f position;
    private Vector3f rotation;
    private Matrix4f viewMatrix;

    private ArrayList<Chunk> visibleChunks;

    public Camera(float x, float y, float z) {
        fieldOfView = (float) Math.toRadians(60.0);
        nearClippingDist = 0.01f;
        farClippingDist = 1000.0f;
        maxAngle = 90.0f;

        position = new Vector3f(x, y, z);
        rotation = new Vector3f(0.0f, 0.0f, 0.0f);
        viewMatrix = new Matrix4f();
        visibleChunks = new ArrayList<>();
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
        return new Vector3f(position);
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
            position.x += (float) Math.sin(Math.toRadians(rotation.y - maxAngle)) * -1.0f * offsetX;
            position.z += (float) Math.cos(Math.toRadians(rotation.y - maxAngle)) * offsetX;
        }
        position.y += offsetY;
    }

    public Vector3f getDirection() {
        Vector3f direction = new Vector3f(0.0f, 0.0f, -1.0f);
        direction.rotateX((float) Math.toRadians(-rotation.x));
        direction.rotateY((float) Math.toRadians(-rotation.y));
        return direction;
    }

    public Vector3f getRotation() {
        return new Vector3f(rotation);
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

        if (rotation.x > maxAngle)
            rotation.x = maxAngle;
        else if (rotation.x < -maxAngle)
            rotation.x = -maxAngle;
    }

    public ArrayList<Chunk> getVisibleChunks(SpatialStructure voxels) {
        visibleChunks.clear();
        //TODO Frustum Culling

        voxels.getAll(visibleChunks);
        return visibleChunks;
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
