package xoric.prism.creator.grid.view;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JMenuItem;

import xoric.prism.creator.common.spritelist.view.ISpriteList;
import xoric.prism.creator.common.spritelist.view.SpriteList;
import xoric.prism.creator.common.view.IMainMenuBar;
import xoric.prism.creator.common.view.INewDialog;
import xoric.prism.creator.common.view.PrismCreatorCommonView;
import xoric.prism.creator.grid.control.IGridControl;
import xoric.prism.creator.grid.model.GridModel;
import xoric.prism.data.types.Point;
import xoric.prism.data.types.Text;
import xoric.prism.swing.input.BooleanInputPanel;
import xoric.prism.swing.input.IValueInputListener;
import xoric.prism.swing.input.PointInputPanel;
import xoric.prism.swing.input.TextInputPanel;
import xoric.prism.swing.input.ValueInputPanel;
import xoric.prism.swing.tooltips.ToolTipFormatter;

public class GridView extends PrismCreatorCommonView implements ActionListener, IValueInputListener, IGridView
{
	private static final long serialVersionUID = 1L;

	private IGridControl control;

	private GridModel model;

	private final TextInputPanel nameInput;
	private final PointInputPanel sizeInput;
	private final BooleanInputPanel hotSpotInput;

	private final ISpriteList spriteList;

	private final JMenuItem mnuItemCreateTexture;
	private final JMenuItem mnuItemCreateCollection;

	public GridView()
	{
		super("SpriteGrid", true);
		super.setLayout(new GridBagLayout());

		Insets insets = new Insets(30, 30, 30, 30);

		nameInput = new TextInputPanel("Name", this);
		nameInput.setValue(new Text("NONE"));
		nameInput.setPrompt("Enter a new name for this grid.");
		nameInput.setToolTipText(ToolTipFormatter.split("Provide a name for this sprite grid."));
		GridBagConstraints c = new GridBagConstraints(0, 0, 1, 1, 0.15, 0.5, GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL,
				insets, 0, 0);
		add(nameInput, c);

		sizeInput = new PointInputPanel("Sprite size", this);
		sizeInput.setValue(new Point(24, 24));
		sizeInput.setPrompt("Enter a new sprite size.");
		sizeInput.setToolTipText(ToolTipFormatter.split("Width and height of one cell in the sprite grid."));
		c.gridy++;
		add(sizeInput, c);

		hotSpotInput = new BooleanInputPanel("Use hotspots and action points", this);
		hotSpotInput.setPrompt("Choose whether or not to use hotspots and action points.");
		hotSpotInput
				.setToolTipText(ToolTipFormatter
						.split("Hotspots are used to manipulate the individual placement of sprites when drawing. Action points are used to mark limbs etc."));
		c.gridy++;
		add(hotSpotInput, c);

		SpriteList s = new SpriteList();
		spriteList = s;
		c = new GridBagConstraints(1, 0, 1, 3, 0.85, 1.0, GridBagConstraints.CENTER, GridBagConstraints.BOTH, insets, 0, 0);
		add(s, c);

		mnuItemCreateTexture = new JMenuItem("Combined texture (.png)");
		mnuItemCreateTexture.addActionListener(this);

		mnuItemCreateCollection = new JMenuItem("SpriteGrid (.gd)");
		mnuItemCreateCollection.addActionListener(this);

		IMainMenuBar m = super.getMainMenuBar();
		m.addCreationItem(mnuItemCreateTexture);
		m.addCreationItem(mnuItemCreateCollection);

		updateHotSpotsEnabled();
		setModel(null);
	}

	@Override
	public INewDialog createDialog()
	{
		return new NewGridDialog();
	}

	public void start()
	{
		setVisible(true);
	}

	@Override
	public void setControl(IGridControl control)
	{
		super.getMainMenuBar().setMainMenuListener(control);
		this.control = control;
		updateHotSpotsEnabled();
	}

	@Override
	public void updateHotSpotsEnabled()
	{
		if (model == null || !model.isHotSpotListEnabled())
			spriteList.registerHotSpotListener(null);
		else
			spriteList.registerHotSpotListener(control);
	}

	@Override
	public void setModel(GridModel model)
	{
		this.model = model;
		super.getMainMenuBar().setModelObjectIsNull(model == null);

		boolean b = model != null;
		spriteList.setEnabled(b);
		nameInput.setEnabled(b);
		sizeInput.setEnabled(b);

		updateHotSpotsEnabled();
	}

	@Override
	public void displayAll()
	{
		displayName();
		displaySpriteSize();
		displaySprites();
	}

	@Override
	public void displayName()
	{
		nameInput.setValue(model == null ? new Text("") : model.getName());
	}

	@Override
	public void displaySpriteSize()
	{
		sizeInput.setValue(model == null ? new Point() : model.getSpriteSize());
	}

	@Override
	public void displaySprites()
	{
		spriteList.loadAndDisplaySprites(model == null ? null : model.getSpriteNameGenerator());
		revalidate();
		repaint();
	}

	@Override
	public void actionPerformed(ActionEvent e)
	{
		Object o = e.getSource();

		if (o == mnuItemCreateTexture)
			control.requestCreateTexture();
		else if (o == mnuItemCreateCollection)
			control.requestCreateGrid();
	}

	@Override
	public void notifyValueChanged(ValueInputPanel input)
	{
		if (input == nameInput)
			control.requestRenameGrid(nameInput.getValue());
		else if (input == sizeInput)
			control.requestSetSpriteSize(sizeInput.getValue());
		else if (input == hotSpotInput)
			control.requestEnableHotSpots(hotSpotInput.getValue());
	}

	@Override
	public void updatedHotSpots()
	{
		spriteList.requestReloadSprites();
	}
}
