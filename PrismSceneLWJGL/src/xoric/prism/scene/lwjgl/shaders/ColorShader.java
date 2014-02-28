package xoric.prism.scene.lwjgl.shaders;

import org.lwjgl.opengl.GL20;

import xoric.prism.data.types.PrismColor;
import xoric.prism.scene.shaders.IColorShader;

public class ColorShader extends TextureShader implements IColorShader
{
	private int colorLocation;
	private boolean isColorSet;

	public ColorShader(int programID)
	{
		super(programID);
		this.isColorSet = true;
	}

	@Override
	public void activate()
	{
		super.activate();

		if (isColorSet)
			setColor(PrismColor.opaqueWhite);
	}

	@Override
	public final void setColor(PrismColor color)
	{
		this.isColorSet = color != PrismColor.opaqueWhite;
		float[] rgba = color.getRGBA();
		GL20.glUniform4f(colorLocation, rgba[0], rgba[1], rgba[2], rgba[3]);
	}

	@Override
	public void initialize()
	{
		super.initialize();
		colorLocation = GL20.glGetUniformLocation(super.programID, "color");
	}

	// ShaderLWJGL:
	@Override
	protected String getShaderName()
	{
		return "color shader";
	}
}
