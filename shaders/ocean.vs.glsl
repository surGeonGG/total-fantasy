#version 400 core

uniform mat4 projectionMatrix;
uniform mat4 viewMatrix;
uniform mat4 transformationMatrix;
uniform vec3 lightPosition;

in vec3 coordinates;
in vec2 texCoords;
in vec3 normal;

out vec2 pass_texCoords;
out vec3 pass_normal;
out vec3 pass_towardsLightVector;
out float pass_visibility;

const float density = 0.002;
const float gradient = 1.4;

void main() {

    vec4 worldPosition = transformationMatrix * vec4(coordinates, 1);
    gl_Position = projectionMatrix * viewMatrix * worldPosition;
    pass_texCoords = texCoords;
    pass_normal = (transformationMatrix * vec4(normal,0)).xyz;
    pass_towardsLightVector = lightPosition - worldPosition.xyz;
    vec4 coordinatesRelToCam = viewMatrix * worldPosition;
    float distanceToCam = length(coordinatesRelToCam);
    pass_visibility = exp(-pow((distanceToCam*density), gradient));
    pass_visibility = clamp(pass_visibility, 0.0, 1.0);
}