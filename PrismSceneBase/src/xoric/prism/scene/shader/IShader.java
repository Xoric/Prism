package xoric.prism.scene.shader;

import java.nio.ByteBuffer;

import xoric.prism.data.exceptions.PrismException;

public interface IShader
{
	public void createShader(ByteBuffer vertexShader, ByteBuffer pixelShader) throws PrismException;
}
