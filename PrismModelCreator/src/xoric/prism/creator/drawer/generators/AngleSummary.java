package xoric.prism.creator.drawer.generators;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import xoric.prism.creator.drawer.model.SpriteNames;
import xoric.prism.data.types.IPath_r;
import xoric.prism.world.animations.AnimationIndex;
import xoric.prism.world.entities.ViewAngle;

class AngleSummary
{
	private final AnimationIndex animationIndex;
	private final ViewAngle viewAngle;
	private final List<File> files;

	public AngleSummary(AnimationIndex a, ViewAngle v)
	{
		this.animationIndex = a;
		this.viewAngle = v;
		this.files = new ArrayList<File>();
	}

	public void load(IPath_r path)
	{
		files.clear();

		int n = 0;
		boolean b;

		do
		{
			String filename = SpriteNames.getFilename(animationIndex, viewAngle, n);
			File file = path.getFile(filename);
			b = file.exists();

			if (b)
			{
				files.add(file);
				++n;
			}
		}
		while (b);
	}

	public boolean hasSprites()
	{
		return files.size() > 0;
	}

	public int getSpriteCount()
	{
		return files.size();
	}

	public File getSpriteFile(int index)
	{
		return files.get(index);
	}

	public ViewAngle getAngle()
	{
		return viewAngle;
	}
}
