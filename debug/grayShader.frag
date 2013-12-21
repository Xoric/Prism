uniform sampler2D tex;
uniform vec4 color = vec4(1.0, 1.0, 1.0, 1.0);

void main()
{
	vec4 texel = texture2D(tex, gl_TexCoord[0].st);
	texel.rgba *= color.rgba;
	
	float g = (texel.r * 0.3 + texel.g * 0.59 + texel.b * 0.11);
	texel.r = g;
	texel.g = g;
	texel.b = g;
	
	gl_FragColor = texel;
}