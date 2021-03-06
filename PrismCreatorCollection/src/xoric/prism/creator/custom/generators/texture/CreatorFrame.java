package xoric.prism.creator.custom.generators.texture;

import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JProgressBar;

public class CreatorFrame implements ICreatorFrame
{
	private final JLabel actionLabel;
	private final JProgressBar chapterBar;
	private final JProgressBar progressBar;
	private volatile boolean isClosed;

	public CreatorFrame()
	{
		actionLabel = new JLabel();
		chapterBar = new JProgressBar();
		progressBar = new JProgressBar();
	}

	@Override
	public void showFrame()
	{
		if (!isClosed)
		{
			Object[] message = { actionLabel, progressBar, chapterBar };
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

	@Override
	public void setChapterMax(int n)
	{
		chapterBar.setMaximum(n);
	}

	//	@Override
	//	public void setChapter(int p)
	//	{
	//		chapterBar.setValue(p);
	//	}

	@Override
	public void increaseChapter()
	{
		chapterBar.setValue(chapterBar.getValue() + 1);
	}
}
