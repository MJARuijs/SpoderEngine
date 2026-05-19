#version 450

struct AmbientLight {
    vec4 color;
};

struct DirectionalLight {
    vec4 color;
    vec3 direction;
};

struct Material {
    vec4 diffuse;
    vec4 specular;
    float shininess;
};

in vec3 passNormal;

uniform AmbientLight ambient;
uniform DirectionalLight directional;
uniform Material material;

out vec4 outColor;

void main() {

    vec4 ambientColor = material.diffuse * ambient.color;
    vec4 sunColor = material.diffuse * dot(passNormal, directional.direction);

    outColor = ambientColor + sunColor;

}
