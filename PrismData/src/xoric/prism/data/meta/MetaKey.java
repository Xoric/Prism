package xoric.prism.data.meta;

public enum MetaKey
{
	// do not resort
	ATTACH,
	TARGET,
	ITEM,
	SUB,
	ALT,
	COUNT,
	SIZE;

	private static final MetaKey[] VALUES = values();

	public static MetaKey valueOf(int index)
	{
		return VALUES[index];
	}
}
