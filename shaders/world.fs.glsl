#version 400 core

flat in vec4 color;

out vec4 out_color;



/*float rand(vec2 co) {
        return fract(sin(dot(co.xy ,vec2(12.9898,78.233))) * 43758.5453);
    }*/

void main() {

    //vec4 color = gl_Color;

    /*float random = rand(vec2((gl_FragCoord.x * color.g * 5435413) / (gl_FragCoord.y * 45646), (gl_FragCoord.y * color.r * 85662) / (gl_FragCoord.x * 457896)));
    float random2 = rand(vec2((gl_FragCoord.x * color.g * 345) / (gl_FragCoord.y * 8768), (gl_FragCoord.y * color.r * 234234) / (gl_FragCoord.x * 457896)));
    float random3 = rand(vec2((gl_FragCoord.x * color.g * 456745) / (gl_FragCoord.y * 6345), (gl_FragCoord.y * color.r * 34534) / (gl_FragCoord.x * 457896)));*/

    //gl_FragColor = vec4(color.r - random / 10, color.g - random2 / 10, color.b - random3 / 10, color.a);

    //gl_FragColor = vec4(color.r + (meshXYCopy.y * verticesCopy.y /20), color.g + (meshXYCopy.y * verticesCopy.y /20), color.b + (meshXYCopy.y * verticesCopy.y /20), color.a);

    out_color = color;
}
