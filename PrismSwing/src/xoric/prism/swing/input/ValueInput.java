package xoric.prism.swing.input;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Image;
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

public abstract class ValueInput extends JPanel implements ActionListener
{
	private static final long serialVersionUID = 1L;

	private JLabel nameLabel;
	private JTextPane valuePane;
	private JButton editButton;

	protected final String name;
	protected String prompt;
	protected String unitSuffix;
	private IValueInputListener listener;

	public ValueInput(String name, int width, IValueInputListener listener)
	{
		this.name = name;
		this.prompt = "Enter " + name.toLowerCase();
		this.unitSuffix = "";
		this.listener = listener;

		Dimension nameSize = new Dimension(60, 24);
		Dimension valueSize = new Dimension(140, 24);

		nameLabel = new JLabel("<html><b>" + name + "</b></html>");
		nameLabel.setHorizontalAlignment(SwingConstants.CENTER);
		nameLabel.setPreferredSize(nameSize);

		valuePane = new JTextPane();
		valuePane.setPreferredSize(valueSize);
		valuePane.setEditable(false);

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
			editButton.setPreferredSize(new Dimension(icn.getIconWidth() + 4, icn.getIconHeight() + 4));
			editButton.setToolTipText("Click to edit");
		}
		catch (IOException e)
		{
			editButton.setText("Edit");
		}

		JPanel p = new JPanel(new FlowLayout(FlowLayout.TRAILING, 2, 2));
		p.add(nameLabel);
		p.add(valuePane);
		p.add(editButton);
		p.setBorder(BorderFactory.createEtchedBorder());

		//		this.setLayout(new FlowLayout(FlowLayout.TRAILING, 2, 2));
		//		add(nameLabel);
		//		add(valuePane);
		//		add(editButton);
		//		setBorder(BorderFactory.createEtchedBorder());

		add(BorderLayout.CENTER, p);
		//		add(BorderLayout.EAST, editButton);

		Dimension totalSize = new Dimension(width, nameSize.height + 13);
		setPreferredSize(totalSize);
		setMaximumSize(totalSize);
	}

	@Override
	public void setEnabled(boolean enabled)
	{
		super.setEnabled(enabled);

		nameLabel.setEnabled(enabled);
		valuePane.setEnabled(enabled);
		editButton.setEnabled(enabled);
	}

	public void setPrompt(String prompt)
	{
		this.prompt = prompt;
	}

	public void setUnit(String unit)
	{
		unitSuffix = unit.length() == 0 ? "" : " " + unit;
	}

	private static String splitTooltip(String text, int maxCharsPerLine)
	{
		StringBuffer sb = new StringBuffer("<html>");
		String[] words = text.split(" ");
		boolean isFirstInLine = true;
		int sum = 0;

		for (String s : words)
		{
			if (!isFirstInLine)
				sb.append(" ");
			sb.append(s);

			isFirstInLine = (sb.length() - sum) >= maxCharsPerLine;
			if (isFirstInLine)
			{
				sum = sb.length();
				sb.append("<br>");
			}
		}
		sb.append("</html>");

		return sb.toString();
	}

	@Override
	public void setToolTipText(String text)
	{
		String s = splitTooltip(text, 30);
		super.setToolTipText(s);
		valuePane.setToolTipText(s);
	}

	protected abstract void requestEdit();

	protected abstract String getValueText();

	protected void valueChanged(boolean notifyListener)
	{
		valuePane.setText(getValueText() + unitSuffix);

		if (listener != null)
			listener.notifyValueChanged();
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
