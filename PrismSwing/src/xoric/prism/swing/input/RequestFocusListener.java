package xoric.prism.swing.input;

import javax.swing.JComponent;
import javax.swing.JTextField;
import javax.swing.event.AncestorEvent;
import javax.swing.event.AncestorListener;

public class RequestFocusListener implements AncestorListener
{
	@Override
	public void ancestorAdded(AncestorEvent e)
	{
		JComponent c = e.getComponent();
		c.requestFocusInWindow();

		if (c instanceof JTextField)
		{
			JTextField t = (JTextField) c;
			t.selectAll();
		}
		//		c.removeAncestorListener(this);
	}

	@Override
	public void ancestorMoved(AncestorEvent e)
	{
	}

	@Override
	public void ancestorRemoved(AncestorEvent e)
	{
	}
}