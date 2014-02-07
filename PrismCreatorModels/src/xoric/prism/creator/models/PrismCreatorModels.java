package xoric.prism.creator.models;

import xoric.prism.creator.models.control.MainControl;
import xoric.prism.creator.models.view.MainView;
import xoric.prism.data.PrismDataLoader;
import xoric.prism.data.exceptions.PrismException;
import xoric.prism.data.global.Prism;
import xoric.prism.global.PrismGlobal;
import xoric.prism.world.PrismWorldLoader;

public abstract class PrismCreatorModels
{
	private static MainView view;
	private static MainControl control;

	public static void main(String[] args)
	{
		try
		{
			// global initialization
			PrismGlobal.setLookAndFeel();
			PrismGlobal global = new PrismGlobal();
			global.init();
			global.load();
			Prism.global = global;

			// initialize
			PrismDataLoader.loadAll();
			PrismWorldLoader.loadAll(true);

			// setup ModelCreator
			view = new MainView();
			control = new MainControl(view);
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
