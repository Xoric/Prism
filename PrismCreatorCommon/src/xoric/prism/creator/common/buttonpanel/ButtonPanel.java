package xoric.prism.creator.common.buttonpanel;

import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JPanel;

import xoric.prism.creator.common.factory.Factory;

public class ButtonPanel extends JPanel implements ActionListener, IButtonPanel
{
	private static final long serialVersionUID = 1L;

	private final IButtonPanelListener listener;

	private final JButton addButton;
	private final JButton editButton;
	private final JButton deleteButton;

	private JButton upButton;
	private JButton downButton;

	public ButtonPanel(IButtonPanelListener listener, String dataName, boolean add, boolean edit, boolean delete)
	{
		super(new FlowLayout());

		this.listener = listener;

		addButton = add ? Factory.createAddButton(this, "Create a new " + dataName + ".") : null;
		editButton = edit ? Factory.createEditButton(this, "Edit the selected " + dataName + ".") : null;
		deleteButton = delete ? Factory.createDeleteButton(this, "Delete the selected " + dataName + "(s).") : null;

		if (addButton != null)
			add(addButton);
		if (editButton != null)
			add(editButton);
		if (deleteButton != null)
			add(deleteButton);
	}

	public void addUpDownButtons()
	{
		upButton = Factory.createUpButton(this, "Move up.");
		add(upButton);
		downButton = Factory.createDownButton(this, "Move down.");
		add(downButton);
	}

	public JButton getAddButton()
	{
		return addButton;
	}

	@Override
	public void actionPerformed(ActionEvent e)
	{
		Object o = e.getSource();

		if (o == addButton)
			listener.onAddButton();
		else if (o == editButton)
			listener.onEditButton();
		else if (o == deleteButton)
			listener.onDeleteButton();
		//
		else if (o == upButton)
			listener.onUpButton();
		else if (o == downButton)
			listener.onDownButton();
	}

	@Override
	public void setEnabled(boolean b, boolean addAndEditCondition)
	{
		if (addButton != null)
			addButton.setEnabled(b);

		b &= addAndEditCondition;

		if (editButton != null)
			editButton.setEnabled(b);
		if (deleteButton != null)
			deleteButton.setEnabled(b);

		if (upButton != null)
			upButton.setEnabled(b);
		if (downButton != null)
			downButton.setEnabled(b);
	}
}
