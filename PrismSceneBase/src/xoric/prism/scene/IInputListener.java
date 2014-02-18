package xoric.prism.scene;

import xoric.prism.data.types.IFloatPoint_r;

public interface IInputListener
{
	public void mouseMove(IFloatPoint_r mouse);

	public void onMouseDown(IFloatPoint_r mouse, boolean isLeft);

	public void onMouseUp(IFloatPoint_r mouse, boolean isLeft);

	public void onControlKey(int keyCode, boolean isDown);

	public void onCharacterKey(char c, boolean isDown);
}