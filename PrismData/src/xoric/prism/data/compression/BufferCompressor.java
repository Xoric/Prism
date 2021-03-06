package xoric.prism.data.compression;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.zip.GZIPOutputStream;

public abstract class BufferCompressor
{
	public static byte[] compressBuffer(byte[] data) throws IOException
	{
		ByteArrayOutputStream o = new ByteArrayOutputStream();
		GZIPOutputStream out = new GZIPOutputStream(o);
		out.write(data);
		out.close();

		return o.toByteArray();
	}

	public static int calcCompressedSize(byte[] buf) throws IOException
	{
		byte[] compressed = compressBuffer(buf);
		int packedSize = compressed.length;

		return packedSize;
	}

	public static int calcCompressedSize(File file) throws IOException
	{
		int originalSize = (int) file.length();
		byte[] buf = new byte[originalSize];
		FileInputStream in = new FileInputStream(file);
		in.read(buf);
		in.close();

		int packedSize = calcCompressedSize(buf);

		return packedSize;
	}

	public static double calcCompressionRate(File file) throws Exception
	{
		int originalSize = (int) file.length();
		int packedSize = calcCompressedSize(file);

		double d = packedSize;
		d /= originalSize;

		return d;
	}
}
