package xoric.prism.data.types;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

public class HeapPacker implements IPackable
{
	private final byte buf[] = new byte[9];

	private final IntPacker intPacker = new IntPacker();
	private final FloatPacker floatPacker = new FloatPacker();
	private final TextPacker textPacker = new TextPacker();

	private Heap heap;

	public HeapPacker()
	{
		setFloatDecimalPlaces(2);
	}

	public HeapPacker(int floatDecimalPlaces)
	{
		setFloatDecimalPlaces(floatDecimalPlaces);
	}

	public void setHeap(Heap heap)
	{
		this.heap = heap;
	}

	public Heap getHeap()
	{
		return heap;
	}

	public void setFloatDecimalPlaces(int floatDecimalPlaces)
	{
		floatPacker.setDecimalPlaces(floatDecimalPlaces);
	}

	@Override
	public void pack(OutputStream stream) throws IOException
	{
		int mode = calcMode();
		int i = heap.getIntCount();
		int f = heap.getFloatCount();
		int t = heap.getTextCount();

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
		{
			intPacker.setValue(heap.ints.get(j));
			intPacker.pack(stream);
		}

		// pack floats
		for (int j = 0; j < f; ++j)
		{
			floatPacker.setValue(heap.floats.get(j));
			floatPacker.pack(stream);
		}

		// pack texts
		for (int j = 0; j < t; ++j)
		{
			textPacker.setText(heap.texts.get(j));
			textPacker.pack(stream);
		}
	}

	@Override
	public void unpack(InputStream stream) throws IOException
	{
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

		// create heap
		heap = new Heap(i, f, t);

		// unpack ints
		for (int j = 0; j < i; ++j)
		{
			intPacker.unpack(stream);
			heap.ints.add(intPacker.getValue());
		}

		// unpack floats
		for (int j = 0; j < f; ++j)
		{
			floatPacker.unpack(stream);
			heap.floats.add(floatPacker.getValue());
		}

		// pack texts	
		for (int j = 0; j < t; ++j)
		{
			textPacker.unpack(stream);
			heap.texts.add(textPacker.getText());
		}
	}

	public int calcPackedSize()
	{
		int mode = calcMode();
		int bytes;

		if (mode == 0)
			bytes = 1;
		else if (mode == 1)
			bytes = 3;
		else
			bytes = 9;

		for (int i = 0; i < heap.ints.size(); ++i)
		{
			intPacker.setValue(heap.ints.get(i));
			bytes += intPacker.calcPackedSize();
		}

		for (int i = 0; i < heap.floats.size(); ++i)
		{
			floatPacker.setValue(heap.floats.get(i));
			bytes += floatPacker.calcPackedSize();
		}

		for (int i = 0; i < heap.texts.size(); ++i)
		{
			textPacker.setText(heap.texts.get(i));
			bytes += textPacker.calcPackedSize();
		}

		return bytes;
	}

	private int calcMode()
	{
		int mode;
		int i = heap.getIntCount();
		int f = heap.getFloatCount();
		int t = heap.getTextCount();

		if (i < 8 && f < 4 && t < 4)
			mode = 0;
		else if (i < 256 && f < 128 && t < 128)
			mode = 1;
		else
			mode = 2;

		return mode;
	}
}
