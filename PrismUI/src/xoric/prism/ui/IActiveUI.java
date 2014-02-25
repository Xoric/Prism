package xoric.prism.ui;

import xoric.prism.data.exceptions.PrismException;
import xoric.prism.data.types.IFloatPoint_r;

public interface IActiveUI
{
	//	/**
	//	 * Handles KeyEvents.
	//	 * @param e
	//	 * @return Returns if event has been handled
	//	 */
	//	public boolean handleKeyEvent(KeyEvent e)
	//	{
	//		return false;
	//	}
	//
	//	public void keyReleased(int key)
	//	{
	//	}

	public boolean containsMouse(IFloatPoint_r mouse);

	/**
	 * Handles mouse-down events. Returns a reference to the component if the event was handled, null otherwise.
	 * @param mouse
	 *            position of the mouse cursor on the screen
	 * @return IActiveUI
	 */
	public abstract IActiveUI mouseDown(IFloatPoint_r mouse);

	public abstract void mouseUp();

	/**
	 * Handles mouse click events. Automatically invoked by parent {@link UIWindow} if a mouse-down and a mouse-up event have been invoked.
	 * @throws PrismException
	 */
	public abstract void mouseClick() throws PrismException;

	public void setFocus(boolean hasFocus);

	public void onControlKey(int keyCode, boolean isDown);

	public void onCharacterKey(char c, boolean isDown);
}
