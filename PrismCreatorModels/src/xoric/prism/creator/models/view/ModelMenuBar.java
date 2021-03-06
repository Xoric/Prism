package xoric.prism.creator.models.view;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JMenuItem;

import xoric.prism.creator.common.view.INewDialogCreator;
import xoric.prism.creator.common.view.MainMenuBar;
import xoric.prism.creator.models.control.IMainControl;
import xoric.prism.creator.models.model.ModelModel;

public class ModelMenuBar extends MainMenuBar implements ActionListener
{
	private static final long serialVersionUID = 1L;
	private static final String modelExtension = ".md";

	private IMainControl control;

	private JMenuItem menuItemCreateAnimations;
	private JMenuItem menuItemCreateModel;

	public ModelMenuBar(INewDialogCreator d)
	{
		super("model", d, true);

		menuItemCreateAnimations = createMenuItem("Create animations (.png)");
		menuItemCreateModel = createMenuItem("Create model (" + modelExtension + ")");

		super.addCreationItem(menuItemCreateAnimations);
		super.addCreationItem(menuItemCreateModel);
	}

	public void setControl(IMainControl control)
	{
		super.setMainMenuListener(control);
		this.control = control;
	}

	private JMenuItem createMenuItem(String text)
	{
		JMenuItem m = new JMenuItem(text);
		m.addActionListener(this);
		return m;
	}

	@Override
	public void actionPerformed(ActionEvent e)
	{
		Object o = e.getSource();

		if (o == menuItemCreateAnimations)
			control.requestCreateAnimations();
		else if (o == menuItemCreateModel)
			control.requestCreateModel();
	}

	public void setModel(ModelModel model)
	{
		boolean isModelNull = model == null;
		super.setModelObjectIsNull(isModelNull);
	}
}
