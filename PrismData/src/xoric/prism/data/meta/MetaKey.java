package xoric.prism.data.meta;

public enum MetaKey
{
	// do not resort
	ATTACH,
	TARGET,
	ITEM;

	public byte toByte()
	{
		return (byte) ordinal();
	}
}
