package xoric.prism.scene.lwjgl.shaders;

import org.lwjgl.opengl.ARBShaderObjects;
import org.lwjgl.opengl.GL20;

import xoric.prism.scene.lwjgl.ICleanUp;
import xoric.prism.scene.shaders.IBaseShader;

/**
 * @author XoricLee
 * @since 25.02.2014, 12:33:17
 */
abstract class ShaderBase implements IBaseShader, ICleanUp
{
	protected int programID;

	public ShaderBase(int programID)
	{
		this.programID = programID;
	}

	@Override
	public void activate()
	{
		ARBShaderObjects.glUseProgramObjectARB(programID);
	}

	@Override
	public void initialize()
	{
		ARBShaderObjects.glUseProgramObjectARB(programID);
	}

	protected abstract String getShaderName();

	@Override
	public final String toString()
	{
		return getShaderName();
	}

	// ICleanUp:
	@Override
	public final void cleanUp() throws Exception
	{
		if (programID > 0)
		{
			GL20.glDeleteShader(programID);
			programID = 0;
		}
	}
}
