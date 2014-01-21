package xoric.prism.server.view;

import xoric.prism.server.control.IServerControl;

public interface INetView
{
	public void setControl(IServerControl control);

	public void displayAll();

	public void displayNetState();
}
