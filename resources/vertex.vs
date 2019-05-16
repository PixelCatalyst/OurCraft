#version 330

layout (location=0) in vec3 position;

uniform mat4 wvpMatrix;

void main()
{
    gl_Position = wvpMatrix * vec4(position, 1.0);
}
