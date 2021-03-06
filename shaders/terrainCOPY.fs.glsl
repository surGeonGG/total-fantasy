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

uniform sampler2D permTexture;
uniform sampler1D simplexTexture;
//uniform sampler2D gradTexture;
//uniform float time; // Used for texture animation

/*
 * To create offsets of one texel and one half texel in the
 * texture lookup, we need to know the texture image size.
 */
#define ONE 0.00390625
#define ONEHALF 0.001953125
// The numbers above are 1/256 and 0.5/256, change accordingly
// if you change the code to use another texture size.


/*
 * The interpolation function. This could be a 1D texture lookup
 * to get some more speed, but it's not the main part of the algorithm.
 */
float fade(float t) {
  // return t*t*(3.0-2.0*t); // Old fade, yields discontinuous second derivative
  return t*t*t*(t*(t*6.0-15.0)+10.0); // Improved fade, yields C2-continuous noise
}

/*
 * 3D simplex noise. Comparable in speed to classic noise, better looking.
 */
float snoise(vec3 P) {

// The skewing and unskewing factors are much simpler for the 3D case
#define F3 0.333333333333
#define G3 0.166666666667

  // Skew the (x,y,z) space to determine which cell of 6 simplices we're in
 	float s = (P.x + P.y + P.z) * F3; // Factor for 3D skewing
  vec3 Pi = floor(P + s);
  float t = (Pi.x + Pi.y + Pi.z) * G3;
  vec3 P0 = Pi - t; // Unskew the cell origin back to (x,y,z) space
  Pi = Pi * ONE + ONEHALF; // Integer part, scaled and offset for texture lookup

  vec3 Pf0 = P - P0;  // The x,y distances from the cell origin

  // For the 3D case, the simplex shape is a slightly irregular tetrahedron.
  // To find out which of the six possible tetrahedra we're in, we need to
  // determine the magnitude ordering of x, y and z components of Pf0.
  // The method below is explained briefly in the C code. It uses a small
  // 1D texture as a lookup table. The table is designed to work for both
  // 3D and 4D noise, so only 8 (only 6, actually) of the 64 indices are
  // used here.
  float c1 = (Pf0.x > Pf0.y) ? 0.5078125 : 0.0078125; // 1/2 + 1/128
  float c2 = (Pf0.x > Pf0.z) ? 0.25 : 0.0;
  float c3 = (Pf0.y > Pf0.z) ? 0.125 : 0.0;
  float sindex = c1 + c2 + c3;
  vec3 offsets = texture1D(simplexTexture, sindex).rgb;
  vec3 o1 = step(0.375, offsets);
  vec3 o2 = step(0.125, offsets);

  // Noise contribution from simplex origin
  float perm0 = texture2D(permTexture, Pi.xy).a;
  vec3  grad0 = texture2D(permTexture, vec2(perm0, Pi.z)).rgb * 4.0 - 1.0;
  float t0 = 0.6 - dot(Pf0, Pf0);
  float n0;
  if (t0 < 0.0) n0 = 0.0;
  else {
    t0 *= t0;
    n0 = t0 * t0 * dot(grad0, Pf0);
  }

  // Noise contribution from second corner
  vec3 Pf1 = Pf0 - o1 + G3;
  float perm1 = texture2D(permTexture, Pi.xy + o1.xy*ONE).a;
  vec3  grad1 = texture2D(permTexture, vec2(perm1, Pi.z + o1.z*ONE)).rgb * 4.0 - 1.0;
  float t1 = 0.6 - dot(Pf1, Pf1);
  float n1;
  if (t1 < 0.0) n1 = 0.0;
  else {
    t1 *= t1;
    n1 = t1 * t1 * dot(grad1, Pf1);
  }

  // Noise contribution from third corner
  vec3 Pf2 = Pf0 - o2 + 2.0 * G3;
  float perm2 = texture2D(permTexture, Pi.xy + o2.xy*ONE).a;
  vec3  grad2 = texture2D(permTexture, vec2(perm2, Pi.z + o2.z*ONE)).rgb * 4.0 - 1.0;
  float t2 = 0.6 - dot(Pf2, Pf2);
  float n2;
  if (t2 < 0.0) n2 = 0.0;
  else {
    t2 *= t2;
    n2 = t2 * t2 * dot(grad2, Pf2);
  }

  // Noise contribution from last corner
  vec3 Pf3 = Pf0 - vec3(1.0-3.0*G3);
  float perm3 = texture2D(permTexture, Pi.xy + vec2(ONE, ONE)).a;
  vec3  grad3 = texture2D(permTexture, vec2(perm3, Pi.z + ONE)).rgb * 4.0 - 1.0;
  float t3 = 0.6 - dot(Pf3, Pf3);
  float n3;
  if(t3 < 0.0) n3 = 0.0;
  else {
    t3 *= t3;
    n3 = t3 * t3 * dot(grad3, Pf3);
  }

  // Sum up and scale the result to cover the range [-1,1]
  return 32.0 * (n0 + n1 + n2 + n3);
}

/*
 * 2D simplex noise. Somewhat slower but much better looking than classic noise.
 */
//float snoise(vec2 P) {
//
//// Skew and unskew factors are a bit hairy for 2D, so define them as constants
//// This is (sqrt(3.0)-1.0)/2.0
//#define F2 0.366025403784
//// This is (3.0-sqrt(3.0))/6.0
//#define G2 0.211324865405
//
//  // Skew the (x,y) space to determine which cell of 2 simplices we're in
//  float s = (P.x + P.y) * F2;   // Hairy factor for 2D skewing
//  vec2 Pi = floor(P + s);
//  float t = (Pi.x + Pi.y) * G2; // Hairy factor for unskewing
//  vec2 P0 = Pi - t; // Unskew the cell origin back to (x,y) space
//  Pi = Pi * ONE + ONEHALF; // Integer part, scaled and offset for texture lookup
//
//  vec2 Pf0 = P - P0;  // The x,y distances from the cell origin
//
//  // For the 2D case, the simplex shape is an equilateral triangle.
//  // Find out whether we are above or below the x=y diagonal to
//  // determine which of the two triangles we're in.
//  vec2 o1;
//  if(Pf0.x > Pf0.y) o1 = vec2(1.0, 0.0);  // +x, +y traversal order
//  else o1 = vec2(0.0, 1.0);               // +y, +x traversal order
//
//  // Noise contribution from simplex origin
//  vec2 grad0 = texture2D(permTexture, Pi).rg * 4.0 - 1.0;
//  float t0 = 0.5 - dot(Pf0, Pf0);
//  float n0;
//  if (t0 < 0.0) n0 = 0.0;
//  else {
//    t0 *= t0;
//    n0 = t0 * t0 * dot(grad0, Pf0);
//  }
//
//  // Noise contribution from middle corner
//  vec2 Pf1 = Pf0 - o1 + G2;
//  vec2 grad1 = texture2D(permTexture, Pi + o1*ONE).rg * 4.0 - 1.0;
//  float t1 = 0.5 - dot(Pf1, Pf1);
//  float n1;
//  if (t1 < 0.0) n1 = 0.0;
//  else {
//    t1 *= t1;
//    n1 = t1 * t1 * dot(grad1, Pf1);
//  }
//
//  // Noise contribution from last corner
//  vec2 Pf2 = Pf0 - vec2(1.0-2.0*G2);
//  vec2 grad2 = texture2D(permTexture, Pi + vec2(ONE, ONE)).rg * 4.0 - 1.0;
//  float t2 = 0.5 - dot(Pf2, Pf2);
//  float n2;
//  if(t2 < 0.0) n2 = 0.0;
//  else {
//    t2 *= t2;
//    n2 = t2 * t2 * dot(grad2, Pf2);
//  }
//
//  // Sum up and scale the result to cover the range [-1,1]
//  return 70.0 * (n0 + n1 + n2);
//}


void main() {
    //lighting
    vec3 unitNormal = normalize(pass_normal);
    vec3 unitTowardsLightVector = normalize(pass_towardsLightVector);
    float brightness = dot(unitNormal, unitTowardsLightVector);
    brightness = max(brightness, 0.8);
    //texturing
    vec2 scaledTexCoords = pass_texCoords * 50;
    float multiplier = 50.0;
    float perlin = (snoise(vec3(multiplier*pass_texCoords, 0.0))
                  + 0.5*snoise(vec3(2*multiplier*pass_texCoords, 2.0))
                  + 0.25*snoise(vec3(4*multiplier*pass_texCoords, 4.0))
                  + 0.125*snoise(vec3(8*multiplier*pass_texCoords, 6.0))
                  + 0.0625*snoise(vec3(16*multiplier*pass_texCoords, 8.0)));
    perlin = clamp(perlin, 0, 1);

    vec2 texCoordsOfLL = ivec2(pass_texCoords * 1024) / 1024.0;
    vec2 texCoordsOfLR = ivec2((pass_texCoords * 1024) + vec2(1, 0)) / 1024.0;
    vec2 texCoordsOfUL = ivec2((pass_texCoords * 1024) + vec2(0, 1)) / 1024.0;
    vec2 texCoordsOfUR = ivec2((pass_texCoords * 1024) + vec2(1, 1)) / 1024.0;
    float xDistanceFromR = texCoordsOfLR.x - pass_texCoords.x;
    float xDistanceFromL = pass_texCoords.x - texCoordsOfLL.x;
    float yDistanceFromU = texCoordsOfUL.y - pass_texCoords.y;
    float yDistanceFromD = pass_texCoords.y - texCoordsOfLL.y;
    float length = texCoordsOfLR.x - texCoordsOfLL.x;
    float weightLL = (yDistanceFromU / length) * (xDistanceFromR / length);
    float weightLR = (yDistanceFromU / length) * (xDistanceFromL / length);
    float weightUL = (yDistanceFromD / length) * (xDistanceFromR / length);
    float weightUR = (yDistanceFromD / length) * (xDistanceFromL / length);

    vec4 grassDesertMountainSnowLL = texture(grassDesertMountainSnowSampler, texCoordsOfLL) * weightLL;
    vec4 grassDesertMountainSnowLR = texture(grassDesertMountainSnowSampler, texCoordsOfLR) * weightLR;
    vec4 grassDesertMountainSnowUL = texture(grassDesertMountainSnowSampler, texCoordsOfUL) * weightUL;
    vec4 grassDesertMountainSnowUR = texture(grassDesertMountainSnowSampler, texCoordsOfUR) * weightUR;
    vec4 grassDesertMountainSnow = (grassDesertMountainSnowLL + grassDesertMountainSnowLR
                            + grassDesertMountainSnowUL + grassDesertMountainSnowUR);

    vec4 waterForestLL = texture(waterForestSampler, texCoordsOfLL) * weightLL;
    vec4 waterForestLR = texture(waterForestSampler, texCoordsOfLR) * weightLR;
    vec4 waterForestUL = texture(waterForestSampler, texCoordsOfUL) * weightUL;
    vec4 waterForestUR = texture(waterForestSampler, texCoordsOfUR) * weightUR;
    vec4 waterForest = (waterForestLL + waterForestLR + waterForestUL + waterForestUR);

    vec4 blendColorGrassGrassland = texture(grassTextureSampler, scaledTexCoords) * grassDesertMountainSnow.x * (1.0 - perlin);
    vec4 blendColorDesertGrassland = texture(desertTextureSampler, scaledTexCoords) * grassDesertMountainSnow.x * perlin;
    vec4 blendColorSandDesert = texture(desertTextureSampler, scaledTexCoords) * grassDesertMountainSnow.y * (1.0 - perlin);
    vec4 blendColorGrassDesert = texture(grassTextureSampler, scaledTexCoords) * grassDesertMountainSnow.y * perlin;
    vec4 blendColorMountain = texture(mountainTextureSampler, scaledTexCoords) * grassDesertMountainSnow.z;
    vec4 blendColorSnow = texture(snowTextureSampler, scaledTexCoords) * grassDesertMountainSnow.w;

    vec4 blendColorForest = texture(forestTextureSampler, scaledTexCoords) * waterForest.y;


//    out_color =  texture();
    out_color =  texture();
    out_color = (blendColorGrassGrassland
                + blendColorDesertGrassland
                + blendColorSandDesert
                + blendColorGrassDesert
                + blendColorMountain
                + blendColorSnow + blendColorForest)
                * brightness
                * vec4(lightColor, 1);
}
