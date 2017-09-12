#version 400 core
uniform mat4 transformationMatrix;

in vec2 coordinates;
in vec2 texCoords;

out vec2 pass_texCoords;


void main() {
    pass_texCoords = texCoords;
    gl_Position = vec4(coordinates, 0, 1);
}
