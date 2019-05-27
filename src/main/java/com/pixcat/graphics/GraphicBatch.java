package com.pixcat.graphics;

import org.joml.Matrix4f;
import org.joml.Vector3f;
import com.pixcat.mesh.Mesh;

import java.util.ArrayList;

public class GraphicBatch {
    private ArrayList<GraphicObject> objects;
    private int iterator;
    private Vector3f position;
    private Matrix4f worldMatrix;

    public GraphicBatch() {
        objects = new ArrayList<>();
        iterator = Integer.MAX_VALUE;
        position = new Vector3f(0.0f, 0.0f, 0.0f);
        worldMatrix = new Matrix4f();
    }

    public int size() {
        return objects.size();
    }

    public void addObject(GraphicObject g) {
        objects.add(g);
    }

    public void beginIteration() {
        iterator = 0;
    }

    public boolean hasNext() {
        return iterator < objects.size();
    }

    public void next() {
        ++iterator;
    }

    public GraphicObject getObject() {
        return objects.get(iterator);
    }

    public GraphicObject getObject(int index) {
        return objects.get(index);
    }

    public Mesh getMesh() {
        return objects.get(iterator).getMesh();
    }

    public Texture getTexture() {
        return objects.get(iterator).getTexture();
    }

    public Vector3f getPosition() {
        return new Vector3f(position);
    }

    public void setPosition(float x, float y, float z) {
        position.x = x;
        position.y = y;
        position.z = z;
    }

    public Matrix4f getWorldMatrix() {
        worldMatrix.identity();
        worldMatrix.translate(position);
        worldMatrix.mul(objects.get(iterator).getWorldMatrix());
        return worldMatrix;
    }
}
