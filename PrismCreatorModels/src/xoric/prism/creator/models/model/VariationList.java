package xoric.prism.creator.models.model;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;

import xoric.prism.data.exceptions.PrismException;
import xoric.prism.data.packable.IPackable;
import xoric.prism.data.packable.IntPacker;
import xoric.prism.data.types.IPath_r;
import xoric.prism.world.animations.AnimationIndex;

public class VariationList implements IPackable
{
	private final IPath_r path;
	private final AnimationIndex animationIndex;
	private List<AnimationModel> variations;

	private boolean isUnlocked;

	public VariationList(IPath_r path, AnimationIndex a)
	{
		variations = new ArrayList<AnimationModel>();

		this.path = path;
		this.animationIndex = a;

		this.isUnlocked = false;
	}

	public void load()
	{
		variations.clear();

		int variation = 0;
		boolean b;
		do
		{
			AnimationModel m = new AnimationModel(path, animationIndex, variation);
			m.loadSpriteCount();
			b = m.isUsed();

			if (b)
			{
				variations.add(m);
				++variation;
			}
		}
		while (b);
	}

	@Override
	public void unpack(InputStream stream) throws IOException, PrismException
	{
		for (int i = 0; i < 20; ++i)
		{
			int ms = IntPacker.unpack_s(stream);
			if (i < variations.size())
				variations.get(i).setDurationMs(ms);
		}
	}

	@Override
	public void pack(OutputStream stream) throws IOException
	{
		for (int i = 0; i < 20; ++i)
		{
			int ms = i < variations.size() ? variations.get(i).getDurationMs() : 1000;
			IntPacker.pack_s(stream, ms);
		}
	}

	public AnimationIndex getAnimationIndex()
	{
		return animationIndex;
	}

	public boolean isUsed()
	{
		return isUnlocked || variations.size() > 0;
	}

	public AnimationModel getVariation(int variation)
	{
		return variations.get(variation);
	}

	public void unlock()
	{
		this.isUnlocked = true;
	}

	public IPath_r getPath()
	{
		return path;
	}
}
