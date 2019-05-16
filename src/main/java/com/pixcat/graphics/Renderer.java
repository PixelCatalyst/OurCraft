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
        glEnable(GL_DEPTH_TEST);
    }

    public void initAssets() throws IllegalStateException {
        if (window == null)
            throw new IllegalStateException("Window not created before initializing assets");
        shaderProgram = new ShaderProgram();
        FileManager fm = FileManager.getInstance();
        shaderProgram.createVertexShader(fm.loadText("vertex.vs"));
        shaderProgram.createFragmentShader(fm.loadText("fragment.fs"));
        shaderProgram.link();
        shaderProgram.createUniform("wvpMatrix");
        transform = new Transformation();
        //TODO textures
    }

    public long getWindowHandle() {
        return window.getHandle();
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
    }

    public void setOrthographic() {
        projectionMatrix = transform.getOrthographicProjection(window.getWidth(), window.getHeight());
    }

    public void draw(GraphicObject object) {
        Mesh mesh = object.getMesh();
        Matrix4f wvpMatrix = transform.getModelTrans(projectionMatrix, object.getWorldMatrix());
        shaderProgram.setUniform("wvpMatrix", wvpMatrix);
        glBindVertexArray(mesh.getVaoID());
        glEnableVertexAttribArray(0);
        glDrawElements(GL_TRIANGLES, mesh.getVertexCount(), GL_UNSIGNED_INT, 0);
        glDisableVertexAttribArray(0);
        glBindVertexArray(0);
    }

    public void endFrame() {
        shaderProgram.unbind();
        window.swapBuffers();
    }

    public void cleanup() {
        shaderProgram.cleanup();
    }
}
