package xoric.prism.server.view;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;

import javax.swing.JPanel;

import xoric.prism.server.control.IServerControl;
import xoric.prism.server.main.ServerModel;
import xoric.prism.swing.PrismFrame;

public class ServerView extends PrismFrame implements IServerView
{
	private static final long serialVersionUID = 1L;

	private final ServerModel model;
	private IServerControl control;

	private final INetView netView;
	private final ServerConsole console;

	public ServerView(ServerModel m)
	{
		super("Prism Server", 600, 400, true);
		JPanel p = new JPanel(new GridBagLayout());
		this.setContentPane(p);
		this.centerOnScreen();

		this.model = m;

		GridBagConstraints c = new GridBagConstraints(0, 0, 1, 1, 1.0, 1.0, GridBagConstraints.CENTER, GridBagConstraints.BOTH, new Insets(
				0, 0, 0, 0), 0, 0);
		p.add(console = new ServerConsole(), c);

		c.fill = GridBagConstraints.NONE;
		c.gridy++;
		NetView n = new NetView(m.net);
		this.netView = n;
		p.add(n, c);
	}

	public void setControl(IServerControl control)
	{
		this.control = control;
		this.netView.setControl(control);
	}

	@Override
	public void displayAll()
	{
		netView.displayAll();
	}

	@Override
	public INetView getNetView()
	{
		return netView;
	}
}
