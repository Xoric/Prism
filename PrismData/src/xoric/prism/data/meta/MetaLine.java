package xoric.prism.data.meta;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.List;

import xoric.prism.data.exceptions.IInfoLayer;
import xoric.prism.data.exceptions.PrismException;
import xoric.prism.data.exceptions.UserErrorText;
import xoric.prism.data.heap.Heap;
import xoric.prism.data.packable.HeapPacker_s;
import xoric.prism.data.packable.IPackable;
import xoric.prism.data.packable.IntPacker;

public class MetaLine implements IPackable, IInfoLayer
{
	private MetaKey key;
	private Heap heap;

	private IInfoLayer uplink;

	public MetaLine()
	{
		this.heap = new Heap();
		this.key = MetaKey.ITEM;
	}

	public MetaLine(MetaKey key)
	{
		this.heap = new Heap();
		this.key = key;
	}

	@SuppressWarnings("rawtypes")
	private void checkList(MetaKey key, String name, List list, int min) throws PrismException
	{
		final int n = list == null ? 0 : list.size();

		if (n < min)
		{
			PrismException e = new PrismException();
			// ----
			e.user.setText(UserErrorText.LOCAL_GAME_FILE_CAUSED_PROBLEM);
			// ----
			e.code.setText("MetaLine has too few " + name, min, n);
			// ----
			addExceptionInfoTo(e);
			// ----
			throw e;
		}
	}

	/**
	 * Ensures that there are a minimum number of ints, floats and Texts. Throws an exception if not.
	 * @param ni
	 *            number of ints
	 * @param nf
	 *            number of floats
	 * @param nt
	 *            number of Texts
	 * @throws PrismException
	 */
	public void ensureMinima(int ni, int nf, int nt) throws PrismException
	{
		checkList(key, "ints", heap.ints, ni);
		checkList(key, "floats", heap.floats, nf);
		checkList(key, "texts", heap.texts, nt);
	}

	@Override
	public String toString()
	{
		return "key=" + key.toString() + ", heap=" + heap.toString();
	}

	public MetaKey getKey()
	{
		return key;
	}

	public Heap getHeap()
	{
		return heap;
	}

	@Override
	public void pack(OutputStream stream) throws IOException
	{
		IntPacker.pack_s(stream, key.ordinal());
		HeapPacker_s.pack_s(stream, heap, 2);
	}

	@Override
	public void unpack(InputStream stream) throws IOException, PrismException
	{
		int k = IntPacker.unpack_s(stream);
		try
		{
			key = MetaKey.valueOf(k);
		}
		catch (Exception e0)
		{
			PrismException e = new PrismException(e0);
			// ----
			e.user.setText(UserErrorText.LOCAL_GAME_FILE_CAUSED_PROBLEM);
			// ----
			e.code.setText("error casting int (" + k + ") to MetaKey");
			// ----
			addExceptionInfoTo(e);
			// ----
			throw e;
		}
		heap = HeapPacker_s.unpack_s(stream, 2);
	}

	@Override
	public void setUplink(IInfoLayer uplink)
	{
		this.uplink = uplink;
	}

	@Override
	public void addExceptionInfoTo(PrismException e)
	{
		if (uplink != null)
			uplink.addExceptionInfoTo(e);

		e.code.addInfo("MetaLine", toString());
	}
}
