// $shader_type: fragment

// $texture_layout: diffuse = 0

#version 120

varying vec2 textureUV;

uniform sampler2D diffuse;

void main() {
    gl_FragData[0] = texture2D(diffuse, textureUV);
}
