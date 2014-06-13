// $shader_type: vertex

// $attrib_layout: position = 0
// $attrib_layout: textureCoords = 2

#version 120

attribute vec3 position;
attribute vec2 textureCoords;

uniform mat4 modelMatrix;
uniform mat4 viewMatrix;
uniform mat4 projectionMatrix;

varying vec2 textureUV;

void main() {
    textureUV = textureCoords;

    gl_Position = projectionMatrix * viewMatrix * modelMatrix * vec4(position, 1);
}

