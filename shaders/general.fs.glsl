#version 400 core

uniform sampler2D textureSampler;
uniform vec3 lightColor;

in vec2 pass_texCoords;
in vec3 pass_normal;
in vec3 pass_towardsLightVector;

out vec4 out_color;

void main() {
//        out_color = texture(textureSampler, pass_texCoords);
//        pass_normal = normalize(pass_normal);
//        pass_towardsLightVector = normalize(pass_towardsLightVector);
//        float brightness = dot(pass_normal, pass_towardsLightVector);
        out_color = texture(textureSampler, pass_texCoords);
//        out_color = vec4(brightness,brightness,brightness,1);
}
