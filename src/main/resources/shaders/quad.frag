#version 450

uniform vec3 color;

out vec4 outColor;

void main() {
    // outColor = vec4(0.3, 0.7, 0.3, 1.0);
    outColor = vec4(color, 1.0);
}

