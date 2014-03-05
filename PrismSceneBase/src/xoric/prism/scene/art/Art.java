package xoric.prism.scene.art;

import java.awt.image.BufferedImage;

import xoric.prism.data.exceptions.PrismException;
import xoric.prism.data.exceptions.UserErrorText;
import xoric.prism.data.meta.MetaFile;
import xoric.prism.data.meta.MetaList_in;

public abstract class Art
{
	public static ITextureBinder textureBinder;

	private final MetaFile metaFile;
	private final ImageLoader imageLoader;
	private final ITexture[] textures;

	public Art(MetaFile metaFile) throws PrismException
	{
		this.metaFile = metaFile;
		this.imageLoader = new ImageLoader(metaFile.getAttachmentLoader());

		final int n = imageLoader.getImageCount();
		if (n == 0)
		{
			PrismException e = new PrismException();
			e.user.setText(UserErrorText.LOCAL_GAME_FILE_CAUSED_PROBLEM);
			e.code.setText("no attachments found");
			metaFile.addExceptionInfoTo(e);
			throw e;
		}
		textures = new ITexture[n];
	}

	public final void load() throws PrismException
	{
		loadMeta(metaFile.getMetaList());
	}

	protected abstract void loadMeta(MetaList_in metaList) throws PrismException;

	public abstract ArtMeta getMeta();

	public ITexture getTexture(int index) throws PrismException
	{
		if (textures[index] == null)
			textures[index] = loadAndBindTexture(index);

		return textures[index];
	}

	private ITexture loadAndBindTexture(int index) throws PrismException
	{
		BufferedImage image = imageLoader.loadImage(index);
		ITexture texture;
		try
		{
			texture = textureBinder.bindTexture(image);
		}
		catch (Exception e0)
		{
			PrismException e = new PrismException(e0);
			e.user.setText(UserErrorText.LOCAL_GAME_FILE_CAUSED_PROBLEM);
			e.code.setText("error while binding texture");
			metaFile.addExceptionInfoTo(e);
			throw e;
		}
		return texture;
	}
}
