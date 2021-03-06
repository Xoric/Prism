package xoric.prism.server.view;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;

import javax.swing.JPanel;

import xoric.prism.com.MessageDispatcher;
import xoric.prism.data.global.Prism;
import xoric.prism.data.tools.Common;
import xoric.prism.server.control.IServerControl;
import xoric.prism.server.net.INetworkStatus;
import xoric.prism.swing.PrismFrame;

public class ServerView extends PrismFrame implements IServerView
{
	private static final long serialVersionUID = 1L;

	private IServerControl control;

	private final NetView netView;
	private final ServerConsole console;

	public ServerView()
	{
		super("Prism Server", 600, 400, true);

		JPanel p0 = new JPanel(new GridBagLayout());
		GridBagConstraints c = new GridBagConstraints(0, 0, 1, 1, 1.0, 1.0, GridBagConstraints.CENTER, GridBagConstraints.BOTH, new Insets(
				15, 15, 15, 15), 0, 0);

		JPanel p = new JPanel(new GridBagLayout());
		p0.add(p, c);

		this.setContentPane(p0);
		this.centerOnScreen();

		c = new GridBagConstraints(0, 0, 1, 1, 1.0, 1.0, GridBagConstraints.CENTER, GridBagConstraints.BOTH, new Insets(0, 0, 15, 0), 0, 0);
		p.add(console = new ServerConsole(), c);

		c.fill = GridBagConstraints.NONE;
		c.gridy++;
		c.weighty = 0.0;
		NetView n = new NetView();
		this.netView = n;
		p.add(n, c);
	}

	public void register(IServerControl control, INetworkStatus network)
	{
		this.netView.register(control, network);
	}

	private void printFileVersions()
	{
		boolean isFirst = true;
		StringBuilder sb = new StringBuilder();
		sb.append("initialized file table: ");

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

	private void printUrgency()
	{
		StringBuilder sb = new StringBuilder();
		sb.append("initialized message urgency: ");

		for (int i = 0; i < MessageDispatcher.thresholds.length; ++i)
			sb.append((i > 0 ? ";" : "") + MessageDispatcher.thresholds[i]);

		System.out.println(sb.toString());
	}

	private void printMemory()
	{
		long maxMemory = Runtime.getRuntime().maxMemory();
		String s = maxMemory == Long.MAX_VALUE ? "" : " (max. " + Common.getFileSize(maxMemory) + ")";
		System.out.println("JVM memory: " + Common.getFileSize(Runtime.getRuntime().totalMemory()) + " of "
				+ Common.getFileSize(Runtime.getRuntime().totalMemory()) + s);
	}

	@Override
	public void printWelcome()
	{
		System.out.println("server application started");
		System.out.println("available processor cores: " + Runtime.getRuntime().availableProcessors());
		printMemory();
		printFileVersions();
		printUrgency();
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
