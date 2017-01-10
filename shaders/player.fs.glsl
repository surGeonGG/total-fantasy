#version 400 core

uniform sampler2D textureSampler;

in vec4 color;
in vec2 pass_texCoords;

out vec4 out_color;

void main() {

    out_color = texture(textureSampler, pass_texCoords);
}
