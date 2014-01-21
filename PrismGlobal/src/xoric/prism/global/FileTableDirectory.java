package xoric.prism.global;

import java.util.ArrayList;
import java.util.List;

import xoric.prism.data.types.IPath_r;
import xoric.prism.data.types.Path;

public class FileTableDirectory
{
	private final String name;
	private final IPath_r path;
	private final List<FileTableEntry> list;

	public FileTableDirectory(String name)
	{
		this.name = name;
		this.path = new Path(name);
		this.list = new ArrayList<FileTableEntry>();
	}

	public IPath_r getPath()
	{
		return path;
	}

	@Override
	public String toString()
	{
		return name;
	}

	public void addEntry(FileTableEntry e)
	{
		list.add(e);
	}

	public List<FileTableEntry> getEntries()
	{
		return list;
	}

	public FileTableEntry get(int index)
	{
		return list.get(index);
	}
}
