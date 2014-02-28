package xoric.prism.scene.lwjgl.shaders;

import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL13;
import org.lwjgl.opengl.GL20;

import xoric.prism.scene.shaders.IMaskShader;
import xoric.prism.scene.textures.ITexture;

public class MaskShader extends TextureShader implements IMaskShader
{
	private int maskLocation;

	public MaskShader(int programID)
	{
		super(programID);
	}

	@Override
	public void initialize()
	{
		super.initialize();
		maskLocation = GL20.glGetUniformLocation(super.programID, "mask");
	}

	@Override
	public void setMask(ITexture texture)
	{
		GL13.glActiveTexture(GL13.GL_TEXTURE1);
		int programID = texture.getTextureID();
		GL11.glBindTexture(GL11.GL_TEXTURE_2D, programID);
		GL20.glUniform1i(maskLocation, 1);
	}

	// ShaderLWJGL:
	@Override
	protected String getShaderName()
	{
		return "mask shader";
	}
}
