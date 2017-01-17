#version 400 core

uniform sampler2D textureSampler;
uniform vec3 lightColor;

in vec2 pass_texCoords;
in vec3 pass_normal;
in vec3 pass_towardsLightVector;

out vec4 out_color;

void main() {
//    out_color = texture(textureSampler, pass_texCoords);
    vec3 unitNormal = normalize(pass_normal);
    vec3 unitTowardsLightVector = normalize(pass_towardsLightVector);
    float brightness = dot(unitNormal, unitTowardsLightVector);
    brightness = max(brightness, 0.5);
    vec2 scaledTexCoords = pass_texCoords;

    out_color = texture(textureSampler, scaledTexCoords) * brightness * vec4(lightColor, 1);
}
