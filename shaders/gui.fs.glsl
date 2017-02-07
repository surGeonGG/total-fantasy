#version 400 core

uniform sampler2D textureSampler;
in vec2 texCoords;
out vec4 out_color;

void main(){
//   out_color = texture(textureSampler, texCoords);
   out_color = vec4(1,1,1,0.5f);
}