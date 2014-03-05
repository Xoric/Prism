package xoric.prism.scene.materials.shaders;

import java.nio.ByteBuffer;

import xoric.prism.data.exceptions.PrismException;
import xoric.prism.data.global.FileTableDirectoryIndex;
import xoric.prism.data.global.Prism;
import xoric.prism.data.global.ShaderIndex;
import xoric.prism.data.meta.AttachmentLoader;
import xoric.prism.data.meta.MetaFile;
import xoric.prism.data.meta.MetaList_in;
import xoric.prism.data.meta.MetaType;
import xoric.prism.scene.shaders.IColorMaskShader;
import xoric.prism.scene.shaders.IColorShader;
import xoric.prism.scene.shaders.IMaskMaskShader;
import xoric.prism.scene.shaders.IShaderIO;

public class AllShaders
{
	public static IColorShader color;
	public static IColorMaskShader mask;
	public static IMaskMaskShader hole;
	public static IColorMaskShader growth;

	public static void loadAll(IShaderIO shaderIO) throws PrismException
	{
		try
		{
			ByteBuffer v = loadBytes(ShaderIndex.DEFAULT, 0); // vertex buffer
			ByteBuffer f = loadBytes(ShaderIndex.DEFAULT, 1); // fragment buffer
			color = shaderIO.createDefaultShader(v, f);
		}
		catch (Exception e)
		{
			System.err.println("using shader substitute: " + e.toString());
			color = shaderIO.createShaderSubstitute();
		}

		ByteBuffer v = loadBytes(ShaderIndex.MASK, 0);
		ByteBuffer f = loadBytes(ShaderIndex.MASK, 1);
		mask = shaderIO.createColorMaskShader(v, f);

		v = loadBytes(ShaderIndex.HOLE, 0);
		f = loadBytes(ShaderIndex.HOLE, 1);
		hole = shaderIO.createMaskMaskShader(v, f);

		v = loadBytes(ShaderIndex.GROWTH, 0);
		f = loadBytes(ShaderIndex.GROWTH, 1);
		growth = shaderIO.createColorMaskShader(v, f);
	}

	private static ByteBuffer loadBytes(ShaderIndex shaderIndex, int attachmentIndex) throws PrismException
	{
		MetaFile mf = Prism.global.loadMetaFile(FileTableDirectoryIndex.SHADER, shaderIndex.ordinal());
		MetaList_in ml = mf.getMetaList();
		ml.claimMetaBlock(MetaType.SHADER);
		AttachmentLoader al = mf.getAttachmentLoader();

		byte[] b = al.loadAttachment(attachmentIndex);
		ByteBuffer buf = ByteBuffer.allocateDirect(b.length);
		buf.put(b);
		buf.flip();

		return buf;
	}
}
