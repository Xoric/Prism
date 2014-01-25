package xoric.prism.creator.custom.view;

import java.awt.GridBagLayout;

import javax.swing.JLabel;
import javax.swing.JScrollPane;

import xoric.prism.swing.PrismFrame;

public class CustomView extends PrismFrame
{
	private static final long serialVersionUID = 1L;

	private final JLabel imageLabel;
	private final JScrollPane scroll;

	public CustomView()
	{
		super("Sprite Collections", 640, 400, true);
		setLayout(new GridBagLayout());

		imageLabel = new JLabel();
		scroll = new JScrollPane(imageLabel);

	}

	public static void main(String[] args)
	{
		CustomView v = new CustomView();
		v.setVisible(true);
	}
}
