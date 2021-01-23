#version 400

in vec3 position;
in vec2 textureCoords;

out vec2 pass_textureCoords;

uniform mat4 transformationMatrix;
uniform mat4 projectionMatrix;
uniform mat4 viewMatrix;

void main() {
    gl_Position = projectionMatrix * viewMatrix * transformationMatrix * vec4(position, 1.0);  // gl_Position is a vec4 so we need to convert our vec3 position 
    pass_textureCoords = textureCoords;
}