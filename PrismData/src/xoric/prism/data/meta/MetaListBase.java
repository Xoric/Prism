package xoric.prism.data.meta;


abstract class MetaListBase
{
	protected abstract MetaBlockBase getMetaBlock(int index);

	protected abstract int getBlockCount();

	public abstract void dropMetaBlock(MetaBlockBase b);

	@Override
	public String toString()
	{
		StringBuilder sb = new StringBuilder("[");

		for (int i = 0; i < getBlockCount(); ++i)
		{
			MetaBlockBase b = getMetaBlock(i);
			if (i > 0)
				sb.append(", ");

			sb.append(b.getMetaType().toString());
		}
		sb.append("]");

		return sb.toString();
	}
}
