package xoric.prism.scene.lwjgl;

import java.io.DataInputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.ByteBuffer;

import org.lwjgl.BufferUtils;
import org.lwjgl.opengl.ARBFragmentShader;
import org.lwjgl.opengl.ARBShaderObjects;
import org.lwjgl.opengl.ARBVertexShader;

import xoric.prism.data.Point;

@Deprecated
public abstract class ShaderIO
{
	private static byte[] loadFileIntoArray(File file) throws IOException
	{
		ClassLoader loader = ShaderIO.class.getClassLoader();
		InputStream inputStream = loader.getResourceAsStream(file.toString());
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

	private static Point registerShaders(ByteBuffer vertexShader, ByteBuffer pixelShader)
	{
		// register shaders and request two ints to reference them
		int vertexShaderID = ARBShaderObjects.glCreateShaderObjectARB(ARBVertexShader.GL_VERTEX_SHADER_ARB);
		int pixelShaderID = ARBShaderObjects.glCreateShaderObjectARB(ARBFragmentShader.GL_FRAGMENT_SHADER_ARB);

		// pass source code to openGL and compile it
		ARBShaderObjects.glShaderSourceARB(vertexShaderID, vertexShader);
		ARBShaderObjects.glCompileShaderARB(vertexShaderID);
		ARBShaderObjects.glShaderSourceARB(pixelShaderID, pixelShader);
		ARBShaderObjects.glCompileShaderARB(pixelShaderID);

		// return in a point
		Point result = new Point(vertexShaderID, pixelShaderID);
		return result;
	}

	public static Point loadShader(File vertexFile, File fragmentFile) throws IOException
	{
		byte[] data = loadFileIntoArray(vertexFile);
		ByteBuffer vertexBuf = convertArrayToBuffer(data);

		data = loadFileIntoArray(fragmentFile);
		ByteBuffer fragmentBuf = convertArrayToBuffer(data);

		Point ids = registerShaders(vertexBuf, fragmentBuf);
		return ids;
	}
}