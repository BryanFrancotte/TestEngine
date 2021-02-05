#version 400

in vec2 pass_textureCoords;
in vec3 surfaceNormal;
in vec3 toLightVector;
in vec3 toCameraVector;

out vec4 out_color;

uniform sampler2D textureSampler;
uniform vec3 lightColour;
uniform float shineDamper;
uniform float reflectivity;

void main() {
	vec3 unitNormal = normalize(surfaceNormal);
	vec3 unitLightVector = normalize(toLightVector);
	
	float nDotl = dot(unitNormal, unitLightVector);
	float brightness = max(nDotl, 0.2);
	vec3 diffuse = brightness * lightColour;
	
	vec3 unitVectorToCamera = normalize(toCameraVector);
	vec3 lightDirection = -unitLightVector;
	vec3 reflectedLightDirection = reflect(lightDirection, unitNormal);
	
	float specularFactor = dot(reflectedLightDirection, unitVectorToCamera);
	specularFactor = max(specularFactor, 0.0);
	float dampedFactor = pow(specularFactor, shineDamper);
	vec3 finalSpecular = dampedFactor * lightColour;
	
	vec4 textureColor = texture(textureSampler, pass_textureCoords);
	if(textureColor.a < 0.5) {
		discard;
	}
	
    out_color = vec4(diffuse, 1.0) * textureColor + vec4(finalSpecular, 1.0);
}