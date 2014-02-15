package xoric.prism.data.meta;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;

import xoric.prism.data.exceptions.IInfoLayer;
import xoric.prism.data.exceptions.PrismException;
import xoric.prism.data.exceptions.UserErrorText;
import xoric.prism.data.heap.Heap_in;
import xoric.prism.data.packable.HeapPacker_s;
import xoric.prism.data.packable.IPackable_in;
import xoric.prism.data.packable.IntPacker;

public class MetaLine_in extends MetaLineBase implements IInfoLayer, IPackable_in
{
	private final Heap_in heap;
	private IInfoLayer uplink;

	public MetaLine_in(IInfoLayer uplink)
	{
		this.heap = new Heap_in();
		setUplink(uplink);
	}

	public MetaLine_in(IInfoLayer uplink, MetaLine_out l)
	{
		this.heap = new Heap_in(l.getHeap());
		setUplink(uplink);
	}

	@Override
	public Heap_in getHeap()
	{
		return heap;
	}

	@Override
	public MetaKey getKey()
	{
		return key;
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
	public void unpack(InputStream stream) throws IOException, PrismException
	{
		int v = IntPacker.unpack_s(stream);
		key = MetaKey.valueOf(v);

		HeapPacker_s.unpack_s(stream, 2, heap);
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
}
