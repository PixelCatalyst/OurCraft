package com.pixcat.graphics;

import static org.lwjgl.opengl.GL33.*;

public class ShaderProgram {
    private final int programID;
    private int vertexShaderID;
    private int fragmentShaderID;

    public ShaderProgram() throws IllegalStateException {
        programID = glCreateProgram();
        if (programID == 0)
            throw new IllegalStateException("Error initializing shader");
    }

    public void createVertexShader(String shaderCode) throws IllegalStateException {
        vertexShaderID = createShader(shaderCode, GL_VERTEX_SHADER);
    }

    public void createFragmentShader(String shaderCode) throws IllegalStateException {
        fragmentShaderID = createShader(shaderCode, GL_FRAGMENT_SHADER);
    }

    private int createShader(String shaderCode, int shaderType) throws IllegalStateException {
        int shaderID = glCreateShader(shaderType);
        if (shaderID == 0)
            throw new IllegalStateException("Error creating shader");

        glShaderSource(shaderID, shaderCode);
        glCompileShader(shaderID);
        if (glGetShaderi(shaderID, GL_COMPILE_STATUS) == 0)
            throw new IllegalStateException("Error compiling shader code: " + getCurrentInfoLog());
        glAttachShader(programID, shaderID);
        return shaderID;
    }

    public void link() throws IllegalStateException {
        glLinkProgram(programID);
        if (glGetProgrami(programID, GL_LINK_STATUS) == 0)
            throw new IllegalStateException("Error linking shader code: " + getCurrentInfoLog());

        if (vertexShaderID != 0)
            glDetachShader(programID, vertexShaderID);
        if (fragmentShaderID != 0)
            glDetachShader(programID, fragmentShaderID);

        glValidateProgram(programID);
        if (glGetProgrami(programID, GL_VALIDATE_STATUS) == 0)
            System.err.println("Warning validating shader code: " + getCurrentInfoLog());
    }

    private String getCurrentInfoLog() {
        final int maxLength = 1024;
        return glGetProgramInfoLog(programID, maxLength);
    }

    public void bind() {
        if (programID == 0)
            System.err.println("Warning: binding empty shader");
        glUseProgram(programID);
    }

    public void unbind() {
        glUseProgram(0);
    }

    public void cleanup() {
        unbind();
        if (programID != 0)
            glDeleteProgram(programID);
    }
}
