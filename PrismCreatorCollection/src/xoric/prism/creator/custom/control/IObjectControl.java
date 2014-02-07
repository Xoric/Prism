package xoric.prism.creator.custom.control;

public interface IObjectControl
{
	public void requestAddObject();

	public void requestDeleteObject(int index);

	public void requestMoveObject(int index, boolean moveUp);
}
