uniform sampler2D tex;
uniform vec4 color = vec4(1.0, 1.0, 1.0, 1.0);

void main()
{
	vec4 texel = texture2D(tex, gl_TexCoord[0].st);
	texel.rgba *= color.rgba;
	
	gl_FragColor = texel;
}