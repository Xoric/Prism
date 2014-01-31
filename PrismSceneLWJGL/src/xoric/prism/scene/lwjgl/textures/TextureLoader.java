package xoric.prism.scene.lwjgl.textures;

import java.io.BufferedInputStream;
import java.io.ByteArrayInputStream;

import xoric.prism.data.exceptions.PrismException;
import xoric.prism.data.exceptions.UserErrorText;
import xoric.prism.data.meta.AttachmentLoader;

public class TextureLoader
{
	private final AttachmentLoader attachmentLoader;

	public TextureLoader(AttachmentLoader attachmentLoader)
	{
		this.attachmentLoader = attachmentLoader;
	}

	public Texture loadTexture(int index) throws PrismException
	{
		byte[] buf = attachmentLoader.loadAttachment(index);
		ByteArrayInputStream in = new ByteArrayInputStream(buf);
		BufferedInputStream stream = new BufferedInputStream(in);

		Texture texture;
		try
		{
			texture = TextureBinderLWJGL.createFromStream(stream);
		}
		catch (Exception e0)
		{
			PrismException e = new PrismException(e0);
			e.user.setText(UserErrorText.LOCAL_GAME_FILE_CAUSED_PROBLEM);
			e.user.addInfo("file", attachmentLoader.getFile().getName());
			e.code.setText("an IO error occured while trying to load a texture");
			e.code.addInfo("file", attachmentLoader.getFile().toString());
			throw e;
		}
		return texture;
	}

	public int getTextureCount()
	{
		return attachmentLoader.getAttachmentCount();
	}
}
