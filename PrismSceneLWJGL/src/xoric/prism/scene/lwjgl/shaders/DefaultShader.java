package xoric.prism.scene.lwjgl.shaders;

import org.lwjgl.opengl.ARBShaderObjects;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL13;
import org.lwjgl.opengl.GL20;

import xoric.prism.data.types.PrismColor;
import xoric.prism.scene.shaders.IDefaultShader;
import xoric.prism.scene.textures.ITexture;

public class DefaultShader extends ShaderBase implements IDefaultShader
{
	protected int programID;

	private int textureLocation;
	private int colorLocation;

	private boolean isColorSet;

	public DefaultShader(int program)
	{
		this.programID = program;
		this.isColorSet = true;
	}

	@Override
	public void activate()
	{
		ARBShaderObjects.glUseProgramObjectARB(programID);

		if (isColorSet)
			setColor(PrismColor.opaqueWhite);
	}

	@Override
	public void setTexture(ITexture texture)
	{
		GL13.glActiveTexture(GL13.GL_TEXTURE0);
		int programID = texture.getTextureID();
		GL11.glBindTexture(GL11.GL_TEXTURE_2D, programID);
		//		int textureLocation = gl2.glGetUniformLocationARB(program, "tex");
		GL20.glUniform1i(textureLocation, 0); // use texture unit 0
	}

	@Override
	public void setColor(PrismColor color)
	{
		this.isColorSet = color != PrismColor.opaqueWhite;
		//		int colorLocation = GL20.glGetUniformLocation(programID, "color");
		float[] rgba = color.getRGBA();
		GL20.glUniform4f(colorLocation, rgba[0], rgba[1], rgba[2], rgba[3]);
	}

	@Override
	public void initialize()
	{
		ARBShaderObjects.glUseProgramObjectARB(programID);
		textureLocation = GL20.glGetUniformLocation(this.programID, "tex");
		colorLocation = GL20.glGetUniformLocation(this.programID, "color");
	}

	// ICleanUp:
	@Override
	public void cleanUp() throws Exception
	{
		if (programID > 0)
		{
			GL20.glDeleteShader(programID);
			programID = 0;
		}
	}

	// ShaderLWJGL:
	@Override
	protected String getShaderName()
	{
		return "default shader";
	}
}
