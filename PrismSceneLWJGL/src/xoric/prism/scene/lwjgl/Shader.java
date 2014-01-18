package xoric.prism.scene.lwjgl;

import java.io.File;
import java.nio.ByteBuffer;
import java.nio.file.Files;

import org.lwjgl.opengl.ARBFragmentShader;
import org.lwjgl.opengl.ARBShaderObjects;
import org.lwjgl.opengl.ARBVertexShader;
import org.lwjgl.opengl.GL11;

import xoric.prism.data.exceptions.PrismException;
import xoric.prism.data.exceptions.UserErrorText;
import xoric.prism.scene.shaders.IShader;

@Deprecated
public class Shader implements IShader
{
	private int program;

	public Shader()
	{
		program = 0;
	}

	private static String getLogInfo(int obj)
	{
		return ARBShaderObjects.glGetInfoLogARB(obj, ARBShaderObjects.glGetObjectParameteriARB(obj,
				ARBShaderObjects.GL_OBJECT_INFO_LOG_LENGTH_ARB));
	}

	/**
	 * @param shaderBuffer
	 *            ByteBuffer containing shader code
	 * @param glShaderType
	 *            vertex or pixel/fragment
	 * @return int
	 * @throws Exception
	 */
	private int createShader(ByteBuffer shaderBuffer, int glShaderType) throws PrismException
	{
		int shaderID = 0;

		// create shader
		shaderID = ARBShaderObjects.glCreateShaderObjectARB(glShaderType);
		if (shaderID == 0)
			return 0;

		// compile shader
		ARBShaderObjects.glShaderSourceARB(shaderID, shaderBuffer);
		ARBShaderObjects.glCompileShaderARB(shaderID);

		// check for errors
		if (ARBShaderObjects.glGetObjectParameteriARB(shaderID, ARBShaderObjects.GL_OBJECT_COMPILE_STATUS_ARB) == GL11.GL_FALSE)
		{
			PrismException e = new PrismException();
			// ----
			// ----
			// ----
			e.setText(UserErrorText.SHADER_PROBLEM);
			// ----
			throw e;
		}
		return shaderID;
	}

	@Override
	public void createShader(ByteBuffer vertexShader, ByteBuffer pixelShader) throws PrismException
	{
		int vertShader, fragShader;

		// create vertex shader and fragment/pixel shader
		vertShader = createShader(vertexShader, ARBVertexShader.GL_VERTEX_SHADER_ARB);
		fragShader = createShader(pixelShader, ARBFragmentShader.GL_FRAGMENT_SHADER_ARB);

		if (vertShader == 0 || fragShader == 0)
		{
			PrismException e = new PrismException();
			// ----
			// ----
			// ----
			e.setText(UserErrorText.SHADER_PROBLEM);
			// ----
			throw e;
		}
		else
		{
			program = ARBShaderObjects.glCreateProgramObjectARB();
			if (program == 0)
				return;

			// if the vertex and fragment shaders setup sucessfully,
			// attach them to the shader program, link the sahder program
			// (into the GL context I suppose), and validate

			ARBShaderObjects.glAttachObjectARB(program, vertShader);
			ARBShaderObjects.glAttachObjectARB(program, fragShader);

			ARBShaderObjects.glLinkProgramARB(program);
			if (ARBShaderObjects.glGetObjectParameteriARB(program, ARBShaderObjects.GL_OBJECT_LINK_STATUS_ARB) == GL11.GL_FALSE)
			{
				System.err.println(getLogInfo(program));
				return;
			}

			ARBShaderObjects.glValidateProgramARB(program);
			if (ARBShaderObjects.glGetObjectParameteriARB(program, ARBShaderObjects.GL_OBJECT_VALIDATE_STATUS_ARB) == GL11.GL_FALSE)
			{
				System.err.println(getLogInfo(program));
				return;
			}
		}
	}

	public void createFrom(File vertexFile, File pixelFile) throws PrismException
	{
		ByteBuffer vertexBuf = readFileAsByteBuffer(vertexFile);
		ByteBuffer pixelBuf = readFileAsByteBuffer(pixelFile);

		createShader(vertexBuf, pixelBuf);
	}

	public void activate()
	{
		ARBShaderObjects.glUseProgramObjectARB(program);
	}

	private ByteBuffer readFileAsByteBuffer(File file) throws PrismException
	{
		try
		{
			byte[] data = Files.readAllBytes(file.toPath());
			ByteBuffer buf = ByteBuffer.allocateDirect(data.length);
			buf.put(data);
			buf.flip();
			return buf;
		}
		catch (Exception e0)
		{
			PrismException e = new PrismException(e0);
			// ----
			// ----
			// ----
			e.setText(UserErrorText.READ_ERROR);
			e.addInfo("file", file.toString());
			// ----
			throw e;
		}
	}

	/*
	private String readFileAsString(String filename) throws PrismException2
	{
		StringBuilder source = new StringBuilder();

		FileInputStream in = new FileInputStream(filename);

		Exception exception = null;

		BufferedReader reader;
		try
		{
			reader = new BufferedReader(new InputStreamReader(in, "UTF-8"));

			Exception innerExc = null;
			try
			{
				String line;
				while ((line = reader.readLine()) != null)
					source.append(line).append('\n');
			}
			catch (Exception exc)
			{
				exception = exc;
			}
			finally
			{
				try
				{
					reader.close();
				}
				catch (Exception exc)
				{
					if (innerExc == null)
						innerExc = exc;
					else
						exc.printStackTrace();
				}
			}

			if (innerExc != null)
				throw innerExc;
		}
		catch (Exception exc)
		{
			exception = exc;
		}
		finally
		{
			try
			{
				in.close();
			}
			catch (Exception exc)
			{
				if (exception == null)
					exception = exc;
				else
					exc.printStackTrace();
			}

			if (exception != null)
				throw exception;
		}

		return source.toString();
	}
	*/
}
