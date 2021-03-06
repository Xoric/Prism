package xoric.prism.creator.models.image;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Image;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JCheckBox;
import javax.swing.JLabel;
import javax.swing.JPanel;

import xoric.prism.world.entities.ViewAngle;

public class AngleSelectorPanel extends JPanel implements ActionListener, IAngleSelector
{
	private static final long serialVersionUID = 1L;

	private final JLabel titleLabel;
	private final JCheckBox[] directions;
	private final ImageIcon[][] icons;
	private ViewAngle selectedAngle;

	private IAngleSelectorListener listener;

	public AngleSelectorPanel(IAngleSelectorListener listener)
	{
		super(new GridBagLayout());

		this.listener = listener;

		Insets insets = new Insets(0, 0, 0, 0);

		titleLabel = new JLabel();
		GridBagConstraints c = new GridBagConstraints(0, 0, 3, 1, 0.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.NONE, insets, 0,
				0);
		add(titleLabel, c);

		int m = ViewAngle.values().length;
		icons = new ImageIcon[m][2];

		int y = 0;
		directions = new JCheckBox[8];
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
					c = new GridBagConstraints(x, y + 1, 1, 1, 0.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.NONE, insets, 0, 0);
					add(directions[i], c);
					++i;
				}
			}
		}

		// set default direction to RIGHT
		selectedAngle = ViewAngle.RIGHT;
		directions[4].setSelected(true);
		updateIcon(directions[4]);
		updateTitle();
	}

	private ImageIcon getIcon(String s, int n)
	{
		ViewAngle v = ViewAngle.valueOf(s.toUpperCase());
		int i = v.ordinal();

		if (icons[i][n] == null)
		{
			try
			{
				Image img = ImageIO.read(ClassLoader.getSystemResource("icons/" + s.toLowerCase() + n + ".png"));
				icons[i][n] = new ImageIcon(img);
			}
			catch (Exception e)
			{
			}
		}
		return icons[i][n];
	}

	private JCheckBox createBox(ViewAngle v)
	{
		JCheckBox b = new JCheckBox();
		String s = v.toString().toLowerCase();
		b.setToolTipText(s);
		b.addActionListener(this);
		updateIcon(b);
		return b;
	}

	private void updateIcon(JCheckBox c)
	{
		String s = c.getToolTipText();
		int n = c.isSelected() ? 1 : 0;
		ImageIcon icn = getIcon(s.toUpperCase(), n);

		if (icn != null)
			c.setIcon(icn);
		else
			c.setText(s);
	}

	@Override
	public void actionPerformed(ActionEvent e)
	{
		Object o = e.getSource();

		if (o instanceof JCheckBox)
		{
			JCheckBox c = (JCheckBox) o;
			String s = c.getToolTipText().toUpperCase();
			selectedAngle = ViewAngle.valueOf(s);
			updateTitle();
			listener.changedAngle(selectedAngle);

			for (JCheckBox b : directions)
			{
				b.setSelected(b == c);
				updateIcon(b);
			}
		}
	}

	private void updateTitle()
	{
		titleLabel.setText("<html><b>" + selectedAngle.toString() + "</b></html>");
	}

	@Override
	public ViewAngle getAngle()
	{
		return selectedAngle;
	}
}
