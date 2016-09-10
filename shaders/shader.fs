#version 120

#define point_color vec3(1,1,1)

void main() {

    vec4 color = gl_Color;
    gl_FragColor = vec4(color);

}