#version 120

attribute vec3 vertices;
uniform mat4 projectionMatrix;
uniform mat4 rotationMatrix;


void main() {

    gl_Position = projectionMatrix * vec4(vertices, 1);
    gl_PointSize = 1.0;
    gl_FrontColor = gl_Color;

}