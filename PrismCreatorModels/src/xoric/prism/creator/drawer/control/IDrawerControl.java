package xoric.prism.creator.drawer.control;

public interface IDrawerControl extends IModelControl, IAnimationControl, ISpriteControl
{
	public void initialize();

	public void requestExit();
}