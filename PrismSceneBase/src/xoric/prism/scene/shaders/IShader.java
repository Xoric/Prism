package xoric.prism.scene.shaders;

import java.nio.ByteBuffer;

import xoric.prism.data.exceptions.PrismException;

public interface IShader
{
	public void createShader(ByteBuffer vertexShader, ByteBuffer pixelShader) throws PrismException;
}
