package xoric.prism.scene.shaders;

public enum ShaderType
{
	UNKNOWN("unknown"), VERTEX_SHADER("vertshader"), PIXEL_SHADER("pixshader");

	private final String name;

	private ShaderType(String name)
	{
		this.name = name;
	}

	@Override
	public String toString()
	{
		return name;
	}
}
