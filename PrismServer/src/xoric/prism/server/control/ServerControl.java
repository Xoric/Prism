package xoric.prism.server.control;

import xoric.prism.server.model.ServerModel;
import xoric.prism.server.view.IServerView;

public class ServerControl implements IServerControl
{
	private final ServerModel model;
	private IServerView view;

	public ServerControl(ServerModel m)
	{
		this.model = m;
	}

	public void setView(IServerView view)
	{
		this.view = view;
	}

	@Override
	public void requestStartServer(boolean b)
	{
		// modify model
		if (b && !model.net.isActive())
			NetControl.startNet(model.net);
		else if (!b && model.net.isActive())
			NetControl.stopNet(model.net);

		// update view
		view.getNetView().displayNetState();

		// start or stop the loop
		if (b)
			ServerLoop.startThread(model);
		else
			ServerLoop.requestStopThread();
	}
}
