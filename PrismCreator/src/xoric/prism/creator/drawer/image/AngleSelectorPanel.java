package xoric.prism.creator.drawer.image;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Image;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JCheckBox;
import javax.swing.JOptionPane;
import javax.swing.JPanel;

import xoric.prism.world.entities.ViewAngle;

public class AngleSelectorPanel extends JPanel implements ActionListener
{
	private static final long serialVersionUID = 1L;

	private static final float F = 1.0f / 3.0f;

	private final Object[] message;
	private final JCheckBox[] directions;
	private final boolean allowMultiselect;

	public AngleSelectorPanel(boolean allowMultiselect)
	{
		super(new GridBagLayout());
		this.allowMultiselect = allowMultiselect;

		Insets insets = new Insets(5, 5, 5, 5);
		directions = new JCheckBox[8];

		int y = 0;
		directions[y++] = createBox(ViewAngle.TOP_LEFT);
		directions[y++] = createBox(ViewAngle.TOP);
		directions[y++] = createBox(ViewAngle.TOP_RIGHT);
		directions[y++] = createBox(ViewAngle.LEFT);
		directions[y++] = createBox(ViewAngle.RIGHT);
		directions[y++] = createBox(ViewAngle.BOT_LEFT);
		directions[y++] = createBox(ViewAngle.BOT);
		directions[y++] = createBox(ViewAngle.BOT_RIGHT);

		int i = 0;

		for (y = 0; y < 3; ++y)
		{
			for (int x = 0; x < 3; ++x)
			{
				if (y != 1 || x != 1)
				{
					GridBagConstraints c = new GridBagConstraints(x, y, 1, 1, F, F, GridBagConstraints.CENTER, GridBagConstraints.NONE,
							insets, 0, 0);
					add(directions[i], c);
					++i;
				}
			}
		}
		message = new Object[] { null, null, this };
	}

	private JCheckBox createBox(ViewAngle v)
	{
		JCheckBox b = new JCheckBox();
		String s = v.toString().toLowerCase();
		b.setToolTipText(s);
		updateIcon(b);
		b.addActionListener(this);
		return b;
	}

	private static void updateIcon(JCheckBox c)
	{
		String s = c.getToolTipText();

		try
		{
			String n = c.isSelected() ? "1" : "0";
			Image img = ImageIO.read(ClassLoader.getSystemResource("icons/" + s + n + ".png"));
			ImageIcon icn = new ImageIcon(img);
			c.setIcon(icn);
		}
		catch (Exception e)
		{
			c.setText(s);
		}
	}

	public boolean showDialog()
	{
		int n = JOptionPane.showConfirmDialog(null, message, "New Model", JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);
		return n == JOptionPane.OK_OPTION;
	}

	@Override
	public void actionPerformed(ActionEvent e)
	{
		Object o = e.getSource();

		if (o instanceof JCheckBox)
		{
			JCheckBox c = (JCheckBox) o;
			updateIcon(c);

			if (!allowMultiselect)
			{
				for (JCheckBox b : directions)
				{
					if (b != c)
					{
						b.setSelected(false);
						updateIcon(b);
					}
				}
			}
		}
	}
}
