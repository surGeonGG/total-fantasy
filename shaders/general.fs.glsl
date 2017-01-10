#version 400 core

uniform sampler2D textureSampler;

in vec4 color;
in vec2 pass_texCoords;

out vec4 out_Color;


void main() {
    out_Color = texture(textureSampler, pass_texCoords);
}
