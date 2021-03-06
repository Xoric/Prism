package xoric.prism.develop.meta;

import java.io.File;

class MetaListInfo
{
	private final File sourceFile;

	public MetaListInfo(File sourceFile)
	{
		this.sourceFile = sourceFile;
	}

	public boolean wasInfused()
	{
		return sourceFile == null;
	}

	public File getSourceFile()
	{
		return sourceFile;
	}
}
