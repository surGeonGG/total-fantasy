#version 400 core
uniform sampler2D textureAtlas;
uniform vec3 color;

in vec2 pass_texCoords;

out vec4 out_color;

void main(){



//   smoothstep();
   alpha = texture(textureAtlas, pass_texCoords).w;
   out_color = texture(textureAtlas, pass_texCoords);




   if (pass_texCoords == vec2(0.0, 0.0)) {
        out_color = vec4(color, 1);
   }
}