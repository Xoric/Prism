package xoric.prism.creator.drawer.control;

import xoric.prism.creator.drawer.model.AnimationModel;
import xoric.prism.creator.drawer.model.DrawerModel;
import xoric.prism.world.animations.AnimationIndex;

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

	public void setDuration(AnimationIndex a, int ms)
	{
		if (ms < 100)
			ms = 100;
		else if (ms > 10000)
			ms = 10000;

		AnimationModel m = model.getAnimation(a);
		m.setDurationMs(ms);
	}
}
