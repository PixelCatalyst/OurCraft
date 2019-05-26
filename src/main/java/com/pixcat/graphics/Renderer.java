package com.pixcat.graphics;

import com.pixcat.core.FileManager;
import com.pixcat.gameplay.Camera;
import com.pixcat.mesh.Mesh;
import org.joml.Matrix4f;

import static org.lwjgl.glfw.GLFW.*;
import static org.lwjgl.opengl.GL33.*;

public class Renderer {
    private Window window;
    private int viewportWidth;
    private int viewportHeight;

    private ShaderProgram shaderProgram;
    private Transformation transform;
    private Matrix4f projectionMatrix;

    public Renderer(int oglMajorVersion, int oglMinorVersion) {
        glfwWindowHint(GLFW_CONTEXT_VERSION_MAJOR, oglMajorVersion);
        glfwWindowHint(GLFW_CONTEXT_VERSION_MINOR, oglMinorVersion);
        glfwWindowHint(GLFW_OPENGL_PROFILE, GLFW_OPENGL_CORE_PROFILE);
        glfwWindowHint(GLFW_OPENGL_FORWARD_COMPAT, GL_TRUE);
    }

    public void createWindow(int width, int height, String text) {
        window = new Window(width, height, text);
        window.bindAsCurrent();
    }

    public void initAssets() {
        if (window == null)
            throw new IllegalStateException("Window not created before initializing assets");
        shaderProgram = new ShaderProgram();
        FileManager fm = FileManager.getInstance();
        shaderProgram.createVertexShader(fm.loadText("vertex.vs"));
        shaderProgram.createFragmentShader(fm.loadText("fragment.fs"));
        shaderProgram.link();
        shaderProgram.createUniform("wvpMatrix");
        shaderProgram.createUniform("textureSampler");
        shaderProgram.setUniform("textureSampler", 0);
        transform = new Transformation();

        glEnable(GL_CULL_FACE);
        glCullFace(GL_BACK);
    }

    public long getWindowHandle() {
        return window.getHandle();
    }

    public int getWindowWidth() {
        return window.getWidth();
    }

    public int getWindowHeight() {
        return window.getHeight();
    }

    public boolean windowIsOpen() {
        if (window == null)
            return false;
        return window.isOpen();
    }

    public void centerWindow() {
        window.center();
    }

    public void destroyWindow() {
        window.destroy();
    }

    public void beginFrame() {
        glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT);
        int windowWidth = window.getWidth();
        int windowHeight = window.getHeight();
        if ((windowWidth != viewportWidth) || (windowHeight != viewportHeight)) {
            viewportWidth = windowWidth;
            viewportHeight = windowHeight;
            glViewport(0, 0, viewportWidth, viewportHeight);
        }
        shaderProgram.bind();
    }

    public void setBackgroundColor(float red, float green, float blue) {
        glClearColor(red, green, blue, 0.0f);
    }

    public void setPerspective(Camera camera) {
        projectionMatrix = transform.getPerspectiveProjection(camera.getFieldOfView(),
                window.getAspectRatio(),
                camera.getNearClippingDist(),
                camera.getFarClippingDist());
        projectionMatrix.mul(camera.getViewMatrix());
        glEnable(GL_DEPTH_TEST);
    }

    public void setOrthographic() {
        projectionMatrix = transform.getOrthographicProjection(window.getWidth(), window.getHeight());
        glDisable(GL_DEPTH_TEST);
    }

    public void draw(GraphicObject object) {
        Texture tex = object.getTexture();
        tex.bind();
        Mesh mesh = object.getMesh();
        Matrix4f wvpMatrix = transform.getModelTrans(projectionMatrix, object.getWorldMatrix());
        shaderProgram.setUniform("wvpMatrix", wvpMatrix);
        drawMesh(mesh);
    }

    public void draw(GraphicBatch batch) {
        batch.beginIteration();
        while (batch.hasNext()) {
            batch.bindTexture();
            Mesh mesh = batch.getMesh();
            Matrix4f wvpMatrix = transform.getModelTrans(projectionMatrix, batch.getWorldMatrix());
            shaderProgram.setUniform("wvpMatrix", wvpMatrix);
            drawMesh(mesh);
            batch.next();
        }
    }

    private void drawMesh(Mesh m) {
        glBindVertexArray(m.getVaoID());
        glEnableVertexAttribArray(0);
        glEnableVertexAttribArray(1);
        glDrawElements(GL_TRIANGLES, m.getVertexCount(), GL_UNSIGNED_INT, 0);
    }

    public void endFrame() {
        shaderProgram.unbind();
        window.swapBuffers();
    }

    public void cleanup() {
        shaderProgram.cleanup();
    }
}
