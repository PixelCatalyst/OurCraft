package com.pixcat.graphics;

import com.pixcat.mesh.Mesh;
import org.joml.Vector3f;
import org.joml.Matrix4f;

public class GraphicObject {
    private Mesh mesh;
    private Vector3f position;
    private Matrix4f worldMatrix;

    //TODO Texture texture/int textureID...

    public GraphicObject(Mesh mesh) {
        this.mesh = mesh;
        position = new Vector3f(0.0f, 0.0f, 0.0f);
        worldMatrix = new Matrix4f();
    }

    public Mesh getMesh() {
        return mesh;
    }

    public void setPosition(float x, float y, float z) {
        position.x = x;
        position.y = y;
        position.z = z;
    }

    public Matrix4f getWorldMatrix() {
        worldMatrix.identity();
        worldMatrix.translate(position);
        return worldMatrix;
    }
}
