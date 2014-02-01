package xoric.prism.scene.textures.grid;

import xoric.prism.data.exceptions.PrismException;
import xoric.prism.data.meta.MetaFile;
import xoric.prism.data.meta.MetaList;
import xoric.prism.scene.textures.Art;

public class GridArt extends Art
{
	private GridMeta meta;

	public GridArt(MetaFile metaFile) throws PrismException
	{
		super(metaFile);
	}

	@Override
	protected void loadMeta(MetaList metaList) throws PrismException
	{
		meta = new GridMeta();
		meta.load(metaList);
	}

	@Override
	public GridMeta getMeta()
	{
		return meta;
	}
}
