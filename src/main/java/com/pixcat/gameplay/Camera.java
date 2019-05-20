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

    private Vector3f position;
    private Vector3f rotation;
    private Matrix4f viewMatrix;

    private ArrayList<Chunk> visibleChunks;

    public Camera(float x, float y, float z) {
        fieldOfView = (float) Math.toRadians(60.0);
        nearClippingDist = 0.01f;
        farClippingDist = 1000.0f;

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
            position.x += (float) Math.sin(Math.toRadians(rotation.y - 90)) * -1.0f * offsetX;
            position.z += (float) Math.cos(Math.toRadians(rotation.y - 90)) * offsetX;
        }
        position.y += offsetY;
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

        if (rotation.x > 85.0f)
            rotation.x = 85.0f;
        else if (rotation.x < -85.0f)
            rotation.x = -85.0f;
    }

    public ArrayList<Chunk> getVisibleChunks(SpatialStructure voxels) {
        visibleChunks.clear();
        //TODO Frustum Culling
        int diameter = voxels.getDiameter();
        int planeMin = -(diameter / 2);
        int planeMax = (diameter / 2);
        if ((diameter % 2) == 0)
            --planeMax;

        for (int x = planeMin; x <= planeMax; ++x) {
            for (int z = planeMin; z <= planeMax; ++z) {
                for (int y = 0; y < 8; ++y) {
                    Chunk chunk = voxels.getChunk(y, x, z);
                    if (chunk != null)
                        visibleChunks.add(chunk);
                }
            }
        }
        return visibleChunks;
    }

    public Matrix4f getViewMatrix() {
        viewMatrix.identity();

        viewMatrix.rotate((float) Math.toRadians(rotation.x), new Vector3f(1, 0, 0))
                .rotate((float) Math.toRadians(rotation.y), new Vector3f(0, 1, 0));
        viewMatrix.translate(-position.x, -position.y, -position.z);

        return viewMatrix;
    }
}
