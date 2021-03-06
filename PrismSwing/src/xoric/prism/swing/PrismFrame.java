package xoric.prism.swing;

import java.awt.Dimension;
import java.awt.Toolkit;

import javax.swing.JFrame;

import xoric.prism.data.types.IRect_r;

public class PrismFrame extends JFrame
{
	private static final long serialVersionUID = 1L;

	public PrismFrame(String title, int width, int height, boolean exitOnClose, IRect_r parent)
	{
		// initialize frame
		init(title, width, height, exitOnClose);

		// center on parent
		centerOnParent(parent.getX(), parent.getY(), parent.getWidth(), parent.getHeight());
	}

	public PrismFrame(String title, int width, int height, boolean exitOnClose)
	{
		// initialize frame
		init(title, width, height, exitOnClose);

		// center on screen
		Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
		centerOnParent(0, 0, screenSize.width, screenSize.height);
	}

	private void init(String title, int width, int height, boolean exitOnClose)
	{
		// initialize frame
		this.setTitle(title);
		this.setSize(width, height);

		// set default close operation
		if (exitOnClose)
			this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	}

	public void centerOnScreen()
	{
		Dimension dim = Toolkit.getDefaultToolkit().getScreenSize();
		this.setLocation(dim.width / 2 - this.getSize().width / 2, dim.height / 2 - this.getSize().height / 2 + 30);
	}

	private void centerOnParent(int x, int y, int width, int height)
	{
		setLocation(x + (width - this.getWidth()) / 2, y + (height - this.getWidth()) / 2);
	}
}
