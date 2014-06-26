// $shader_type: vertex

#version 330

layout(location = 0) in vec3 position;
layout(location = 2) in vec2 textureCoords;

uniform mat4 modelMatrix;
uniform mat4 viewMatrix;
uniform mat4 normalMatrix;
uniform mat4 projectionMatrix;

out vec2 textureUV;

void main() {
    textureUV = textureCoords;

    gl_Position = projectionMatrix * viewMatrix * modelMatrix * vec4(position, 1);
}
