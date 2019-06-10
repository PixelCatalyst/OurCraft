package com.pixcat.graphics;

import com.pixcat.core.FileManager;
import org.joml.Matrix4f;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;
import static org.lwjgl.glfw.GLFW.glfwInit;
import static org.lwjgl.opengl.GL11.glGetIntegerv;
import static org.lwjgl.opengl.GL20.GL_CURRENT_PROGRAM;

public class ShaderProgramTest {

    private ShaderProgram testShader;
    private Window placeholderWindow;

    @Before
    public void setUp() {
        if (glfwInit() == false)
            throw new RuntimeException("Unable to initialize GLFW");
        placeholderWindow = new Window(200, 200, "placeholder");
        placeholderWindow.bindAsCurrent();

        testShader = new ShaderProgram();
    }

    @After
    public void destroyWindow() {
        placeholderWindow.destroy();
    }

    @Test
    public void testCreateEmptyVertexShader() {
        final String shaderCode = "";

        testShader.createVertexShader(shaderCode);

        assertNotEquals(0, testShader.getVertexShaderID());
    }

    @Test
    public void testCreateEmptyFragmentShader() {
        final String shaderCode = "";

        testShader.createFragmentShader(shaderCode);

        assertNotEquals(0, testShader.getFragmentShaderID());
    }

    @Test(expected = IllegalStateException.class)
    public void testCreateInvalidVertexShader() {
        final String shaderCode = "invalid vertex code";

        testShader.createVertexShader(shaderCode);
    }

    @Test(expected = IllegalStateException.class)
    public void testCreateInvalidFragmentShader() {
        final String shaderCode = "invalid fragment code";

        testShader.createFragmentShader(shaderCode);
    }

    @Test
    public void testCreateValidVertexShader() {
        final String shaderCode = FileManager.getInstance().loadText("vertex.vs");

        testShader.createVertexShader(shaderCode);

        assertNotEquals(0, testShader.getVertexShaderID());
    }

    @Test
    public void testCreateValidFragmentShader() {
        final String shaderCode = FileManager.getInstance().loadText("fragment.fs");

        testShader.createFragmentShader(shaderCode);

        assertNotEquals(0, testShader.getFragmentShaderID());
    }

    @Test
    public void testLink() {
        final String vertexCode = FileManager.getInstance().loadText("vertex.vs");
        final String fragmentCode = FileManager.getInstance().loadText("fragment.fs");
        testShader.createVertexShader(vertexCode);
        testShader.createFragmentShader(fragmentCode);
        testShader.link(); //Shouldn't throw any exceptions
    }

    @Test
    public void testValidUniform() {
        final String vertexCode = FileManager.getInstance().loadText("vertex.vs");
        final String fragmentCode = FileManager.getInstance().loadText("fragment.fs");
        testShader.createVertexShader(vertexCode);
        testShader.createFragmentShader(fragmentCode);
        testShader.link();

        testShader.createUniform("wvpMatrix");
        testShader.createUniform("textureSampler");
        testShader.setUniform("textureSampler", 0);
        testShader.setUniform("wvpMatrix", new Matrix4f().identity());
        //Shouldn't throw any exceptions
    }

    @Test
    public void testInvalidUniform() {
        final String vertexCode = FileManager.getInstance().loadText("vertex.vs");
        final String fragmentCode = FileManager.getInstance().loadText("fragment.fs");
        testShader.createVertexShader(vertexCode);
        testShader.createFragmentShader(fragmentCode);
        testShader.link();

        int expectedExceptionCount = 4;
        int exceptionCount = 0;
        try {
            testShader.createUniform("wvpMatrix_INVALID");
        } catch (IllegalStateException e) {
            ++exceptionCount;
        }
        try {
            testShader.createUniform("textureSampler_INVALID");
        } catch (IllegalStateException e) {
            ++exceptionCount;
        }
        try {
            testShader.setUniform("textureSampler_INVALID", 0);
        } catch (IllegalStateException e) {
            ++exceptionCount;
        }
        try {
            testShader.setUniform("wvpMatrix_INVALID", new Matrix4f().identity());
        } catch (IllegalStateException e) {
            ++exceptionCount;
        }

        assertEquals(expectedExceptionCount, exceptionCount);
    }

    @Test(expected = IllegalStateException.class)
    public void testUniformInNorCompiledShader() {
        testShader.createUniform("some_uniform");
    }

    @Test
    public void testBind() {
        final String vertexCode = FileManager.getInstance().loadText("vertex.vs");
        final String fragmentCode = FileManager.getInstance().loadText("fragment.fs");
        testShader.createVertexShader(vertexCode);
        testShader.createFragmentShader(fragmentCode);
        testShader.link();

        testShader.bind();

        int[] idBuffer = new int[1];
        glGetIntegerv(GL_CURRENT_PROGRAM, idBuffer);

        assertNotEquals(0, idBuffer[0]);
    }

    @Test
    public void testUnbind() {
        final String vertexCode = FileManager.getInstance().loadText("vertex.vs");
        final String fragmentCode = FileManager.getInstance().loadText("fragment.fs");
        testShader.createVertexShader(vertexCode);
        testShader.createFragmentShader(fragmentCode);
        testShader.link();

        testShader.bind();
        testShader.unbind();

        int[] idBuffer = new int[1];
        glGetIntegerv(GL_CURRENT_PROGRAM, idBuffer);

        assertEquals(0, idBuffer[0]);
    }

    @Test
    public void testCleanup() {
        final String vertexCode = FileManager.getInstance().loadText("vertex.vs");
        final String fragmentCode = FileManager.getInstance().loadText("fragment.fs");
        testShader.createVertexShader(vertexCode);
        testShader.createFragmentShader(fragmentCode);
        testShader.link();

        testShader.cleanup();
        testShader.bind();
        int[] idBuffer = new int[1];
        glGetIntegerv(GL_CURRENT_PROGRAM, idBuffer);

        assertEquals(0, idBuffer[0]);
    }

}
