#version 400 core

uniform mat4 projectionMatrix;
uniform mat4 rotationMatrix;
uniform int meshLength;
uniform sampler2D biomes;

in vec3 coordinates;

flat out vec4 color;

void main() {
    int instanceX = int(gl_InstanceID / meshLength);
    int instanceY = int(mod(gl_InstanceID, meshLength));
    float y = coordinates.y + gl_InstanceID / meshLength;
    float x = coordinates.x + mod(gl_InstanceID, meshLength);
    color = texelFetch(biomes, ivec2(instanceX, instanceY), 0);
    //color = texture(biomes, vec2(0.5,0.4));
    if (mod(gl_InstanceID / meshLength, 2) == 0) {
        x = x + 0.5;
    }
    gl_Position = projectionMatrix * rotationMatrix * vec4(x, y, coordinates.z, 1);
}