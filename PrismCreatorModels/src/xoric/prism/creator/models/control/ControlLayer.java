package xoric.prism.creator.models.control;

import xoric.prism.creator.models.model.ModelModel;

class ControlLayer
{
	protected IBusyControl busyControl;
	protected ModelModel model;

	public ControlLayer(ModelModel model, IBusyControl busyControl)
	{
		this.busyControl = busyControl;
		this.model = model;
	}

	public void setModel(ModelModel model)
	{
		this.model = model;
	}
}
