package com.pixcat.graphics;

import org.joml.Matrix4f;
import org.lwjgl.system.MemoryStack;

import java.nio.FloatBuffer;
import java.util.Map;
import java.util.HashMap;

import static org.lwjgl.opengl.GL33.*;

public class ShaderProgram {
    private int programID;
    private int vertexShaderID;
    private int fragmentShaderID;

    private final Map<String, Integer> uniforms;

    public ShaderProgram() {
        programID = glCreateProgram();
        if (programID == 0)
            throw new IllegalStateException("Error initializing shader");
        uniforms = new HashMap<>();
    }

    public void createVertexShader(String shaderCode) {
        vertexShaderID = createShader(shaderCode, GL_VERTEX_SHADER);
    }

    public void createFragmentShader(String shaderCode) {
        fragmentShaderID = createShader(shaderCode, GL_FRAGMENT_SHADER);
    }

    private int createShader(String shaderCode, int shaderType) {
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

    public int getVertexShaderID() {
        return vertexShaderID;
    }

    public int getFragmentShaderID() {
        return fragmentShaderID;
    }

    public void link() {
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

    public void createUniform(String uniformName) {
        if (programID == 0)
            throw new IllegalStateException("Could not create uniform in not compiled shader");
        int uniformLocation = glGetUniformLocation(programID, uniformName);
        if (uniformLocation < 0)
            throw new IllegalStateException("Could not find uniform: " + uniformName);
        uniforms.put(uniformName, uniformLocation);
    }

    public void setUniform(String uniformName, Matrix4f value) {
        try (MemoryStack stack = MemoryStack.stackPush()) {
            FloatBuffer buffer = stack.mallocFloat(16); //4x4 floats
            value.get(buffer);
            Integer location = uniforms.get(uniformName);
            if (location == null)
                throw new IllegalStateException("Tried to set non existing matrix uniform");
            glUniformMatrix4fv(location, false, buffer);
        }
    }

    public void setUniform(String uniformName, int value) {
        Integer location = uniforms.get(uniformName);
        if (location == null)
            throw new IllegalStateException("Tried to set non existing int uniform");
        glUniform1i(location, value);
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
        if (programID != 0) {
            glDeleteProgram(programID);
            programID = 0;
        }
    }
}
