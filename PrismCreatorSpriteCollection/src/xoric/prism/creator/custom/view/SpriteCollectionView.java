package xoric.prism.creator.custom.view;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.util.List;

import xoric.prism.creator.common.view.PrismCreatorCommonView;
import xoric.prism.creator.custom.control.ISpriteCollectionControl;
import xoric.prism.creator.custom.model.SpriteCollectionModel;
import xoric.prism.creator.custom.model.SpriteModel;

public class SpriteCollectionView extends PrismCreatorCommonView implements ISpriteCollectionView
{
	private static final long serialVersionUID = 1L;

	private SpriteCollectionModel model;

	private final SpriteList list;

	public SpriteCollectionView()
	{
		super("SpriteCollection");
		super.setLayout(new GridBagLayout());

		list = new SpriteList();
		GridBagConstraints c = new GridBagConstraints(0, 0, 1, 1, 1.0, 1.0, GridBagConstraints.CENTER, GridBagConstraints.BOTH, new Insets(
				0, 0, 0, 0), 0, 0);
		add(list, c);
	}

	public void start()
	{
		setVisible(true);
	}

	@Override
	public void setControl(ISpriteCollectionControl control)
	{
		super.setMainMenuListener(control);
	}

	@Override
	public void setModel(SpriteCollectionModel model)
	{
		this.model = model;
	}

	@Override
	public void displaySprites()
	{
		List<SpriteModel> models = model.getModels();
		list.display(model.getPath(), models);
		revalidate();
		repaint();
	}
}
