package xoric.prism.server.view;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;

import xoric.prism.server.net.IControlNet;
import xoric.prism.server.net.ServerNet;
import xoric.prism.swing.PrismPanel;

public class NetView extends PrismPanel implements ActionListener, INetView
{
	private static final long serialVersionUID = 1L;

	private final ServerNet net;
	private IControlNet control;

	private final JLabel portLabel;
	private final JLabel stateLabel;

	private final JButton startButton;

	public NetView(ServerNet net)
	{
		super("Network");

		this.net = net;

		JPanel p = new JPanel(new GridBagLayout());
		this.setContent(p);
		GridBagConstraints c;

		Insets i0 = new Insets(0, 0, 0, 0);
		Insets i1 = new Insets(0, 5, 0, 0);

		c = new GridBagConstraints(0, 0, 1, 1, 1.0, 1.0, GridBagConstraints.CENTER, GridBagConstraints.BOTH, i0, 5, 5);
		p.add(createHeaderLabel("Port"), c);
		c.gridx++;
		c.insets = i1;
		p.add(portLabel = createValueLabel(), c);

		c.gridx = 0;
		c.gridy++;
		c.insets = i0;
		p.add(createHeaderLabel("State"), c);
		c.gridx++;
		c.insets = i1;
		p.add(stateLabel = createValueLabel(), c);
		c.gridx++;
		c.weightx = 0.0;
		c.weighty = 0.0;
		p.add(startButton = createButton("", ""), c);
	}

	@Override
	public void setControl(IControlNet control)
	{
		this.control = control;
	}

	private JButton createButton(String s, String tooltip)
	{
		JButton b = new JButton(s);
		b.addActionListener(this);
		b.setToolTipText(tooltip);
		return b;
	}

	private static JLabel createHeaderLabel(String text)
	{
		JLabel l = new JLabel(text);
		l.setBorder(BorderFactory.createEtchedBorder());
		l.setHorizontalAlignment(JLabel.CENTER);
		return l;
	}

	private static JLabel createValueLabel()
	{
		JLabel l = new JLabel();
		return l;
	}

	@Override
	public void displayAll()
	{
		displayNetState();
	}

	@Override
	public void displayNetState()
	{
		boolean b = net.isActive();
		stateLabel.setText(b ? "online" : "offline");
		startButton.setText(b ? "Stop" : "Start");
		portLabel.setText(net.getPort());
	}

	private void onStartButton()
	{
		control.requestStartNet(startButton.getText().equals("Start"));
	}

	@Override
	public void actionPerformed(ActionEvent e)
	{
		Object o = e.getSource();

		if (o == startButton)
			onStartButton();
	}
}