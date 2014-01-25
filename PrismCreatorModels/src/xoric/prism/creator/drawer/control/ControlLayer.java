package xoric.prism.creator.drawer.control;

import xoric.prism.creator.drawer.model.DrawerModel;

class ControlLayer
{
	protected IBusyControl busyControl;
	protected DrawerModel model;

	public ControlLayer(DrawerModel model, IBusyControl busyControl)
	{
		this.busyControl = busyControl;
		this.model = model;
	}

	public void setModel(DrawerModel model)
	{
		this.model = model;
	}
}
