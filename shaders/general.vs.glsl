#version 400 core

uniform mat4 projectionMatrix;
uniform mat4 viewMatrix;
uniform vec3 lightPosition;

in vec3 coordinates;
in vec2 texCoords;
in vec3 normal;

out vec2 pass_texCoords;
out vec3 pass_normal;
out vec3 pass_towardsLightVector;

void main() {
    gl_Position = projectionMatrix * viewMatrix * vec4(coordinates, 1);
    pass_texCoords = texCoords;
    pass_normal = normal;
    pass_towardsLightVector = vec3(50,100,100) - coordinates;
}