package com.pixcat.graphics;

import com.pixcat.mesh.Mesh;
import org.joml.Matrix4f;

public class StaticImage extends GraphicObject {
    private float width;
    private float height;

    public StaticImage(Mesh mesh, int width, int height) {
        super(mesh);
        this.width = (float) width;
        this.height = (float) height;
    }

    public int getWidth() {
        return (int) (width * scale);
    }

    public int getHeight() {
        return (int) (height * scale);
    }

    public void setPosition(int x, int y) {
        super.setPosition((float) x, (float) y, 0.0f);
    }

    @Override
    public Matrix4f getWorldMatrix() {
        return super.getWorldMatrix().scale(width, height, 1.0f);
    }
}
