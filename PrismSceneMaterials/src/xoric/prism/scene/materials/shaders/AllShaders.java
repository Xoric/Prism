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
import xoric.prism.scene.shaders.IDefaultShader;
import xoric.prism.scene.shaders.IMaskShader;
import xoric.prism.scene.shaders.IShaderIO;

public class AllShaders
{
	public static IDefaultShader defaultShader;
	public static IMaskShader maskShader;

	public static void loadAll(IShaderIO shaderIO) throws PrismException
	{
		try
		{
			ByteBuffer v = loadBytes(ShaderIndex.DEFAULT, 0); // vertex buffer
			ByteBuffer f = loadBytes(ShaderIndex.DEFAULT, 1); // fragment buffer
			defaultShader = shaderIO.createDefaultShader(v, f);

			v = loadBytes(ShaderIndex.MASK, 0);
			f = loadBytes(ShaderIndex.MASK, 1);
			maskShader = shaderIO.createMaskShader(v, f);
		}
		catch (Exception e)
		{
			System.out.println("using shader substitute");
			defaultShader = shaderIO.createShaderSubstitute();
		}
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
