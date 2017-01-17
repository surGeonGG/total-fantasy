#version 400 core

uniform sampler2D rockTextureSampler;
uniform sampler2D grassTextureSampler;
uniform sampler2D desertTextureSampler;
uniform sampler2D waterTextureSampler;
uniform sampler2D snowTextureSampler;
uniform sampler2D textureMapSampler;
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
    vec2 scaledTexCoords = pass_texCoords * 100;
    float textureMapValue = texture(textureMapSampler, pass_texCoords).x;
    float moisture = texture(textureMapSampler, pass_texCoords).y;
    if (textureMapValue <= 0.05f) {
        out_color = texture(waterTextureSampler, scaledTexCoords) * brightness * vec4(lightColor, 1);
    }
//    else if (texture(textureMapSampler, pass_texCoords).x <= 0.15f) {
//        out_color = texture(desertTextureSampler, scaledTexCoords) * brightness * vec4(lightColor, 1);
//    }
    else if (textureMapValue <= 0.25f){
        vec4 blendColorGrass = (texture(grassTextureSampler, scaledTexCoords) * moisture);
        vec4 blendColorDesert = texture(desertTextureSampler, scaledTexCoords) * abs(moisture - 1.0f);
        out_color = ((blendColorDesert + blendColorGrass) / 2) * brightness * vec4(lightColor, 1);
    }
    else if (textureMapValue <= 0.35f){
        out_color = texture(rockTextureSampler, scaledTexCoords) * brightness * vec4(lightColor, 1);
    }
    else if (textureMapValue <= 0.55f){
        out_color = texture(snowTextureSampler, scaledTexCoords) * brightness * vec4(lightColor, 1);
    }
}
