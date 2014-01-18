package xoric.prism.scene.shaders;

import java.nio.ByteBuffer;

import xoric.prism.data.exceptions.PrismException;

public interface IShaderIO
{
	public IShader2 createShader(ByteBuffer vertexShader, ByteBuffer pixelShader) throws PrismException;
}
