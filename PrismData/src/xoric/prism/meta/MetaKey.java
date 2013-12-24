package xoric.prism.meta;

public enum MetaKey
{
	// do not resort
	COMMON,
	ATTACH,
	TARGET;

	public byte toByte()
	{
		return (byte) ordinal();
	}
}
