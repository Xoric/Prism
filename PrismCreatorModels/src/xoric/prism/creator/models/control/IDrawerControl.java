package xoric.prism.creator.models.control;

public interface IDrawerControl extends IModelControl, IAnimationControl, ISpriteControl
{
	public void initialize();

	public void requestExit();
}