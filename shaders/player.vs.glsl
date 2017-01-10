#version 400 core

uniform mat4 projectionMatrix;
uniform mat4 viewMatrix;
uniform mat4 transformationMatrix;

in vec3 coordinates;
in vec2 texCoords;

out vec4 color;
out vec2 pass_texCoords;

void main() {
    gl_Position = projectionMatrix * viewMatrix * transformationMatrix * vec4(coordinates, 1);
    pass_texCoords = texCoords;
}