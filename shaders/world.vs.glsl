#version 400 core

uniform mat4 projectionMatrix;
uniform mat4 rotationMatrix;
uniform int meshLength;
uniform sampler2D biomes;

in vec3 coordinates;

flat out vec4 color;

void main() {



    int instanceY = int(gl_InstanceID / meshLength);
    int instanceX = int(mod(gl_InstanceID, meshLength));

    float y = coordinates.y + gl_InstanceID / meshLength;
    float x = coordinates.x + mod(gl_InstanceID, meshLength);

    color = texelFetch(biomes, ivec2(instanceX, instanceY), 0);

    if (mod(gl_InstanceID / meshLength, 2) == 0) {
        x = x + 0.5;
    }

    gl_Position = projectionMatrix * vec4(x, y, coordinates.z, 1);






}