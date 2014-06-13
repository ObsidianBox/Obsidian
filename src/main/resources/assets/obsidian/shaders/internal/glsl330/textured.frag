// $shader_type: fragment

// $texture_layout: diffuse = 0

#version 330

in vec2 textureUV;

layout(location = 0) out vec4 outputColor;

uniform sampler2D diffuse;

void main() {
    outputColor = texture(diffuse, textureUV);
}


