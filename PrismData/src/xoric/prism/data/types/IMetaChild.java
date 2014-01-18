package xoric.prism.data.types;

import xoric.prism.data.exceptions.PrismException;
import xoric.prism.data.meta.MetaList;

public interface IMetaChild
{
	public void load(MetaList metaList) throws PrismException;
}
