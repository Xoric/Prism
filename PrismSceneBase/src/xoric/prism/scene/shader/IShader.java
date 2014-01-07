package xoric.prism.scene.shader;

import java.nio.ByteBuffer;

import xoric.prism.data.exceptions2.PrismException2;

public interface IShader
{
	public void createShader(ByteBuffer vertexShader, ByteBuffer pixelShader) throws PrismException2;
}
