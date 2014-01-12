package xoric.prism.swing.multithread;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JProgressBar;
import javax.swing.Timer;

public class PrismWatcher extends JFrame implements ActionListener
{
	private static final long serialVersionUID = 1L;

	private final PrismWorker worker;
	private final Timer timer;

	private final JLabel textLabel;
	private final JProgressBar progressBar;

	public PrismWatcher(PrismWorker worker)
	{
		this.setTitle(worker.getTitle());
		this.setSize(320, 240);
		this.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);

		this.worker = worker;
		this.timer = new Timer(500, this);

		this.textLabel = new JLabel(worker.getText());
		this.progressBar = new JProgressBar();

		progressBar.setMaximum(1000);
		progressBar.setValue((int) (1000 * worker.getProgress()));
	}

	public void start()
	{
		Thread t = new Thread(worker);
		t.start();
		timer.start();
	}

	@Override
	public void actionPerformed(ActionEvent e)
	{
		if (worker.isDone())
		{
			this.dispose();
		}
		else
		{
			textLabel.setText(worker.getText());
			progressBar.setValue((int) (1000 * worker.getProgress()));
		}
	}
}
