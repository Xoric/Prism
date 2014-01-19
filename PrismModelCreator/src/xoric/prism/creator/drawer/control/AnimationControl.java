package xoric.prism.creator.drawer.control;

import xoric.prism.creator.drawer.model.AnimationModel;
import xoric.prism.creator.drawer.model.DrawerModel;
import xoric.prism.creator.drawer.model.VariationList;
import xoric.prism.world.animations.AnimationIndex;

public class AnimationControl extends ControlLayer
{
	public AnimationControl(DrawerModel model, IBusyControl busyControl)
	{
		super(model, busyControl);
	}

	public void deleteAnimation(AnimationIndex animation)
	{
		System.out.println("requestDeleteAnimation(" + animation + ")");

		//		view.displayAnimation(animation, false);
	}

	public void setDuration(AnimationIndex a, int variation, int ms)
	{
		if (ms < 100)
			ms = 100;
		else if (ms > 10000)
			ms = 10000;

		VariationList l = model.getAnimation(a);
		AnimationModel m = l.getVariation(variation);
		m.setDurationMs(ms);
	}
}
