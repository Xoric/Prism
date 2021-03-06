package xoric.prism.creator.models.image;

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

import xoric.prism.creator.common.spritelist.control.SpriteNameGenerator;
import xoric.prism.creator.common.spritelist.view.ISpriteList;
import xoric.prism.creator.common.spritelist.view.SpriteList;
import xoric.prism.creator.models.control.IMainControl;
import xoric.prism.creator.models.model.AnimationModel;
import xoric.prism.creator.models.model.VariationList;
import xoric.prism.creator.models.view.AnimationCell;
import xoric.prism.creator.models.view.IAnimationCell;
import xoric.prism.creator.models.view.IAnimationEditor;
import xoric.prism.creator.models.view.PreviewFrame;
import xoric.prism.data.types.IPoint_r;
import xoric.prism.swing.input.IIntInput;
import xoric.prism.swing.input.IValueInputListener;
import xoric.prism.swing.input.IntInputPanel;
import xoric.prism.swing.input.ValueInputPanel;
import xoric.prism.swing.tooltips.ToolTipFormatter;
import xoric.prism.world.animations.AnimationIndex;
import xoric.prism.world.entities.ViewAngle;

public class AnimationView extends JPanel implements ActionListener, IAnimationView, IAngleSelectorListener, IValueInputListener,
		IAnimationPanel
{
	private static final long serialVersionUID = 1L;

	private final JButton backButton;
	//	private final JLabel nameLabel;

	private final PreviewFrame previewFrame;

	private IMainControl control;

	private final IAnimationEditor mainView;

	private final IAnimationCell animationCell;
	private final IVariationSelector variationSelector;
	private final IIntInput durationInput;
	private IAngleSelector angleSelector;
	private ISpriteList spriteList;
	private JButton previewButton;

	private VariationList variationList;

	private IPoint_r spriteSize;

	public AnimationView(IAnimationEditor animationEditor)
	{
		super(new GridBagLayout());

		this.mainView = animationEditor;

		this.previewFrame = new PreviewFrame();

		VariationSelector vs = new VariationSelector();
		variationSelector = vs;

		AnimationCell animCell = new AnimationCell(AnimationIndex.IDLE);
		animCell.setEnabled(true);
		animationCell = animCell;

		IntInputPanel durInput = new IntInputPanel("Duration", this);
		durInput.setToolTipText(ToolTipFormatter.split("Duration of this animation in milliseconds"));
		durationInput = durInput;
		durationInput.setUnit("ms");
		durationInput.setPrompt("Enter duration in milliseconds.");

		AngleSelectorPanel angleSel = new AngleSelectorPanel(this);
		angleSelector = angleSel;

		SpriteList f = new SpriteList();
		spriteList = f;

		previewButton = createButton("Preview", null);

		//		backButton = createButton("Back", null);
		backButton = createButton("<", null);

		Insets insets = new Insets(15, 15, 15, 15);

		// add controls
		GridBagConstraints c = new GridBagConstraints(0, 0, 1, 1, 0.0, 0.0, GridBagConstraints.EAST, GridBagConstraints.VERTICAL, insets,
				0, 0);
		add(backButton, c);

		c = new GridBagConstraints(1, 0, 1, 1, 0.7, 0.0, GridBagConstraints.WEST, GridBagConstraints.BOTH, insets, 0, 0);
		add(animCell, c);

		c = new GridBagConstraints(2, 0, 1, 1, 0.3, 0.0, GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL, insets, 0, 0);
		add(vs, c);

		c = new GridBagConstraints(3, 0, 1, 1, 0.0, 0.0, GridBagConstraints.EAST, GridBagConstraints.VERTICAL, insets, 0, 0);
		add(durInput, c);

		c = new GridBagConstraints(0, 1, 4, 1, 1.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.BOTH, insets, 0, 0);
		add(new JSeparator(), c);

		c = new GridBagConstraints(0, 2, 1, 1, 0.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.NONE, insets, 0, 0);
		add(angleSel, c);

		c = new GridBagConstraints(1, 2, 3, 1, 1.0, 1.0, GridBagConstraints.WEST, GridBagConstraints.BOTH, insets, 0, 0);
		add(f, c);

		c = new GridBagConstraints(3, 3, 1, 1, 0.0, 0.0, GridBagConstraints.EAST, GridBagConstraints.NONE, insets, 0, 0);
		add(previewButton, c);
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
	public void setTileSize(IPoint_r spriteSize)
	{
		this.spriteSize = spriteSize;
		this.spriteList.setSpriteSize(spriteSize);
	}

	private void onPreviewButton()
	{
		int variation = variationSelector.getCurrentVariation();
		AnimationModel m = variationList.getVariation(variation);
		previewFrame.loadAndPlay(m, variation, angleSelector.getAngle(), spriteSize);
	}

	@Override
	public void actionPerformed(ActionEvent e)
	{
		Object o = e.getSource();

		if (o == backButton)
			mainView.requestCloseAnimationEditor();
		else if (o == previewButton)
			onPreviewButton();
	}

	@Override
	public void displayAnimation(VariationList list)
	{
		variationList = list;
		int variation = variationSelector.getCurrentVariation();
		AnimationModel m = list.getVariation(variation);
		animationCell.displayAnimationIndex(m.getAnimationIndex());
		durationInput.setValue(m.getDurationMs());

		SpriteNameGenerator spriteNameGenerator = new AnimationSpriteNameGenerator(list.getPath(), m.getAnimationIndex(), variationSelector
				.getCurrentVariation(), angleSelector.getAngle());
		spriteList.loadAndDisplaySprites(spriteNameGenerator);
	}

	@Override
	public void displayCurrentAnimationDuration()
	{
		int variation = variationSelector.getCurrentVariation();
		AnimationModel m = variationList.getVariation(variation);
		durationInput.setValue(m.getDurationMs());
	}

	@Override
	public void changedAngle(ViewAngle v)
	{
		SpriteNameGenerator spriteNameGenerator = new AnimationSpriteNameGenerator(variationList.getPath(), variationList
				.getAnimationIndex(), variationSelector.getCurrentVariation(), v);
		spriteList.loadAndDisplaySprites(spriteNameGenerator);
	}

	@Override
	public void setControl(IMainControl control)
	{
		this.control = control;
		//		this.spriteList.setControl(control);
	}

	@Override
	public void reloadCurrentAnimationFrames()
	{
		SpriteNameGenerator spriteNameGenerator = new AnimationSpriteNameGenerator(variationList.getPath(), variationList
				.getAnimationIndex(), variationSelector.getCurrentVariation(), angleSelector.getAngle());
		spriteList.loadAndDisplaySprites(spriteNameGenerator);
	}

	@Override
	public void notifyValueChanged(ValueInputPanel input)
	{
		if (input == durationInput)
		{
			int ms = durationInput.getValue();
			int variation = variationSelector.getCurrentVariation();
			control.requestSetAnimationDuration(variationList.getAnimationIndex(), variation, ms);
		}
	}
}
