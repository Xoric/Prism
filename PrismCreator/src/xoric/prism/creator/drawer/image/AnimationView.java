package xoric.prism.creator.drawer.image;

import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Image;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JSeparator;

import xoric.prism.creator.drawer.model.AnimationModel;
import xoric.prism.creator.drawer.view.AnimationLine;
import xoric.prism.creator.drawer.view.IAnimationLine;
import xoric.prism.swing.PrismFrame;
import xoric.prism.world.entities.AnimationIndex;
import xoric.prism.world.entities.ViewAngle;

public class AnimationView extends JPanel implements ActionListener, IAnimationView, IAngleSelectorListener
{
	private static final long serialVersionUID = 1L;

	private final JButton backButton;
	//	private final JLabel nameLabel;

	private final IAnimationLine animationLine;
	private IAngleSelector angleSelector;
	private IFramesView framesView;

	private AnimationModel animationModel;

	public AnimationView()
	{
		super(new GridBagLayout());

		AnimationLine l = new AnimationLine(AnimationIndex.IDLE);
		animationLine = l;
		AngleSelectorPanel a = new AngleSelectorPanel(this);
		angleSelector = a;
		SpritesList f = new SpritesList();
		framesView = f;

		backButton = createButton("Back", null);

		Insets insets = new Insets(15, 15, 15, 15);

		// add controls
		GridBagConstraints c = new GridBagConstraints(0, 0, 1, 1, 0.0, 0.0, GridBagConstraints.NORTHWEST, GridBagConstraints.NONE, insets,
				0, 0);
		add(backButton, c);

		c = new GridBagConstraints(1, 0, 1, 1, 1.0, 0.0, GridBagConstraints.WEST, GridBagConstraints.BOTH, insets, 0, 0);
		add(l, c);

		c = new GridBagConstraints(0, 1, 2, 1, 1.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.BOTH, insets, 0, 0);
		add(new JSeparator(), c);

		c = new GridBagConstraints(0, 2, 1, 1, 0.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.NONE, insets, 0, 0);
		add(a/*angleSelector*/, c);

		//		c = new GridBagConstraints(1, 2, 1, 1, 0.0, 0.0, GridBagConstraints.WEST, GridBagConstraints.BOTH, insets, 0, 0);
		//		JScrollPane scroll = new JScrollPane(f /*framesView*/, ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED,
		//				ScrollPaneConstants.HORIZONTAL_SCROLLBAR_AS_NEEDED);
		//		add(scroll, c);
		//
		//		scroll.getVerticalScrollBar().setUnitIncrement(16);
		//		scroll.getHorizontalScrollBar().setUnitIncrement(16);

		c = new GridBagConstraints(1, 2, 1, 1, 0.0, 0.0, GridBagConstraints.WEST, GridBagConstraints.BOTH, insets, 0, 0);
		add(f, c);
	}

	private JButton createButton(String s, String icon)
	{
		JButton b = new JButton();
		b.addActionListener(this);
		boolean hasIcon = false;
		try
		{
			if (icon != null && icon.length() > 0)
			{
				Image img = ImageIO.read(ClassLoader.getSystemResource(icon));
				ImageIcon icn = new ImageIcon(img);
				b.setIcon(icn);
				Dimension d = new Dimension(icn.getIconWidth(), icn.getIconHeight());
				b.setPreferredSize(d);
				b.setMinimumSize(d);
				b.setMaximumSize(d);
				hasIcon = true;
			}
		}
		catch (Exception e)
		{
		}
		if (!hasIcon)
			b.setText(s);

		return b;
	}

	public static void main(String[] args)
	{
		AnimationView p = new AnimationView();

		JFrame f = new PrismFrame("Test", 300, 300, true);
		f.setContentPane(p);

		f.setVisible(true);
	}

	@Override
	public void actionPerformed(ActionEvent e)
	{
		Object o = e.getSource();

		if (o == backButton)
			System.out.println("Back!");
	}

	@Override
	public void displayAnimationImages(AnimationModel m)
	{
		animationModel = m;
		animationLine.displayAnimation(m);

		framesView.loadFrames(animationModel, angleSelector.getAngle());
	}

	@Override
	public void changedAngle(ViewAngle v)
	{
		framesView.loadFrames(animationModel, v);
	}
}
