package xoric.prism.data.meta;

public enum MetaKey
{
	// do not resort
	ATTACH,
	TARGET,
	ITEM,
	SUB,
	ALT;

	public byte toByte()
	{
		return (byte) ordinal();
	}
}
