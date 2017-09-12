#version 400 core
uniform sampler2D textureSampler;
uniform vec3 color;

in vec2 pass_texCoords;

out vec4 out_color;

void main(){

//   smoothstep();
   out_color = texture(textureSampler, pass_texCoords);
   out_color = vec4(color, out_color.w);
}