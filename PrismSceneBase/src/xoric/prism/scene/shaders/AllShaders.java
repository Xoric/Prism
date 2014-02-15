package xoric.prism.scene.shaders;

import java.nio.ByteBuffer;

import xoric.prism.data.exceptions.PrismException;
import xoric.prism.data.global.FileTableDirectoryIndex;
import xoric.prism.data.global.Prism;
import xoric.prism.data.global.ShaderIndex;
import xoric.prism.data.meta.AttachmentLoader;
import xoric.prism.data.meta.MetaFile;
import xoric.prism.data.meta.MetaList_in;
import xoric.prism.data.meta.MetaType;

public class AllShaders
{
	public static IShader2 defaultShader;

	public static void load(IShaderIO shaderIO) throws PrismException
	{
		try
		{
			defaultShader = loadShader(shaderIO, ShaderIndex.DEFAULT);
		}
		catch (Exception e)
		{
			System.out.println("using shader substitute");
			defaultShader = shaderIO.createShaderSubstitute();
		}
	}

	private static IShader2 loadShader(IShaderIO shaderIO, ShaderIndex si) throws PrismException
	{
		MetaFile mf = Prism.global.loadMetaFile(FileTableDirectoryIndex.SHADER, si.ordinal());
		mf.load();
		MetaList_in ml = mf.getMetaList();
		ml.claimMetaBlock(MetaType.SHADER);
		AttachmentLoader al = mf.getAttachmentLoader();

		byte[] vertexBuf = al.loadAttachment(0);
		ByteBuffer vertex = ByteBuffer.allocateDirect(vertexBuf.length);
		vertex.put(vertexBuf);
		vertex.flip();

		byte[] fragmentBuf = al.loadAttachment(1);
		ByteBuffer fragment = ByteBuffer.allocateDirect(fragmentBuf.length);
		fragment.put(fragmentBuf);
		fragment.flip();

		IShader2 shader = shaderIO.createShader(vertex, fragment);

		return shader;
	}
}
