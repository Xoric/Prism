package xoric.prism.data.global;

import xoric.prism.data.exceptions2.PrismException2;
import xoric.prism.data.meta.MetaFile;
import xoric.prism.data.types.IPath_r;

public interface IPrismGlobal
{
	public MetaFile loadMetaFile(FileIndex fi) throws PrismException2;

	public IPath_r getDataPath();
}
