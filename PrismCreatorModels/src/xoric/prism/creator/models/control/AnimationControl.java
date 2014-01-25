package xoric.prism.creator.models.control;

import xoric.prism.creator.models.model.AnimationModel;
import xoric.prism.creator.models.model.ModelModel;
import xoric.prism.creator.models.model.VariationList;
import xoric.prism.world.animations.AnimationIndex;

public class AnimationControl extends ControlLayer
{
	public AnimationControl(ModelModel model, IBusyControl busyControl)
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
