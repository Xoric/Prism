package xoric.prism.creator.models.generators;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import xoric.prism.creator.models.image.AnimationSpriteNameGenerator;
import xoric.prism.data.types.IPath_r;
import xoric.prism.world.animations.AnimationIndex;
import xoric.prism.world.entities.ViewAngle;

class AngleSummary
{
	private final AnimationIndex animationIndex;
	private final int variation;
	private final ViewAngle viewAngle;
	private final List<File> files;

	public AngleSummary(AnimationIndex a, int variation, ViewAngle v)
	{
		this.animationIndex = a;
		this.variation = variation;
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
			String filename = AnimationSpriteNameGenerator.getFilename(animationIndex, variation, viewAngle, n);
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
