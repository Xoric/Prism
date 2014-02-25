package xoric.prism.scene.lwjgl.shaders;

import xoric.prism.scene.lwjgl.ICleanUp;
import xoric.prism.scene.shaders.IBaseShader;

/**
 * @author XoricLee
 * @since 25.02.2014, 12:33:17
 */
abstract class ShaderBase implements IBaseShader, ICleanUp
{
	protected abstract String getShaderName();

	@Override
	public final String toString()
	{
		return getShaderName();
	}
}
