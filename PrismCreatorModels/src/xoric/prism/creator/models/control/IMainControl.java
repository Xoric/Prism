package xoric.prism.creator.models.control;

public interface IMainControl extends IModelControl, IAnimationControl, ISpriteControl
{
	public void initialize();

	public void requestExit();
}