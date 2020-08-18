#version 130

varying vec2 texcoord;
varying vec4 color;

void main() 
{
    gl_Position = gl_ModelViewProjectionMatrix * gl_Vertex;
    texcoord = vec2(gl_MultiTexCoord0);
    color = gl_Color;
}