package xoric.prism.creator.drawer.image;

import java.awt.FlowLayout;

import javax.swing.JPanel;

import xoric.prism.data.types.IPath_r;

public class FramesView extends JPanel
{
	private static final long serialVersionUID = 1L;

	public FramesView()
	{
		super(new FlowLayout());
	}

	public void loadFrames(IPath_r path)
	{
		for (int i = 0; i < 4; ++i)
		{
			FrameView v = new FrameView(i);
			v.loadImage(path, "idle.right." + i + ".png");
			add(v);
		}
	}
}
