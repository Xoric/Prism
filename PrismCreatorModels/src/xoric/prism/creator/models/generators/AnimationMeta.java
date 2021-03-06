package xoric.prism.creator.models.generators;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;

import xoric.prism.data.packable.IPackable;
import xoric.prism.data.packable.IntPacker;
import xoric.prism.world.entities.ViewAngle;

class AnimationMeta implements IPackable
{
	private final List<ViewAngle> angles;
	private final List<Integer> columnCounts;

	public AnimationMeta()
	{
		angles = new ArrayList<ViewAngle>();
		columnCounts = new ArrayList<Integer>();
	}

	public void addAngle(ViewAngle v, int columns)
	{
		angles.add(v);
		columnCounts.add(columns);
	}

	public int getCount()
	{
		return angles.size();
	}

	public ViewAngle getAngle(int index)
	{
		return angles.get(index);
	}

	public int getColumnCount(int index)
	{
		return columnCounts.get(index);
	}

	@Override
	public void pack(OutputStream stream) throws IOException
	{
		final int n = angles.size();
		IntPacker.pack_s(stream, n);

		for (int i = 0; i < angles.size(); ++i)
		{
			IntPacker.pack_s(stream, angles.get(i).ordinal());
			IntPacker.pack_s(stream, columnCounts.get(i));
		}
	}

	@Override
	public void unpack(InputStream stream) throws IOException
	{
		angles.clear();
		columnCounts.clear();

		final int n = IntPacker.unpack_s(stream);

		for (int i = 0; i < n; ++i)
		{
			int j = IntPacker.unpack_s(stream);
			ViewAngle a = ViewAngle.valueOf(j);
			int columns = IntPacker.unpack_s(stream);

			addAngle(a, columns);
		}
	}
}
