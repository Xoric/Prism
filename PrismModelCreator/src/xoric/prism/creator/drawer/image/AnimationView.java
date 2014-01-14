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
import javax.swing.JPanel;
import javax.swing.JSeparator;

import xoric.prism.creator.drawer.control.IDrawerControl;
import xoric.prism.creator.drawer.model.AnimationModel;
import xoric.prism.creator.drawer.view.AnimationCell;
import xoric.prism.creator.drawer.view.IAnimationCell;
import xoric.prism.creator.drawer.view.IAnimationEditor;
import xoric.prism.data.types.IPoint_r;
import xoric.prism.swing.input.IIntInput;
import xoric.prism.swing.input.IValueInputListener;
import xoric.prism.swing.input.IntInput;
import xoric.prism.swing.input.ValueInput;
import xoric.prism.swing.tooltips.ToolTipFormatter;
import xoric.prism.world.animations.AnimationIndex;
import xoric.prism.world.entities.ViewAngle;

public class AnimationView extends JPanel implements ActionListener, IAnimationView, IAngleSelectorListener, IValueInputListener,
		IAnimationPanel
{
	private static final long serialVersionUID = 1L;

	private final JButton backButton;
	//	private final JLabel nameLabel;

	private IDrawerControl control;

	private final IAnimationEditor mainView;

	private final IAnimationCell animationCell;
	private final IIntInput durationInput;
	private IAngleSelector angleSelector;
	private ISpriteList spriteList;

	private AnimationModel animationModel;

	public AnimationView(IAnimationEditor animationEditor)
	{
		super(new GridBagLayout());

		this.mainView = animationEditor;

		AnimationCell animCell = new AnimationCell(AnimationIndex.IDLE);
		animCell.setEnabled(true);
		animationCell = animCell;

		IntInput durInput = new IntInput("Duration", this);
		durInput.setToolTipText(ToolTipFormatter.split("Duration of this animation in milliseconds"));
		durationInput = durInput;
		durationInput.setUnit("ms");
		durationInput.setPrompt("Enter duration in milliseconds.");

		AngleSelectorPanel angleSel = new AngleSelectorPanel(this);
		angleSelector = angleSel;

		SpriteList f = new SpriteList();
		spriteList = f;

		//		backButton = createButton("Back", null);
		backButton = createButton("<", null);

		Insets insets = new Insets(15, 15, 15, 15);

		// add controls
		//		GridBagConstraints c = new GridBagConstraints(0, 0, 1, 1, 0.0, 0.0, GridBagConstraints.NORTHWEST, GridBagConstraints.NONE, insets,
		//				0, 0);
		GridBagConstraints c = new GridBagConstraints(0, 0, 1, 1, 0.0, 0.0, GridBagConstraints.EAST, GridBagConstraints.VERTICAL, insets,
				0, 0);
		add(backButton, c);

		c = new GridBagConstraints(1, 0, 1, 1, 1.0, 0.0, GridBagConstraints.WEST, GridBagConstraints.BOTH, insets, 0, 0);
		add(animCell, c);

		c = new GridBagConstraints(2, 0, 1, 1, 0.0, 0.0, GridBagConstraints.EAST, GridBagConstraints.VERTICAL, insets, 0, 0);
		add(durInput, c);

		c = new GridBagConstraints(0, 1, 3, 1, 1.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.BOTH, insets, 0, 0);
		add(new JSeparator(), c);

		c = new GridBagConstraints(0, 2, 1, 1, 0.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.NONE, insets, 0, 0);
		add(angleSel, c);

		c = new GridBagConstraints(1, 2, 2, 1, 1.0, 1.0, GridBagConstraints.WEST, GridBagConstraints.BOTH, insets, 0, 0);
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

	@Override
	public void setTileSize(IPoint_r tileSize)
	{
		spriteList.setSpriteSize(tileSize);
	}

	@Override
	public void actionPerformed(ActionEvent e)
	{
		Object o = e.getSource();

		if (o == backButton)
			mainView.requestCloseAnimationEditor();
	}

	@Override
	public void displayAnimation(AnimationModel m)
	{
		animationModel = m;
		animationCell.displayAnimation(m.getAnimationIndex());
		durationInput.setValue(animationModel.getDurationMs());
		spriteList.loadFrames(animationModel, angleSelector.getAngle());
	}

	@Override
	public void displayCurrentAnimationDuration()
	{
		durationInput.setValue(animationModel.getDurationMs());
	}

	@Override
	public void changedAngle(ViewAngle v)
	{
		spriteList.loadFrames(animationModel, v);
	}

	@Override
	public void setControl(IDrawerControl control)
	{
		this.control = control;
		spriteList.setControl(control);
	}

	@Override
	public void reloadCurrentAnimationFrames()
	{
		spriteList.loadFrames(animationModel, angleSelector.getAngle());
	}

	@Override
	public void notifyValueChanged(ValueInput input)
	{
		if (input == durationInput)
		{
			int ms = durationInput.getValue();
			control.requestSetAnimationDuration(animationModel.getAnimationIndex(), ms);
		}
	}
}
