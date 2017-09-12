#version 400 core


uniform sampler2D waterTextureSampler;
uniform vec3 lightColor;

in vec2 pass_texCoords;
in vec3 pass_normal;
in vec3 pass_towardsLightVector;
in float pass_visibility;


out vec4 out_color;

const vec3 skyColor = vec3(0.40,0.70,0.92);


void main() {
    vec3 unitNormal = normalize(pass_normal);
    vec3 unitTowardsLightVector = normalize(pass_towardsLightVector);
    float brightness = dot(unitNormal, unitTowardsLightVector);
    brightness = max(brightness, 0.3);
    vec2 scaledTexCoords = pass_texCoords * 50;
//    out_color = texture(waterTextureSampler, scaledTexCoords) * brightness * vec4(lightColor, 1);
    out_color = vec4(0.266, 0.266, 0.68, 1) * brightness * vec4(lightColor, 1);

    out_color = mix(vec4(skyColor, 1.0), out_color, pass_visibility);
}
