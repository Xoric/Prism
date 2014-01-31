package xoric.prism.scene.textures;

import java.awt.image.BufferedImage;
import java.io.BufferedInputStream;
import java.io.ByteArrayInputStream;

import javax.imageio.ImageIO;

import xoric.prism.data.exceptions.PrismException;
import xoric.prism.data.exceptions.UserErrorText;
import xoric.prism.data.meta.AttachmentLoader;

public class ImageLoader
{
	private final AttachmentLoader attachmentLoader;

	public ImageLoader(AttachmentLoader attachmentLoader)
	{
		this.attachmentLoader = attachmentLoader;
	}

	public BufferedImage loadImage(int index) throws PrismException
	{
		byte[] buf = attachmentLoader.loadAttachment(index);
		ByteArrayInputStream in = new ByteArrayInputStream(buf);
		BufferedInputStream stream = new BufferedInputStream(in);

		BufferedImage image;
		try
		{
			image = ImageIO.read(stream);
		}
		catch (Exception e0)
		{
			PrismException e = new PrismException(e0);
			e.user.setText(UserErrorText.LOCAL_GAME_FILE_CAUSED_PROBLEM);
			e.user.addInfo("file", attachmentLoader.getFile().getName());
			e.code.setText("an error occured while trying to load an attachment as image");
			e.code.addInfo("file", attachmentLoader.getFile().toString());
			throw e;
		}
		return image;
	}

	public int getImageCount()
	{
		return attachmentLoader.getAttachmentCount();
	}
}
