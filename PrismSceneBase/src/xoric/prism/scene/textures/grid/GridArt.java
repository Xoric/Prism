package xoric.prism.scene.textures.grid;

import xoric.prism.data.exceptions.PrismException;
import xoric.prism.data.meta.MetaFile;
import xoric.prism.data.meta.MetaList_in;
import xoric.prism.scene.textures.Art;

public class GridArt extends Art
{
	private GridMeta meta;

	public GridArt(MetaFile metaFile) throws PrismException
	{
		super(metaFile);
	}

	@Override
	protected void loadMeta(MetaList_in metaList) throws PrismException
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
