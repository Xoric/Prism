package xoric.prism.scene;

import xoric.prism.data.types.IFloatPoint_r;

public interface IInputListener
{
	public boolean onMouseDown(IFloatPoint_r mouse, boolean isLeft);

	public void onMouseUp(IFloatPoint_r mouse, boolean isLeft);

	public void onMouseMove(IFloatPoint_r mouse);

	public boolean onControlKey(int keyCode, boolean isDown);

	public boolean onCharacterKey(char c, boolean isDown);
}