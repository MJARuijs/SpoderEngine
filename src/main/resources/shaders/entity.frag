#version 450

layout(location = 0) in vec3 inPosition;

uniform mat4 projection;
uniform mat4 view;
uniform mat4 model;

out vec4 worldPosition;

void main() {
    worldPosition = model * vec4(inPosition, 1.0);

    gl_Position = projection * view * worldPosition;
}
