#version 330

layout (location=0) in vec3 position;
layout (location=1) in vec2 inTexCoord;

out vec2 texCoord;

uniform mat4 wvpMatrix;

void main()
{
    gl_Position = wvpMatrix * vec4(position, 1.0);
    texCoord = inTexCoord;
}
