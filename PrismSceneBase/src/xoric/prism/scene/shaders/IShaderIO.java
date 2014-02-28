package xoric.prism.scene.shaders;

import java.nio.ByteBuffer;

import xoric.prism.data.exceptions.PrismException;

public interface IShaderIO
{
	public IColorShader createDefaultShader(ByteBuffer vertexShader, ByteBuffer pixelShader) throws PrismException;

	public IMaskShader createMaskShader(ByteBuffer vertexShader, ByteBuffer pixelShader) throws PrismException;

	public IMaskMaskShader createMaskMaskShader(ByteBuffer vertexShader, ByteBuffer pixelShader) throws PrismException;

	public IColorMaskShader createColorMaskShader(ByteBuffer vertexShader, ByteBuffer pixelShader) throws PrismException;

	public IColorShader createShaderSubstitute();
}
