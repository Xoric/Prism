package xoric.prism.develop.meta;

import xoric.prism.data.meta.MetaType;

public abstract class MetaNames
{
	@Deprecated
	public static String makeMetaBlock(String filename)
	{
		return filename.toLowerCase() + ".block";
	}

	public static String makeMetaBlock(MetaType t)
	{
		return t.toString().toLowerCase() + ".block";
	}
}
