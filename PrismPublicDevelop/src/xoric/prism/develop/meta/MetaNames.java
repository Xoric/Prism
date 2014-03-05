package xoric.prism.develop.meta;

import xoric.prism.data.meta.MetaType;
import xoric.prism.data.types.IText_r;

public abstract class MetaNames
{
	public static final String projectCollection = "collection.proj";
	public static final String projectGrid = "grid.proj";

	@Deprecated
	public static String makeMetaBlock(String filename)
	{
		return filename.toLowerCase() + ".block";
	}

	public static String makeMetaBlock(MetaType t)
	{
		return t.toString().toLowerCase() + ".block";
	}

	public static String makeCollection(IText_r name)
	{
		return name.toString().toLowerCase() + ".cn";
	}

	public static String makeGrid(IText_r name)
	{
		return name.toString().toLowerCase() + ".gd";
	}
}
