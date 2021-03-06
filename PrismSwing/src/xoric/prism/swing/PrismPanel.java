package xoric.prism.swing;

import java.awt.BorderLayout;
import java.awt.Font;

import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JPanel;

public class PrismPanel extends JPanel
{
	private static final long serialVersionUID = 1L;

	private final JLabel titleLabel;

	public PrismPanel(String title)
	{
		super(new BorderLayout());

		titleLabel = new JLabel(title);
		titleLabel.setFont(titleLabel.getFont().deriveFont(Font.BOLD));

		add(BorderLayout.NORTH, titleLabel);
	}

	protected void setTitle(String title)
	{
		titleLabel.setText(title);
	}

	@Override
	public void setEnabled(boolean enabled)
	{
		super.setEnabled(enabled);

		//		titleLabel.setEnabled(enabled);
	}

	public void setContent(JComponent comp)
	{
		add(BorderLayout.CENTER, comp);
	}
}
