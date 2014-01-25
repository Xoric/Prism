package xoric.prism.server.view;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;

import javax.swing.JPanel;

import xoric.prism.data.global.Prism;
import xoric.prism.server.control.IServerControl;
import xoric.prism.server.model.ServerModel;
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

		JPanel p0 = new JPanel(new GridBagLayout());
		GridBagConstraints c = new GridBagConstraints(0, 0, 1, 1, 1.0, 1.0, GridBagConstraints.CENTER, GridBagConstraints.BOTH, new Insets(
				15, 15, 15, 15), 0, 0);

		JPanel p = new JPanel(new GridBagLayout());
		p0.add(p, c);

		this.setContentPane(p0);
		this.centerOnScreen();

		this.model = m;

		c = new GridBagConstraints(0, 0, 1, 1, 1.0, 1.0, GridBagConstraints.CENTER, GridBagConstraints.BOTH, new Insets(0, 0, 15, 0), 0, 0);
		p.add(console = new ServerConsole(), c);

		c.fill = GridBagConstraints.NONE;
		c.gridy++;
		c.weighty = 0.0;
		NetView n = new NetView(m.net);
		this.netView = n;
		p.add(n, c);
	}

	public void setControl(IServerControl control)
	{
		this.control = control;
		this.netView.setControl(control);
	}

	private void printFileVersions()
	{
		boolean isFirst = true;
		StringBuilder sb = new StringBuilder();
		sb.append("registered file table (" + Prism.global.getVersionHeap().ints.size() + " files): ");

		for (int v : Prism.global.getVersionHeap().ints)
		{
			if (isFirst)
				isFirst = false;
			else
				sb.append(";");

			sb.append(String.valueOf(v));
		}
		System.out.println(sb.toString());
	}

	@Override
	public void printWelcome()
	{
		System.out.println("server application started");
		printFileVersions();
		System.out.println();
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
