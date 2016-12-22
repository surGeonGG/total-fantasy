#version 400 core

uniform mat4 projectionMatrix;
uniform mat4 transformationMatrix;
uniform mat4 rotationMatrix;
uniform int meshLength;
uniform sampler2D biomes;

in vec3 coordinates;

flat out vec4 color;

void main() {





    color = vec4(0,1,1,1);
    //color = texture(biomes, vec2(0.5,0.4));

    gl_Position = projectionMatrix * rotationMatrix * transformationMatrix * vec4(coordinates, 1);






}