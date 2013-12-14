package xoric.prism.scene;

import xoric.prism.data.IFloatPoint_r;

public interface IRenderer
{
	public void setColor(float r, float g, float b);

	public void bindTexture(int programID);

	public void unbindTexture();

	public void drawPlane(IFloatPoint_r position, IFloatPoint_r size);

	public void drawObject(IFloatPoint_r position, IFloatPoint_r size, float dz);

	public void drawSprite(float x, float y, float w, float h);
}