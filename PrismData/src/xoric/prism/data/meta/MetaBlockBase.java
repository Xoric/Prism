package xoric.prism.data.meta;

abstract class MetaBlockBase
{
	protected MetaType metaType;
	protected int version;

	protected MetaBlockBase()
	{
	}

	protected MetaBlockBase(MetaType metaType, int version)
	{
		this.metaType = metaType;
		this.version = version;
	}

	public abstract int getLineCount();

	@Override
	public String toString()
	{
		return getLineCount() + " line(s)";
	}

	public MetaType getMetaType()
	{
		return metaType;
	}

	public int getVersion()
	{
		return version;
	}
}
