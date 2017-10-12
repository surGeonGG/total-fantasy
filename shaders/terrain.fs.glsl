#version 400 core

uniform sampler2D biomes;

uniform sampler2D permTexture;
uniform sampler1D simplexTexture;
uniform vec3 lightColor;

in vec2 pass_texCoords;
in vec3 pass_normal;
in vec3 pass_towardsLightVector;
in float pass_visibility;

out vec4 out_color;

const vec3 skyColor = vec3(0.40,0.70,0.92);
const int GRASSLAND = 0;
const int DESERT = 1;
const int FOREST = 2;
const int TROPICAL_FOREST = 3;
const int SWAMPLAND = 4;
const int BADLANDS = 5;
const int STONY = 6;
const int OCEAN = 7;
const int DEEP_FOREST = 8;
const int SNOW = 9;
const vec4 grassland = vec4(0.533, 0.666, 0.333, 1);
const vec4 desert = vec4(0.839, 0.733, 0.615, 1);
const vec4 elevatedDesert = vec4(0.699, 0.592, 0.537, 1);
const vec4 deep_forest = vec4(0.266, 0.533, 0.333, 1);
const vec4 forest = vec4(0.392, 0.568, 0.372, 1);
const vec4 tropical_forest = vec4(0.333, 0.600, 0.266, 1);
const vec4 tropical_deep_forest = vec4(0.200, 0.470, 0.333, 1);
const vec4 stony = vec4(0.51, 0.572, 0.62, 1);
const vec4 badlands = vec4(0.5, 0.42, 0.36, 1);
const vec4 snow = vec4(0.98, 0.98, 0.98, 1);
const vec4 swampland = vec4(0.51, 0.55, 0.32, 1);

float blendEval(float begin, float end, float x) {
    return smoothstep(begin - 0.05, begin + 0.05, x) * smoothstep(-end - 0.05, -end + 0.05, -x);
}

int isSame(int edge, float x) {
    float evalLarger = step(float(-edge), -x);
    float evalSmaller = step(float(edge), x);
    float eval = evalSmaller * evalLarger;
    return int(eval);
}

float blendEval(float begin, float end, float x, float blendRange) {
    return smoothstep(begin - blendRange, begin + blendRange, x) * smoothstep(-end - blendRange, -end + blendRange, -x);
}

void main() {
    //lighting
    vec3 unitNormal = normalize(pass_normal);
    vec3 unitTowardsLightVector = normalize(pass_towardsLightVector);
    float brightness = dot(unitNormal, unitTowardsLightVector);
    brightness = max(brightness, 0.4);
    //texturing


    vec2 texCoordsOfLL = ivec2(pass_texCoords * 200) / 200.0f;
    vec2 texCoordsOfLR = ivec2(pass_texCoords * 200 + vec2(1, 0)) / 200.0f;
    vec2 texCoordsOfUL = ivec2(pass_texCoords * 200 + vec2(0, 1)) / 200.0f;
    vec2 texCoordsOfUR = ivec2(pass_texCoords * 200 + vec2(1, 1)) / 200.0f;
    float xDistanceFromR = texCoordsOfLR.x - pass_texCoords.x;
    float xDistanceFromL = pass_texCoords.x - texCoordsOfLL.x;
    float yDistanceFromU = texCoordsOfUL.y - pass_texCoords.y;
    float yDistanceFromD = pass_texCoords.y - texCoordsOfLL.y;
    float length = texCoordsOfLR.x - texCoordsOfLL.x;
    float weightLL = (yDistanceFromU / length) * (xDistanceFromR / length);
    float weightLR = (yDistanceFromU / length) * (xDistanceFromL / length);
    float weightUL = (yDistanceFromD / length) * (xDistanceFromR / length);
    float weightUR = (yDistanceFromD / length) * (xDistanceFromL / length);

//    vec4 height_moistureLL = texture(biomes, pass_texCoords) * weightLL;
//    vec4 height_moistureLR = texture(biomes, pass_texCoords) * weightLR;
//    vec4 height_moistureUL = texture(biomes, pass_texCoords) * weightUL;
//    vec4 height_moistureUR = texture(biomes, pass_texCoords) * weightUR;
//    vec4 biome = (height_moistureLL + height_moistureLR
//                            + height_moistureUL + height_moistureUR);
    vec4 biome = texture(biomes, pass_texCoords);

    float moist_blend_str = 0.25;
    float biomex = biome.x * 255;

    vec4 blendDesert = grassland * isSame(DESERT, biomex);
    vec4 blendGrassland = grassland * isSame(GRASSLAND, biomex);
    vec4 blendForest = forest * isSame(FOREST, biomex);
    vec4 blendBadlands = badlands * isSame(BADLANDS, biomex);
    vec4 blendStony = stony * isSame(STONY, biomex);
    vec4 blendOcean = desert * isSame(OCEAN, biomex);
    vec4 blendDeepForest = deep_forest * isSame(DEEP_FOREST, biomex);
    vec4 blendSwampland = swampland * isSame(SWAMPLAND, biomex);
    vec4 blendTropicalForest = tropical_forest * isSame(TROPICAL_FOREST, biomex);
    vec4 blendSnow = snow * isSame(SNOW, biomex);

    out_color = (blendGrassland + blendDesert + blendForest +
                 blendBadlands + blendStony + blendOcean +
                 blendDeepForest + blendSwampland + blendTropicalForest + blendSnow)
                 * brightness * vec4(lightColor, 1);

//    out_color = mix(vec4(skyColor, 1.0), out_color, pass_visibility);
}
