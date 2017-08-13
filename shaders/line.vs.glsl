#version 400 core

uniform mat4 projectionMatrix;
uniform mat4 viewMatrix;

in vec3 coordinates;

void main() {

    gl_Position = projectionMatrix * viewMatrix * vec4(coordinates, 1);
}