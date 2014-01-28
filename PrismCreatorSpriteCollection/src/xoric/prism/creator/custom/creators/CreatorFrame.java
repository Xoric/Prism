package xoric.prism.creator.custom.creators;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;

import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JProgressBar;

public class CreatorFrame extends JDialog implements ICreatorFrame
{
	private static final long serialVersionUID = 1L;

	private final JLabel actionLabel;
	private final JProgressBar progressBar;
	private volatile boolean isClosed;

	public CreatorFrame()
	{
		actionLabel = new JLabel();
		progressBar = new JProgressBar();

		JPanel p = new JPanel(new GridBagLayout());

		Insets insets = new Insets(30, 30, 0, 30);
		GridBagConstraints c = new GridBagConstraints(0, 0, 1, 1, 1.0, 0.5, GridBagConstraints.CENTER, GridBagConstraints.BOTH, insets, 0,
				0);
		p.add(actionLabel, c);

		c.gridy++;
		c.insets = new Insets(15, 30, 30, 30);
		p.add(progressBar, c);

		this.setContentPane(p);
	}

	@Override
	public void showFrame()
	{
		if (!isClosed)
		{
			Object[] message = { actionLabel, progressBar };
			String[] options = { "Abort" };
			JOptionPane.showOptionDialog(null, message, "Create texture", JOptionPane.OK_OPTION, JOptionPane.PLAIN_MESSAGE, null, options,
					options[0]);
		}
	}

	@Override
	public void closeFrame()
	{
		isClosed = true;
		JOptionPane.getRootFrame().dispose();
	}

	@Override
	public void setAction(final String action)
	{
		actionLabel.setText(action);
		setProgress(0);
	}

	@Override
	public void setProgressMax(int n)
	{
		progressBar.setMaximum(n);
	}

	@Override
	public void setProgress(int p)
	{
		progressBar.setValue(p);
	}
}