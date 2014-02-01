package xoric.prism.creator.grid;

import javax.swing.UIManager;

import xoric.prism.creator.grid.control.GridControl;
import xoric.prism.creator.grid.view.GridView;
import xoric.prism.data.PrismDataLoader;

public abstract class PrismCreatorGrid
{
	private static GridView view;
	private static GridControl control;

	public static void main(String[] args)
	{
		try
		{
			setLookAndFeel();

			// initialize
			PrismDataLoader.loadAll();

			// setup ModelCreator
			view = new GridView();
			control = new GridControl(view);
			view.setControl(control);

			// start creator
			view.start();
		}
		//		catch (PrismException e)
		//		{
		//			e.user.showMessage();
		//			e.code.print();
		//		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
	}

	public static void setLookAndFeel()
	{
		try
		{
			UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
		}
		catch (Exception e)
		{
		}
	}
}
