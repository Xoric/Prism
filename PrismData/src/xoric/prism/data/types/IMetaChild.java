package xoric.prism.data.types;

import xoric.prism.data.exceptions.PrismException;
import xoric.prism.data.meta.MetaList_in;

public interface IMetaChild
{
	public void load(MetaList_in metaList) throws PrismException;
}
