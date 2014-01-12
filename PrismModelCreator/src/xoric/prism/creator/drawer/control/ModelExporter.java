package xoric.prism.creator.drawer.control;

import xoric.prism.creator.drawer.model.DrawerModel;

public class ModelExporter
{
	private final DrawerModel model;

	public ModelExporter(DrawerModel model)
	{
		this.model = model;
	}

	public void start()
	{
		System.out.println("export model");
	}
}