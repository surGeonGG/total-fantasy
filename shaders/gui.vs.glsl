#version 400 core
uniform mat4 transformationMatrix;

in vec2 coordinates;
out vec2 texCoords;

void main() {
    texCoords = vec2((coordinates.x+1)/2, 1-(coordinates.y+1)/2);
    gl_Position = transformationMatrix * vec4(coordinates, 0, 1);
}
