package xoric.prism.scene.lwjgl;

import java.io.DataInputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.ByteBuffer;

import org.lwjgl.BufferUtils;

public abstract class ShaderIO
{
	private static byte[] loadFileIntoArray(String file) throws IOException
	{
		ClassLoader loader = ShaderIO.class.getClassLoader();
		InputStream inputStream = loader.getResourceAsStream(file);
		byte[] buf = null;
		DataInputStream dataStream = new DataInputStream(inputStream);
		dataStream.readFully(buf = new byte[inputStream.available()]);
		dataStream.close();
		inputStream.close();

		return buf;
	}

	private static ByteBuffer convertArrayToBuffer(byte[] buf)
	{
		ByteBuffer byteBuffer = BufferUtils.createByteBuffer(buf.length);
		byteBuffer.put(buf);
		byteBuffer.flip();

		return byteBuffer;
	}

	public static void loadShader(File vertexFile, File fragmentFile)
	{
		//		ClassLoader loader = PrismSceneLWJGL.class.getClassLoader();
		//		InputStream is = loader.getResourceAsStream(file);
		//		byte[] shadercode = null;
	}
}
