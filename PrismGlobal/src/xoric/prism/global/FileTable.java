package xoric.prism.global;

import java.util.ArrayList;
import java.util.List;

import xoric.prism.data.exceptions.PrismException;
import xoric.prism.data.exceptions.PrismMetaFileException;
import xoric.prism.data.global.FileIndex;
import xoric.prism.data.meta.MetaBlock;
import xoric.prism.data.meta.MetaFile;
import xoric.prism.data.meta.MetaKey;
import xoric.prism.data.meta.MetaLine;
import xoric.prism.data.meta.MetaList;
import xoric.prism.data.meta.MetaType;
import xoric.prism.data.modules.ActorID;
import xoric.prism.data.modules.ErrorCode;
import xoric.prism.data.modules.ErrorID;
import xoric.prism.data.types.Path;

public class FileTable
{
	public static final int TOC = 0;
	public static final int ANIM_DESCRIPTIONS = 1;

	private final Path dataPath;
	private final List<FileTableEntry> list;

	public FileTable(Path dataPath)
	{
		this.dataPath = dataPath;
		this.list = new ArrayList<FileTableEntry>();
	}

	public void load(String filename) throws PrismException
	{
		MetaFile f = new MetaFile(dataPath, filename);
		f.load();
		MetaList metaList = f.getMetaList();
		MetaBlock metaBlock = metaList.findMetaBlock(MetaType.TOC);

		for (MetaLine l : metaBlock.getMetaLines())
		{
			if (l.getKey() == MetaKey.ITEM && l.getHeap().texts.size() == 1)
			{
				FileTableEntry e = new FileTableEntry(dataPath, l.getHeap().texts.get(0).toString().toLowerCase());
				list.add(e);
			}
			else
			{
				ErrorCode c = new ErrorCode(ActorID.GENERIC, ErrorID.CORRUPT_LINE);
				PrismMetaFileException e = new PrismMetaFileException(c, filename);
				e.appendInfo("line", l.toString());
				throw e;
			}
		}
	}

	public MetaFile loadMetaFile(FileIndex fi) throws PrismException
	{
		int index = fi.ordinal();

		if (index >= list.size())
		{
			ErrorCode c = new ErrorCode(ActorID.GENERIC, ErrorID.INVALID_INDEX);
			PrismException e = new PrismException(c);
			e.appendInfo("index", String.valueOf(index) + " (" + fi.toString() + ")");
			e.appendInfo("max", String.valueOf(list.size()));
			throw e;
		}

		FileTableEntry e = list.get(index);
		MetaFile metaFile = e.loadMetaFile();

		return metaFile;
	}
}