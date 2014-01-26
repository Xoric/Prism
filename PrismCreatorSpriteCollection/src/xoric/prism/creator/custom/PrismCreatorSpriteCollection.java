package xoric.prism.creator.custom;

import xoric.prism.creator.custom.control.SpriteCollectionControl;
import xoric.prism.creator.custom.view.SpriteCollectionView;
import xoric.prism.data.PrismDataLoader;
import xoric.prism.data.exceptions.PrismException;
import xoric.prism.data.global.Prism;
import xoric.prism.global.PrismGlobal;

public class PrismCreatorSpriteCollection
{
	private static SpriteCollectionView view;
	private static SpriteCollectionControl control;

	public static void main(String[] args)
	{
		try
		{
			// global initialization
			PrismGlobal.setLookAndFeel();
			PrismGlobal global = new PrismGlobal();
			global.load();
			Prism.global = global;

			// initialize
			PrismDataLoader.loadAll();

			// setup ModelCreator
			view = new SpriteCollectionView();
			control = new SpriteCollectionControl(view);
			view.setControl(control);

			// start creator
			view.start();
		}
		catch (PrismException e)
		{
			e.user.showMessage();
			e.code.print();
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
	}
}