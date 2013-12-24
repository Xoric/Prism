package xoric.prism.scene.lwjgl;

import java.io.File;
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
import xoric.prism.exceptions.PrismFileException;
import xoric.prism.scene.exceptions.PrismShaderException;
import xoric.prism.scene.shader.IShader2;
import xoric.prism.scene.shader.ShaderType;

public class ShaderIO2 implements IActor
{
	private static ShaderIO2 instance;

	private ShaderIO2()
	{
	}

	public static ShaderIO2 getInstance()
	{
		if (instance == null)
			instance = new ShaderIO2();

		return instance;
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

	private int createProgram(int vertShader, int fragShader) throws PrismShaderException
	{
		int program = 0;
		String error = "";
		Exception originalException = null;
		try
		{
			int p = ARBShaderObjects.glCreateProgramObjectARB();
			if (p != 0)
			{
				// if the vertex and fragment shaders setup sucessfully,
				// attach them to the shader program, link the sahder program and validate

				ARBShaderObjects.glAttachObjectARB(p, vertShader);
				ARBShaderObjects.glAttachObjectARB(p, fragShader);

				ARBShaderObjects.glLinkProgramARB(p);
				if (ARBShaderObjects.glGetObjectParameteriARB(p, ARBShaderObjects.GL_OBJECT_LINK_STATUS_ARB) == GL11.GL_FALSE)
					error = getLogInfo(p);

				if (error.length() == 0)
				{
					ARBShaderObjects.glValidateProgramARB(p);
					if (ARBShaderObjects.glGetObjectParameteriARB(p, ARBShaderObjects.GL_OBJECT_VALIDATE_STATUS_ARB) == GL11.GL_FALSE)
						error = getLogInfo(p);
				}
				if (error.length() == 0)
					program = p;
			}
		}
		catch (Exception e0)
		{
			originalException = e0;
		}

		if (originalException != null || error.length() > 0 || program == 0)
		{
			ErrorCode c = new ErrorCode(this, ErrorID.COMPILE_ERROR);
			PrismShaderException e = new PrismShaderException(c);
			if (originalException != null)
				e.appendOriginalException(originalException);
			if (error.length() > 0)
				e.appendInfo(error);

			throw e;
		}
		return program;
	}

	public IShader2 createShader(ByteBuffer vertexShader, ByteBuffer pixelShader) throws PrismShaderException
	{
		IShader2 shader = null;

		// create vertex shader and fragment/pixel shader
		int vertShader = createShader(vertexShader, ARBVertexShader.GL_VERTEX_SHADER_ARB);
		int fragShader = createShader(pixelShader, ARBFragmentShader.GL_FRAGMENT_SHADER_ARB);

		// create program
		int program = createProgram(vertShader, fragShader);

		// create shader object
		shader = new Shader2(program);

		return shader;
	}

	public IShader2 createShader(File vertexFile, File pixelFile) throws PrismFileException, PrismShaderException
	{
		ByteBuffer vertexBuf = readFileAsByteBuffer(vertexFile);
		ByteBuffer pixelBuf = readFileAsByteBuffer(pixelFile);

		IShader2 shader = createShader(vertexBuf, pixelBuf);

		return shader;
	}

	@Override
	public ActorID getActorID()
	{
		return ActorID.SHADERIO;
	}

	private ByteBuffer readFileAsByteBuffer(File file) throws PrismFileException
	{
		ByteBuffer buf = null;
		try
		{
			byte[] data = Files.readAllBytes(file.toPath());
			buf = ByteBuffer.allocateDirect(data.length);
			buf.put(data);
			buf.flip();
		}
		catch (Exception e0)
		{
			ErrorCode c = new ErrorCode(this, ErrorID.READ_ERROR);
			PrismFileException e = new PrismFileException(c, file);
			e.appendOriginalException(e0);
			throw e;
		}
		return buf;
	}

	private static String getLogInfo(int obj)
	{
		return ARBShaderObjects.glGetInfoLogARB(obj, ARBShaderObjects.glGetObjectParameteriARB(obj,
				ARBShaderObjects.GL_OBJECT_INFO_LOG_LENGTH_ARB));
	}
}