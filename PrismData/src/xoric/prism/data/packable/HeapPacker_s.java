package xoric.prism.data.packable;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import xoric.prism.data.exceptions.PrismException;
import xoric.prism.data.heap.Heap_r;
import xoric.prism.data.heap.Heap_w;

public abstract class HeapPacker_s
{
	private static final byte buf[] = new byte[9];

	public static synchronized void pack_s(OutputStream stream, Heap_w heap, int floatDecimals) throws IOException, PrismException
	{
		int mode = calcMode_s(heap);
		int i = heap.ints.size();
		int f = heap.floats.size();
		int t = heap.texts.size();

		if (mode == 0)
		{
			int b = (i << 1) | (f << 4) | (t << 6);
			buf[0] = (byte) b;

			stream.write(buf, 0, 1);
		}
		else if (mode == 1)
		{
			buf[0] = (byte) (0x1 | (f << 1));
			buf[1] = (byte) (t << 1);
			buf[2] = (byte) i;

			stream.write(buf, 0, 3);
		}
		else
		{
			buf[0] = (byte) (0x1 | (f << 1));
			buf[1] = (byte) (0x1 | (t << 1));
			for (int j = 1; j < 3; ++j)
			{
				buf[1 + j] = (byte) (f >> (j * 8 - 1));
				buf[3 + j] = (byte) (t >> (j * 8 - 1));
			}
			for (int j = 0; j < 3; ++j)
				buf[6 + j] = (byte) (i >> (j * 8));

			stream.write(buf, 0, 9);
		}

		// pack ints
		for (int j = 0; j < i; ++j)
			IntPacker.pack_s(stream, heap.ints.get(j));

		// pack floats
		FloatPacker3.pack_s(stream, heap.floats, floatDecimals);

		// pack texts
		for (int j = 0; j < t; ++j)
			TextPacker.pack_s(stream, heap.texts.get(j));
	}

	public static synchronized void unpack_s(InputStream stream, int floatDecimals, Heap_r heap) throws IOException, PrismException
	{
		// reset heap
		heap.ints.clear();
		heap.floats.clear();
		heap.texts.clear();

		// read number of ints, floats and texts
		int k0 = 0xFF & stream.read();
		int mode = 0x1 & k0;
		int i, f, t;

		if (mode == 0)
		{
			i = (k0 >> 1) & 0x7;
			f = (k0 >> 4) & 0x3;
			t = k0 >> 6;
		}
		else
		{
			stream.read(buf, 1, 2);
			int k1 = 0xFF & buf[1];
			mode = (k1 & 0x1) > 0 ? 2 : 1;

			if (mode == 1)
			{
				f = (k0 >> 1) & 0x7F;
				t = k1 >> 1;
				i = 0xFF & buf[2];
			}
			else
			{
				stream.read(buf, 3, 6);

				i = 0;
				f = k0 >> 1;
				t = k1 >> 1;

				for (int j = 1; j < 3; ++j)
				{
					f |= buf[1 + j] << (j * 8 - 1);
					t |= buf[3 + j] << (j * 8 - 1);
				}
				for (int j = 0; j < 3; ++j)
					i |= (0xFF & buf[6 + j]) << (j * 8);
			}
		}

		// unpack ints
		for (int j = 0; j < i; ++j)
			heap.ints.add(IntPacker.unpack_s(stream));

		// unpack floats
		FloatPacker3.unpack_s(stream, heap.floats, floatDecimals, f);

		// pack texts	
		for (int j = 0; j < t; ++j)
			heap.texts.add(TextPacker.unpack_s(stream));
	}

	public static Heap unpack_s(InputStream stream, int floatDecimals) throws IOException, PrismException
	{
		Heap heap = new Heap();
		unpack_s(stream, floatDecimals, heap);
		return heap;
	}

	public static int calcPackedSize_s(Heap heap, int floatDecimals) throws PrismException
	{
		int mode = calcMode_s(heap);
		int bytes;

		if (mode == 0)
			bytes = 1;
		else if (mode == 1)
			bytes = 3;
		else
			bytes = 9;

		for (int i = 0; i < heap.ints.size(); ++i)
			bytes += IntPacker.calcPackedSize_s(heap.ints.get(i));

		for (int i = 0; i < heap.floats.size(); ++i)
			bytes += FloatPacker3.calcPackedSize(heap.floats.get(i), floatDecimals);

		for (int i = 0; i < heap.texts.size(); ++i)
			bytes += TextPacker.calcPackedSize_s(heap.texts.get(i));

		return bytes;
	}

	private static int calcMode_s(Heap heap)
	{
		int mode;
		int i = heap.ints.size();
		int f = heap.floats.size();
		int t = heap.texts.size();

		if (i < 8 && f < 4 && t < 4)
			mode = 0;
		else if (i < 256 && f < 128 && t < 128)
			mode = 1;
		else
			mode = 2;

		return mode;
	}
}
