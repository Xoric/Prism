package xoric.prism.scene.textures.collections;

import xoric.prism.data.exceptions.PrismException;
import xoric.prism.data.meta.MetaFile;
import xoric.prism.data.meta.MetaList;
import xoric.prism.data.types.IFloatRect_r;
import xoric.prism.scene.textures.Art;
import xoric.prism.scene.textures.ITexture;
import xoric.prism.scene.textures.TextureInfo;

public class CollectionArt extends Art
{
	private CollectionMeta meta;

	public CollectionArt(MetaFile metaFile) throws PrismException
	{
		super(metaFile);
	}

	@Override
	public void loadMeta(MetaList metaList) throws PrismException
	{
		meta = new CollectionMeta();
		meta.load(metaList);
	}

	@Override
	public CollectionMeta getMeta()
	{
		return meta;
	}

	public TextureInfo getTextureInfo(int textureIndex, int objectIndex, int objectInstance, int rectIndex) throws PrismException
	{
		ITexture texture = getTexture(textureIndex);

		ObjectMeta om = meta.getObject(objectIndex);
		ObjectInstance oi = om.getInstance(objectInstance);
		IFloatRect_r rect = oi.getRect(rectIndex);

		TextureInfo texInfo = new TextureInfo(texture, rect, false);

		return texInfo;
	}
}
