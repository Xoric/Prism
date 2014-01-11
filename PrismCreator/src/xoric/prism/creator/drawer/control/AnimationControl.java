package xoric.prism.creator.drawer.control;

import xoric.prism.creator.drawer.model.DrawerModel;
import xoric.prism.world.entities.AnimationIndex;

public class AnimationControl extends ControlLayer
{
	public AnimationControl(DrawerModel model, IBusyControl busyControl)
	{
		super(model, busyControl);
	}

	public void addAnimation(AnimationIndex animation)
	{
		System.out.println("requestAddAnimation(" + animation + ")");

		//		view.displayAnimation(animation, true);
	}

	public void deleteAnimation(AnimationIndex animation)
	{
		System.out.println("requestDeleteAnimation(" + animation + ")");

		//		view.displayAnimation(animation, false);
	}
}
