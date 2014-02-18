package xoric.prism.data.packable;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import xoric.prism.data.exceptions.PrismException;
import xoric.prism.data.heap.HeapBase;
import xoric.prism.data.heap.Heap_in;
import xoric.prism.data.heap.Heap_out;
import xoric.prism.data.types.Booleans;

public abstract class HeapPacker_s
{
	private static final byte buf[] = new byte[9];

	public static synchronized void pack_s(OutputStream stream, Heap_out heap, int floatDecimals) throws IOException, PrismException
	{
		int mode = calcMode_s(heap);
		int i = heap.ints.size();
		int f = heap.floats.size();
		int t = heap.texts.size();
		int b = heap.bools.getNeededBlockCount();

		if (mode == 0)
		{
			//  [ttffiiin]
			//			int n = (i << 1) | (f << 4) | (t << 6);

			// [btffiii0]
			int n = (i << 1) | (f << 4) | (t << 6) | (b << 7);

			buf[0] = (byte) n;

			stream.write(buf, 0, 1);
		}
		else if (mode == 1)
		{
			//			buf[0] = (byte) (0x1 | (f << 1));
			//			buf[1] = (byte) (t << 1);
			//			buf[2] = (byte) i;

			buf[0] = (byte) (0x1 | (f << 1));
			buf[1] = (byte) ((t << 1) | (b << 6)); // bbttttt0
			buf[2] = (byte) i;

			stream.write(buf, 0, 3);
		}
		else
		{
			// [0] 	fffffff1
			// [1] 	ttttttt1
			// [2] 	ffffffff
			// [3] 	ffffffff
			// [4] 	tttttttt
			// [5] 	tttttttt
			// [6] 	iiiiiiii
			// [7] 	iiiiiiii
			// [8] 	iiiiiiii
			//			buf[0] = (byte) (0x1 | (f << 1));
			//			buf[1] = (byte) (0x1 | (t << 1));
			//			for (int j = 1; j < 3; ++j)
			//			{
			//				buf[1 + j] = (byte) (f >> (j * 8 - 1));
			//				buf[3 + j] = (byte) (t >> (j * 8 - 1));
			//			}
			//			for (int j = 0; j < 3; ++j)
			//				buf[6 + j] = (byte) (i >> (j * 8));

			// [0] 	bbbbbbb1
			// [1] 	ttttttt1
			// [2] 	ffffffff
			// [3] 	ffffffff
			// [4] 	tttttttt
			// [5] 	tttttttt
			// [6] 	iiiiiiii
			// [7] 	iiiiiiii
			// [8] 	iiiiiiii
			buf[0] = (byte) (0x1 | (b << 1));
			buf[1] = (byte) (0x1 | (t << 1));
			for (int j = 1; j < 3; ++j)
			{
				buf[1 + j] = (byte) (f >> ((j - 1) * 8));
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

		// pack bools
		for (int j = 0; j < b; ++j)
			stream.write(heap.bools.getBlock(j).toInt());
	}

	public static synchronized void unpack_s(InputStream stream, int floatDecimals, Heap_in heap) throws IOException, PrismException
	{
		// reset heap
		heap.clear();

		// read number of ints, floats and texts
		int k0 = 0xFF & stream.read();
		int mode = 0x1 & k0;
		int i, f, t, b;

		if (mode == 0)
		{
			//			i = (k0 >> 1) & 0x7;
			//			f = (k0 >> 4) & 0x3;
			//			t = k0 >> 6;

			i = (k0 >> 1) & 0x7;
			f = (k0 >> 4) & 0x3;
			t = (k0 >> 6) & 0x1;
			b = k0 >> 7;
		}
		else
		{
			stream.read(buf, 1, 2);
			int k1 = 0xFF & buf[1];
			mode = (k1 & 0x1) > 0 ? 2 : 1;

			if (mode == 1)
			{
				//				f = (k0 >> 1) & 0x7F;
				//				t = k1 >> 1;
				//				i = 0xFF & buf[2];

				f = k0 >> 1;//) & 0x7F;
				t = (k1 >> 1) & 0x1F;
				b = (k1 >> 6);// & 0x3;
				i = 0xFF & buf[2];
			}
			else
			{
				stream.read(buf, 3, 6);

				//				i = 0;
				//				f = k0 >> 1;
				//				t = k1 >> 1;

				i = 0;
				f = 0;
				t = k1 >> 1;
				b = k0 >> 1;

				for (int j = 1; j < 3; ++j)
				{
					f |= (0xFF & buf[1 + j]) << ((j - 1) * 8);
					t |= (0xFF & buf[3 + j]) << (j * 8 - 1);
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

		// unpack texts	
		for (int j = 0; j < t; ++j)
			heap.texts.add(TextPacker.unpack_s(stream));

		// unpack bools	
		for (int j = 0; j < b; ++j)
			heap.bools.addBlock(new Booleans(stream.read()));
	}

	public static Heap_in unpack_s(InputStream stream, int floatDecimals) throws IOException, PrismException
	{
		Heap_in heap = new Heap_in();
		unpack_s(stream, floatDecimals, heap);
		return heap;
	}

	public static int calcPackedSize_s(HeapBase h, int floatDecimals) throws PrismException
	{
		int mode = calcMode_s(h);
		int bytes;

		if (mode == 0)
			bytes = 1;
		else if (mode == 1)
			bytes = 3;
		else
			bytes = 9;

		for (int i = 0; i < h.ints.size(); ++i)
			bytes += IntPacker.calcPackedSize_s(h.ints.get(i));

		for (int i = 0; i < h.floats.size(); ++i)
			bytes += FloatPacker3.calcPackedSize(h.floats.get(i), floatDecimals);

		for (int i = 0; i < h.getTextCount(); ++i)
			bytes += TextPacker.calcPackedSize_s(h.getText(i));

		bytes += h.bools.getNeededBlockCount();

		return bytes;
	}

	private static int calcMode_s(HeapBase heap)
	{
		int mode;
		int i = heap.ints.size();
		int f = heap.floats.size();
		int t = heap.getTextCount();
		int b = heap.bools.getNeededBlockCount();

		//		if (i < 8 && f < 4 && t < 4)
		if (i < 8 && f < 4 && t < 2 && b < 2)
			mode = 0;
		//		else if (i < 256 && f < 128 && t < 128)
		else if (i < 256 && f < 128 && t < 32 && b < 4)
			mode = 1;
		else
			mode = 2;

		return mode;
	}

	/*
	public static void main(String[] args)
	{
		try
		{
			boolean b;
			int n = 0;
			int succ = 0;
			do
			{
				int mode = (int) (Math.random() * 3);
				if (mode > 2)
					mode = 2;

				b = test(mode);
				if (b)
					++succ;
			}
			while (b & ++n < 50);

			System.out.println();
			System.out.println("successful ... " + succ);
		}
		catch (Exception e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	private static int calcRnd(int mode, int[] min, int[] max)
	{
		int n = min[mode];
		int d = max[mode] - 1 - n;
		n += Math.random() * Math.random() * d;
		return n;
	}

	private static boolean test(int mode) throws IOException, PrismException
	{
		boolean success = true;

		System.out.println();
		System.out.println("mode " + mode);

		int[] intMin = { 0, 8, 256 };
		int[] intMax = { 8, 256, 16777216 };
		int[] floatMin = { 0, 4, 128 };
		int[] floatMax = { 4, 128, 65536 };
		int[] textMin = { 0, 2, 32 };
		int[] textMax = { 2, 32, 8388608 };
		int[] boolBlockMin = { 0, 2, 4 };
		int[] boolBlockMax = { 2, 4, 128 };

		Heap_out heapOut = new Heap_out();

		int intCount = calcRnd(mode, intMin, intMax);
		int floatCount = calcRnd(mode, floatMin, floatMax);
		int textCount = calcRnd(mode, textMin, textMax);
		int boolBlockCount = calcRnd(mode, boolBlockMin, boolBlockMax);

		int maxTrue = 0;
		// ================================================================================================================
		for (int i = 0; i < intCount; ++i)
		{
			int v = 9;
			heapOut.ints.add(v);
		}
		for (int i = 0; i < floatCount; ++i)
		{
			float f = 9.0f;
			heapOut.floats.add(f);
		}
		for (int i = 0; i < textCount; ++i)
		{
			Text t = new Text("NINE");
			heapOut.texts.add(t);
		}
		for (int i = 0; i < boolBlockCount * 8; ++i)
		{
			boolean b = Math.random() < 0.1;
			heapOut.bools.add(b);
			if (b)
				maxTrue = i;
		}
		// ================================================================================================================
		ByteArrayOutputStream streamOut = new ByteArrayOutputStream();
		HeapPacker_s.pack_s(streamOut, heapOut, 2);
		streamOut.close();

		byte[] buf = streamOut.toByteArray();
		ByteArrayInputStream streamIn = new ByteArrayInputStream(buf);
		Heap_in heapIn = HeapPacker_s.unpack_s(streamIn, 2);
		streamIn.close();
		// ================================================================================================================
		success &= heapIn.ints.size() == intCount;

		if (success)
		{
			int o = 0;

			for (int i = 0; i < intCount; ++i)
				if (heapOut.ints.get(i) == heapIn.ints.get(i))
					++o;

			success &= o == intCount;
		}
		// ================================================================================================================
		success &= heapIn.floats.size() == floatCount;

		if (success)
		{
			int o = 0;

			for (int i = 0; i < floatCount; ++i)
				if (heapOut.floats.get(i) >= heapIn.floats.get(i) - 1.0f && heapOut.floats.get(i) <= heapIn.floats.get(i) + 1.0f)
					++o;

			success &= o == floatCount;
		}
		// ================================================================================================================
		success &= heapIn.texts.size() == textCount;

		if (success)
		{
			int o = 0;

			for (int i = 0; i < textCount; ++i)
				if (heapOut.texts.get(i).equals(heapIn.texts.get(i)))
					++o;

			success &= o == textCount;
		}
		// ================================================================================================================
		if (success)
			for (int i = 0; i < maxTrue; ++i)
				success &= heapOut.bools.get(i) == heapIn.bools.get(i);
		// ================================================================================================================
		return success;
	}
	*/
}
