#version 400 core

uniform sampler2D mountainTextureSampler;
uniform sampler2D grassTextureSampler;
uniform sampler2D desertTextureSampler;
uniform sampler2D waterTextureSampler;
uniform sampler2D forestTextureSampler;
uniform sampler2D snowTextureSampler;
uniform sampler2D grassDesertMountainSnowSampler;
uniform sampler2D waterForestSampler;
uniform vec3 lightColor;

in vec2 pass_texCoords;
in vec3 pass_normal;
in vec3 pass_towardsLightVector;

out vec4 out_color;

void main() {
    vec3 unitNormal = normalize(pass_normal);
    vec3 unitTowardsLightVector = normalize(pass_towardsLightVector);
    float brightness = dot(unitNormal, unitTowardsLightVector);
    brightness = max(brightness, 0.8);
    vec2 scaledTexCoords = pass_texCoords * 50;

    vec4 grassDesertMountainSnow = texture(grassDesertMountainSnowSampler, pass_texCoords);
    vec4 waterForest = texture(waterForestSampler, pass_texCoords);

    vec4 blendColorGrass = texture(grassTextureSampler, scaledTexCoords) * grassDesertMountainSnow.x;
    vec4 blendColorDesert = texture(desertTextureSampler, scaledTexCoords) * grassDesertMountainSnow.y;
    vec4 blendColorMountain = texture(mountainTextureSampler, scaledTexCoords) * grassDesertMountainSnow.z;
    vec4 blendColorSnow = texture(snowTextureSampler, scaledTexCoords) * grassDesertMountainSnow.w;

    vec4 blendColorForest = texture(forestTextureSampler, scaledTexCoords) * waterForest.y;

    out_color = (blendColorGrass + blendColorDesert + blendColorMountain + blendColorSnow + blendColorForest)
                * brightness * vec4(lightColor, 1);
//    if (out_color == vec4(0,0,0,0))
//        out_color = texture(waterTextureSampler, scaledTexCoords);
}
