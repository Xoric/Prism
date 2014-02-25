package xoric.prism.scene.lwjgl.shaders;

import java.nio.ByteBuffer;

import org.lwjgl.opengl.ARBFragmentShader;
import org.lwjgl.opengl.ARBShaderObjects;
import org.lwjgl.opengl.ARBVertexShader;
import org.lwjgl.opengl.GL11;

import xoric.prism.data.exceptions.PrismException;
import xoric.prism.data.exceptions.UserErrorText;
import xoric.prism.scene.cleanup.TrashCan;
import xoric.prism.scene.shaders.IDefaultShader;
import xoric.prism.scene.shaders.IMaskShader;
import xoric.prism.scene.shaders.IShaderIO;

public class ShaderIOLWJGL implements IShaderIO
{
	/**
	 * @param shaderBuffer
	 *            ByteBuffer containing shader code
	 * @param glShaderType
	 *            vertex or pixel/fragment
	 * @return int
	 * @throws Exception
	 */
	private static int createShader(ByteBuffer shaderBuffer, int glShaderType) throws PrismException
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
			e.code.addInfo("log", getLogInfo(shaderID));
			// ----
			e.setText(UserErrorText.SHADER_PROBLEM);
			// ----
			throw e;
		}
		return shaderID;
	}

	private static int createProgram(ByteBuffer vertexShader, ByteBuffer pixelShader) throws PrismException
	{
		// create vertex shader and fragment/pixel shader
		int vertShader = createShader(vertexShader, ARBVertexShader.GL_VERTEX_SHADER_ARB);
		int fragShader = createShader(pixelShader, ARBFragmentShader.GL_FRAGMENT_SHADER_ARB);

		// create program
		int program = createProgram(vertShader, fragShader);

		return program;
	}

	private static int createProgram(int vertShader, int fragShader) throws PrismException
	{
		int program = 0;
		String errorLog = "";

		int p = ARBShaderObjects.glCreateProgramObjectARB();
		if (p != 0)
		{
			// if the vertex and fragment shaders setup sucessfully,
			// attach them to the shader program, link the sahder program and validate

			ARBShaderObjects.glAttachObjectARB(p, vertShader);
			ARBShaderObjects.glAttachObjectARB(p, fragShader);

			ARBShaderObjects.glLinkProgramARB(p);
			if (ARBShaderObjects.glGetObjectParameteriARB(p, ARBShaderObjects.GL_OBJECT_LINK_STATUS_ARB) == GL11.GL_FALSE)
				errorLog = getLogInfo(p);

			if (errorLog.length() == 0)
			{
				ARBShaderObjects.glValidateProgramARB(p);
				if (ARBShaderObjects.glGetObjectParameteriARB(p, ARBShaderObjects.GL_OBJECT_VALIDATE_STATUS_ARB) == GL11.GL_FALSE)
					errorLog = getLogInfo(p);
			}
			if (errorLog.length() == 0)
				program = p;
		}

		if (errorLog.length() > 0 || program == 0)
		{
			PrismException e = new PrismException();
			// ----
			// ----
			if (errorLog.length() > 0)
				e.code.addInfo("log", errorLog);
			// ----
			e.setText(UserErrorText.SHADER_PROBLEM);
			// ----
			throw e;
		}
		return program;
	}

	/*
	@Deprecated
	private static IDefaultShader createShader(ByteBuffer vertexShader, ByteBuffer pixelShader) throws PrismException
	{
		DefaultShader shader = null;

		// create vertex shader and fragment/pixel shader
		int vertShader = createShader(vertexShader, ARBVertexShader.GL_VERTEX_SHADER_ARB);
		int fragShader = createShader(pixelShader, ARBFragmentShader.GL_FRAGMENT_SHADER_ARB);

		// create program
		int program = createProgram(vertShader, fragShader);

		// create shader object
		shader = new DefaultShader(program);

		// initialize shader
		shader.initialize();

		return shader;
	}

	@Deprecated
	public static IDefaultShader createShader(File vertexFile, File pixelFile) throws PrismException
	{
		ByteBuffer vertexBuf = readFileAsByteBuffer(vertexFile);
		ByteBuffer pixelBuf = readFileAsByteBuffer(pixelFile);

		IDefaultShader shader = createShader(vertexBuf, pixelBuf);

		return shader;
	}

	private static ByteBuffer readFileAsByteBuffer(File file) throws PrismException
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
			PrismException e = new PrismException(e0);
			// ----
			e.user.setText(UserErrorText.READ_ERROR);
			// ----
			e.code.setText("error while reading a shader file - this is not a compile error");
			// ----
			e.addInfo("file", file.toString());
			// ----
			throw e;
		}
		return buf;
	}	
	*/

	private static String getLogInfo(int obj)
	{
		return ARBShaderObjects.glGetInfoLogARB(obj, ARBShaderObjects.glGetObjectParameteriARB(obj,
				ARBShaderObjects.GL_OBJECT_INFO_LOG_LENGTH_ARB));
	}

	// IShaderIO:
	@Override
	public IDefaultShader createDefaultShader(ByteBuffer vertexShader, ByteBuffer pixelShader) throws PrismException
	{
		// create and initialize shader object
		int program = createProgram(vertexShader, pixelShader);
		DefaultShader shader = new DefaultShader(program);
		shader.initialize();
		TrashCan.addResource(shader);

		return shader;
	}

	// IShaderIO:
	@Override
	public IMaskShader createMaskShader(ByteBuffer vertexShader, ByteBuffer pixelShader) throws PrismException
	{
		// create and initialize shader object
		int program = createProgram(vertexShader, pixelShader);
		MaskShader shader = new MaskShader(program);
		shader.initialize();
		TrashCan.addResource(shader);

		return shader;
	}

	// IShaderIO:
	@Override
	@Deprecated
	public IDefaultShader createShaderSubstitute()
	{
		DefaultShaderSubstitute d = new DefaultShaderSubstitute();
		d.initialize();

		return d;
	}
}
