package xoric.prism.scene.shaders;

import java.nio.ByteBuffer;

import xoric.prism.data.exceptions.PrismException;

public interface IShaderIO
{
	public IDefaultShader createDefaultShader(ByteBuffer vertexShader, ByteBuffer pixelShader) throws PrismException;

	public IMaskShader createMaskShader(ByteBuffer vertexShader, ByteBuffer pixelShader) throws PrismException;

	public IDefaultShader createShaderSubstitute();
}
