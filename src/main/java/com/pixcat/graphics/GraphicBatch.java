package com.pixcat.graphics;

import org.joml.Matrix4f;
import org.joml.Vector3f;
import com.pixcat.mesh.Mesh;

import java.util.ArrayList;

public class GraphicBatch {
    private ArrayList<GraphicObject> objects;
    private ArrayList<Integer> textureChanges;
    private int changeIndex;
    private int iterator;
    private Vector3f position;
    private Matrix4f worldMatrix;

    public GraphicBatch() {
        objects = new ArrayList<>();
        textureChanges = new ArrayList<>();
        iterator = Integer.MAX_VALUE;
        position = new Vector3f(0.0f, 0.0f, 0.0f);
        worldMatrix = new Matrix4f();
    }

    public void addObject(GraphicObject g) {
        objects.add(g);
    }

    public void bakeTextures() {
        if (objects.size() > 0) {
            objects.sort(GraphicObject::compareTextures);
            textureChanges.clear();
            Texture currTex = objects.get(0).getTexture();
            textureChanges.add(0);
            for (int i = 1; i < objects.size(); ++i) {
                Texture otherTex = objects.get(i).getTexture();
                if (currTex.equals(otherTex) == false) {
                    textureChanges.add(i);
                    currTex = otherTex;
                }
            }
        }
    }

    public void beginIteration() {
        iterator = 0;
        changeIndex = 0;
        if (textureChanges.size() == 0)
            bakeTextures();
    }

    public boolean hasNext() {
        return iterator < objects.size();
    }

    public void next() {
        ++iterator;
    }

    public Mesh getMesh() {
        return objects.get(iterator).getMesh();
    }

    public void bindTexture() {
        if (changeIndex < textureChanges.size()) {
            if (iterator == textureChanges.get(changeIndex)) {
                ++changeIndex;
                objects.get(iterator).getTexture().bind();
            }
        }
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
