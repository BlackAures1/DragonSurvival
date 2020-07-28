#version 120

varying vec2 texcoord;
varying vec4 color;

uniform float angle;
uniform int width;
uniform int height;
uniform sampler2D texture;
uniform float time;

void main() 
{
    vec4 original = texture2D(texture, texcoord) * color;
    
    float w = (original.r + original.g + original.b) / 3;
    
    vec4 bleached = vec4(w, w, w, original.a);
    
    vec2 uv = gl_FragCoord.xy / vec2(width, height);
    
    vec4 col = vec4(0.6 + 0.4*cos(angle+uv.xyx+vec3(0,2,4)*time), 1);

    gl_FragColor = bleached * col;
}