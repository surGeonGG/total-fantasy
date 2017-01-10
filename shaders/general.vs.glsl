#version 400 core

uniform mat4 projectionMatrix;
uniform mat4 viewMatrix;

in vec3 coordinates;
in vec2 texCoords;

out vec2 pass_texCoords;

void main() {
    gl_Position = projectionMatrix * viewMatrix * vec4(coordinates, 1);
    pass_texCoords = texCoords;
}