package xoric.prism.scene.lwjgl.shaders;

import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL13;
import org.lwjgl.opengl.GL20;

import xoric.prism.scene.art.ITexture;
import xoric.prism.scene.shaders.IMaskMaskShader;

public class MaskMaskShader extends MaskShader implements IMaskMaskShader
{
	private int mask2Location;

	public MaskMaskShader(int programID)
	{
		super(programID);
	}

	@Override
	public void initialize()
	{
		super.initialize();
		mask2Location = GL20.glGetUniformLocation(super.programID, "mask2");
	}

	// IMask2Shader:
	@Override
	public void setMask2(ITexture texture)
	{
		GL13.glActiveTexture(GL13.GL_TEXTURE2);
		int programID = texture.getTextureID();
		GL11.glBindTexture(GL11.GL_TEXTURE_2D, programID);
		GL20.glUniform1i(mask2Location, 2);
	}

	// ShaderLWJGL:
	@Override
	protected String getShaderName()
	{
		return "mask2 shader";
	}
}
