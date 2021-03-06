package xoric.prism.creator.models.model;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import xoric.prism.creator.models.image.AnimationSpriteNameGenerator;
import xoric.prism.data.packable.IPackable;
import xoric.prism.data.packable.IntPacker;
import xoric.prism.data.types.IPath_r;
import xoric.prism.world.animations.AnimationIndex;
import xoric.prism.world.entities.ViewAngle;

public class AnimationModel implements IPackable
{
	private IPath_r path;
	private AnimationIndex animationIndex;
	private int variation;
	private AngleModel[] angles;
	private boolean isUnlocked;

	// saved and loaded:
	private int durationMs;

	public AnimationModel(IPath_r path, AnimationIndex a, int variation)
	{
		this.path = path;
		this.animationIndex = a;
		this.variation = variation;
		this.durationMs = 1000;
		this.isUnlocked = false;

		int n = ViewAngle.values().length;
		this.angles = new AngleModel[n];

		for (ViewAngle v : ViewAngle.values())
			angles[v.ordinal()] = new AngleModel(path, a, variation, v);
	}

	public IPath_r getPath()
	{
		return path;
	}

	public AnimationIndex getAnimationIndex()
	{
		return animationIndex;
	}

	public int getVariation()
	{
		return variation;
	}

	public void unlock()
	{
		this.isUnlocked = true;
	}

	public boolean isUsed()
	{
		boolean b = isUnlocked;

		if (!b)
			for (AngleModel m : angles)
				b |= m.isUsed();

		return b;
	}

	public void loadSpriteCount()
	{
		for (AngleModel a : angles)
			a.load();
	}

	public File getFile(ViewAngle v, int index)
	{
		return path.getFile(getFilename(v, index));
	}

	public String getFilename(ViewAngle v, int index)
	{
		return AnimationSpriteNameGenerator.getFilename(animationIndex, variation, v, index);
	}

	public void setDurationMs(int ms)
	{
		durationMs = ms;
	}

	public int getDurationMs()
	{
		return durationMs;
	}

	@Override
	public void pack(OutputStream stream) throws IOException
	{
		IntPacker.pack_s(stream, durationMs);
	}

	@Override
	public void unpack(InputStream stream) throws IOException
	{
		durationMs = IntPacker.unpack_s(stream);
	}
}
