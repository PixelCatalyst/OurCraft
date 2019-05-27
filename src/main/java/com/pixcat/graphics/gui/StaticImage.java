package com.pixcat.graphics.gui;

import com.pixcat.graphics.GraphicObject;
import com.pixcat.mesh.Mesh;
import org.joml.Matrix4f;

public class StaticImage extends GraphicObject {
    private float width;
    private float height;
    private int viewportWidth;
    private int viewportHeight;

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

    public int getX() {
        return (int) getPosition().x;
    }

    public int getY() {
        return (int) getPosition().y;
    }

    public StaticImage setPosition(int x, int y) {
        super.setPosition((float) x, (float) y, 0.0f);
        return this;
    }

    public StaticImage setSize(float width, float height) {
        this.width = width;
        this.height = height;
        return this;
    }

    public StaticImage viewport(int viewportWidth, int viewportHeight) {
        this.viewportWidth = viewportWidth;
        this.viewportHeight = viewportHeight;
        return this;
    }

    // set position relative to the viewport size
    public StaticImage setPositionRel(Float relX, Float relY) {
        float x = relX == null? getX() : (relX * viewportWidth);
        float y = relY == null? getY() : (relY * viewportHeight);
        setPosition((int) x, (int) y);
        return this;
    }

    public StaticImage move(int offsetX, int offsetY) {
        setPosition(getX() + offsetX, getY() + offsetY);
        return this;
    }

    public StaticImage moveRel(int relOffsetX, int relOffsetY) {
        int offsetX = relOffsetX * viewportWidth;
        int offsetY = relOffsetX * viewportWidth;
        setPosition(getX() + offsetX, getY() + offsetY);
        return this;
    }

    public StaticImage selfCenterX() {
        return move(-getWidth() / 2, 0);
    }

    public StaticImage selfCenterY() {
        return move(0, -getHeight() / 2);
    }

    public StaticImage selfCenter() {
        return move(-getWidth() / 2, -getHeight() / 2);
    }

    @Override
    public Matrix4f getWorldMatrix() {
        return super.getWorldMatrix().scale(width, height, 1.0f);
    }
}
