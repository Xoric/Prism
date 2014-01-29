package xoric.prism.swing.input;

import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Image;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextPane;
import javax.swing.SwingConstants;
import javax.swing.text.SimpleAttributeSet;
import javax.swing.text.StyleConstants;
import javax.swing.text.StyledDocument;

public abstract class ValueInput extends JPanel implements ActionListener, IValueInput
{
	private static final long serialVersionUID = 1L;

	private JLabel nameLabel;
	private JTextPane valuePane;
	private JButton editButton;

	protected final String name;
	protected String prompt;
	protected String unitSuffix;
	private final IValueInputListener listener;

	public ValueInput(String name, IValueInputListener listener)
	{
		this.setLayout(new GridBagLayout());

		this.listener = listener;

		this.name = name;
		this.prompt = "Enter " + name.toLowerCase();
		this.unitSuffix = "";

		nameLabel = new JLabel("<html><b>" + name + "</b></html>");
		nameLabel.setHorizontalAlignment(SwingConstants.CENTER);

		valuePane = new JTextPane();
		valuePane.setEditable(false);
		valuePane.setBorder(BorderFactory.createEtchedBorder());

		StyledDocument doc = valuePane.getStyledDocument();
		SimpleAttributeSet center = new SimpleAttributeSet();
		StyleConstants.setAlignment(center, StyleConstants.ALIGN_CENTER);
		doc.setParagraphAttributes(0, doc.getLength(), center, false);

		editButton = new JButton();
		editButton.addActionListener(this);

		try
		{
			Image img = ImageIO.read(ClassLoader.getSystemResource("icons/edit.png"));
			ImageIcon icn = new ImageIcon(img);
			editButton.setIcon(icn);
			editButton.setText("");
			editButton.setPreferredSize(new Dimension(icn.getIconWidth() + 8, icn.getIconHeight() + 4));
			editButton.setToolTipText("Click to edit");
		}
		catch (IOException e)
		{
			editButton.setText("Edit");
		}

		GridBagConstraints c = new GridBagConstraints(0, 0, 1, 1, 1.0, 0.5, GridBagConstraints.CENTER, GridBagConstraints.BOTH, new Insets(
				3, 5, 2, 5), 0, 0);
		add(nameLabel, c);

		c = new GridBagConstraints(0, 1, 1, 1, 0.5, 0.5, GridBagConstraints.CENTER, GridBagConstraints.BOTH, new Insets(0, 5, 3, 0), 0, 0);
		add(valuePane, c);

		c = new GridBagConstraints(1, 1, 1, 1, 0.0, 1.0, GridBagConstraints.CENTER, GridBagConstraints.NONE, new Insets(0, 3, 3, 5), 0, 0);
		add(editButton, c);
	}

	@Override
	public void setEnabled(boolean enabled)
	{
		super.setEnabled(enabled);

		nameLabel.setEnabled(enabled);
		valuePane.setEnabled(enabled);
		editButton.setEnabled(enabled);
	}

	@Override
	public void setPrompt(String prompt)
	{
		this.prompt = prompt;
	}

	@Override
	public void setUnit(String unit)
	{
		unitSuffix = unit.length() == 0 ? "" : " " + unit;
	}

	@Override
	public void setToolTipText(String tooltip)
	{
		super.setToolTipText(tooltip);
		valuePane.setToolTipText(tooltip);
	}

	protected abstract void requestEdit();

	protected abstract String getValueText();

	protected void valueChanged(boolean notifyListener)
	{
		valuePane.setText(getValueText() + unitSuffix);

		if (notifyListener && listener != null)
			listener.notifyValueChanged(this);
	}

	@Override
	public void actionPerformed(ActionEvent e)
	{
		Object o = e.getSource();

		if (o == editButton)
		{
			requestEdit();
		}
	}
}
