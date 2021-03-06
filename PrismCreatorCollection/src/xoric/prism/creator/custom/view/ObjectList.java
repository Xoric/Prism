package xoric.prism.creator.custom.view;

import java.awt.BorderLayout;

import javax.swing.DefaultListModel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

import xoric.prism.creator.common.buttonpanel.ButtonPanel;
import xoric.prism.creator.common.buttonpanel.IButtonPanel;
import xoric.prism.creator.common.buttonpanel.IButtonPanelListener;
import xoric.prism.creator.custom.control.IObjectControl;
import xoric.prism.creator.custom.model.CollectionModel;
import xoric.prism.data.types.IText_r;
import xoric.prism.swing.PrismPanel;

public class ObjectList extends PrismPanel implements ListSelectionListener, IObjectList, IButtonPanelListener
{
	private static final long serialVersionUID = 1L;

	private IObjectControl control;
	private CollectionModel model;
	private final IObjectListListener listener;

	private final JList<IText_r> list;
	private final DefaultListModel<IText_r> listModel;
	private final IButtonPanel buttonPanel;

	public ObjectList(IObjectListListener listener)
	{
		super("Objects");

		this.listener = listener;

		list = new JList<IText_r>();
		listModel = new DefaultListModel<IText_r>();
		list.addListSelectionListener(this);
		list.setModel(listModel);
		JScrollPane scroll = new JScrollPane(list);

		ButtonPanel bp = new ButtonPanel(this, "object", true, false, true);
		bp.addUpDownButtons();
		buttonPanel = bp;

		JPanel p = new JPanel(new BorderLayout());
		p.add(BorderLayout.CENTER, scroll);
		p.add(BorderLayout.SOUTH, bp);

		this.setContent(p);

		enableButtons();
	}

	@Override
	public void setEnabled(boolean b)
	{
		super.setEnabled(b);
		enableButtons();
	}

	private void enableButtons()
	{
		int n = list.getSelectedIndices().length;
		buttonPanel.setEnabled(this.isEnabled(), n > 0);
	}

	@Override
	public void setControl(IObjectControl control)
	{
		this.control = control;
	}

	@Override
	public void setModel(CollectionModel model)
	{
		this.model = model;
	}

	@Override
	public void displayObjects()
	{
		if (model == null)
		{
			listModel.clear();
		}
		else
		{
			final int n = model.getCategoryCount();

			for (int i = 0; i < n; ++i)
			{
				IText_r t = model.getObjectModel(i).getName();

				if (i < listModel.size())
					listModel.set(i, t);
				else
					listModel.addElement(t);
			}

			for (int i = n; i < listModel.size(); ++i)
				listModel.remove(i);
		}
		enableButtons();
	}

	@Override
	public void valueChanged(ListSelectionEvent e)
	{
		enableButtons();
		listener.displayObject();
	}

	@Override
	public int getSelectedIndex()
	{
		return list.getSelectedIndex();
	}

	@Override
	public void onAddButton()
	{
		control.requestAddObject();
	}

	@Override
	public void onEditButton()
	{
	}

	@Override
	public void onDeleteButton()
	{
		control.requestDeleteObject(list.getSelectedIndex());
	}

	@Override
	public void onUpButton()
	{
		control.requestMoveObject(list.getSelectedIndex(), true);
	}

	@Override
	public void onDownButton()
	{
		control.requestMoveObject(list.getSelectedIndex(), false);
	}

	@Override
	public void selectObject(int index)
	{
		list.setSelectedIndex(index);
	}
}
