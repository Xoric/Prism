package xoric.prism.swing.gridpanel;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;

import javax.swing.JComponent;
import javax.swing.JPanel;

@Deprecated
public class GridPanel extends JPanel
{
	private static final long serialVersionUID = 1L;

	private GridBagLayout layout;

	public GridPanel()
	{
		layout = new GridBagLayout();
		setLayout(layout);
	}

	public void add(int x, int y, JComponent comp)
	{
		add(x, y, 1, 1, comp);
	}

	public void add(int x, int y, int width, int height, JComponent comp)
	{
		GridBagConstraints c = new GridBagConstraints();
		c.gridx = x;
		c.gridy = y;
		c.gridwidth = width;
		c.gridheight = height;
		this.add(comp, c);
	}
}
