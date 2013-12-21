package xoric.prism.scene.shader;

import java.nio.ByteBuffer;

import xoric.prism.scene.exceptions.PrismShaderException;

public interface IShader
{
	public void createShader(ByteBuffer vertexShader, ByteBuffer pixelShader) throws PrismShaderException;
}
