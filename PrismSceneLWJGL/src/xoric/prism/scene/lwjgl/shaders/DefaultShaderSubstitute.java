package xoric.prism.scene.lwjgl.shaders;

import org.lwjgl.opengl.GL11;

import xoric.prism.data.types.PrismColor;
import xoric.prism.scene.shaders.IDefaultShader;
import xoric.prism.scene.textures.ITexture;

public class DefaultShaderSubstitute implements IDefaultShader
{
	@Override
	public void activate()
	{
	}

	@Override
	public void setTexture(ITexture texture)
	{
		//		GL11.glBindTexture(GL11.GL_TEXTURE_2D, 0); // TODO: needed?
		GL11.glBindTexture(GL11.GL_TEXTURE_2D, texture.getTextureID());
	}

	@Override
	public void setColor(PrismColor color)
	{
	}

	@Override
	public void initialize()
	{
		GL11.glEnable(GL11.GL_TEXTURE_2D);
	}
}
