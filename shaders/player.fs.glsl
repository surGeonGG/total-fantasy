#version 400 core

uniform sampler2D textureSampler;
uniform vec3 lightColor;

in vec2 pass_texCoords;
in vec3 pass_normal;
in vec3 pass_towardsLightVector;

out vec4 out_color;

void main() {
    out_color = texture(textureSampler, pass_texCoords);
    vec3 unitNormal = normalize(pass_normal);
    vec3 unitTowardsLightVector = normalize(pass_towardsLightVector);
    float brightness = dot(unitNormal, unitTowardsLightVector);
    out_color = texture(textureSampler, pass_texCoords) * brightness * vec4(lightColor, 1);
//    out_color = vec4(brightness,brightness,brightness,1);
}
