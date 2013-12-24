package xoric.prism.scene.lwjgl;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.nio.ByteBuffer;
import java.nio.file.Files;

import org.lwjgl.opengl.ARBFragmentShader;
import org.lwjgl.opengl.ARBShaderObjects;
import org.lwjgl.opengl.ARBVertexShader;
import org.lwjgl.opengl.GL11;

import xoric.prism.data.modules.ActorID;
import xoric.prism.data.modules.ErrorCode;
import xoric.prism.data.modules.ErrorID;
import xoric.prism.data.modules.IActor;
import xoric.prism.scene.exceptions.PrismShaderException;
import xoric.prism.scene.shader.IShader;
import xoric.prism.scene.shader.ShaderType;

@Deprecated
public class Shader implements IShader, IActor
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
	private int createShader(ByteBuffer shaderBuffer, int glShaderType) throws PrismShaderException
	{
		PrismShaderException exception = null;
		int shaderID = 0;
		try
		{
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
				ErrorCode c = new ErrorCode(this, ErrorID.COMPILE_ERROR);
				exception = new PrismShaderException(c);
				exception.appendInfo("info", getLogInfo(shaderID));
			}
		}
		catch (Exception e0)
		{
			// catch any exception
			ARBShaderObjects.glDeleteObjectARB(shaderID);

			ErrorCode c = new ErrorCode(this, ErrorID.COMPILE_ERROR);
			exception = new PrismShaderException(c);
			exception.appendOriginalException(e0);
		}

		if (exception != null)
		{
			// throw exception if any
			ShaderType shaderType;
			if (glShaderType == ARBVertexShader.GL_VERTEX_SHADER_ARB)
				shaderType = ShaderType.VERTEX_SHADER;
			if (glShaderType == ARBFragmentShader.GL_FRAGMENT_SHADER_ARB)
				shaderType = ShaderType.PIXEL_SHADER;
			else
				shaderType = ShaderType.UNKNOWN;

			exception.appendInfo("type", shaderType.toString());

			throw exception;
		}
		return shaderID;
	}

	@Override
	public void createShader(ByteBuffer vertexShader, ByteBuffer pixelShader) throws PrismShaderException
	{
		PrismShaderException exception = null;

		int vertShader, fragShader;
		try
		{
			// create vertex shader and fragment/pixel shader
			vertShader = createShader(vertexShader, ARBVertexShader.GL_VERTEX_SHADER_ARB);
			fragShader = createShader(pixelShader, ARBFragmentShader.GL_FRAGMENT_SHADER_ARB);

			if (vertShader == 0 || fragShader == 0)
			{
				ErrorCode c = new ErrorCode(this, ErrorID.COMPILE_ERROR);
				exception = new PrismShaderException(c);
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
		catch (PrismShaderException e0)
		{
			throw e0;
		}
		catch (Exception e0)
		{
			ErrorCode c = new ErrorCode(this, ErrorID.COMPILE_ERROR);
			exception = new PrismShaderException(c);
			exception.appendOriginalException(e0);
		}

		if (exception != null)
		{
			throw exception;
		}
	}

	public void createFrom(File vertexFile, File pixelFile) throws Exception
	{
		ByteBuffer vertexBuf = readFileAsByteBuffer(vertexFile);
		ByteBuffer pixelBuf = readFileAsByteBuffer(pixelFile);

		createShader(vertexBuf, pixelBuf);
	}

	public void activate()
	{
		ARBShaderObjects.glUseProgramObjectARB(program);
	}

	@Override
	public ActorID getActorID()
	{
		return ActorID.SHADER;
	}

	private ByteBuffer readFileAsByteBuffer(File file) throws Exception
	{
		byte[] data = Files.readAllBytes(file.toPath());
		ByteBuffer buf = ByteBuffer.allocateDirect(data.length);
		buf.put(data);
		buf.flip();
		return buf;
	}

	private String readFileAsString(String filename) throws Exception
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
}