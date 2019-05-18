package com.pixcat.graphics;

import com.pixcat.mesh.Mesh;
import org.joml.Vector3f;
import org.joml.Matrix4f;

public class GraphicObject {
    private Mesh mesh;
    private Texture texture;
    private Vector3f position;
    protected float scale;
    private Matrix4f worldMatrix;

    public GraphicObject(Mesh mesh) {
        this.mesh = mesh;
        position = new Vector3f(0.0f, 0.0f, 0.0f);
        scale = 1.0f;
        worldMatrix = new Matrix4f();
    }

    public Mesh getMesh() {
        return mesh;
    }

    public Texture getTexture() {
        return texture;
    }

    public void setTexture(Texture texture) {
        this.texture = texture;
    }

    public void setPosition(float x, float y, float z) {
        position.x = x;
        position.y = y;
        position.z = z;
    }

    public void setScale(float scale) {
        this.scale = scale;
    }

    public Matrix4f getWorldMatrix() {
        worldMatrix.identity();
        worldMatrix.translate(position);
        worldMatrix.scale(scale);
        return worldMatrix;
    }
}
