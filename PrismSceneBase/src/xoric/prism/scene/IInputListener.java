package xoric.prism.scene;

public interface IInputListener
{
	public boolean onMouseDown(int button);

	public void onMouseUp(int button);

	public void onMouseMove();

	public boolean onControlKey(int keyCode, boolean isDown);

	public boolean onCharacterKey(char c, boolean isDown);
}