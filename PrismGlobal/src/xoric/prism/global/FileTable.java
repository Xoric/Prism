package xoric.prism.global;

import java.util.ArrayList;
import java.util.List;

import xoric.prism.data.exceptions.PrismException;
import xoric.prism.data.exceptions.UserErrorText;
import xoric.prism.data.global.FileIndex;
import xoric.prism.data.meta.MetaBlock;
import xoric.prism.data.meta.MetaFile;
import xoric.prism.data.meta.MetaKey;
import xoric.prism.data.meta.MetaLine;
import xoric.prism.data.meta.MetaList;
import xoric.prism.data.meta.MetaType;
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
				PrismException e = new PrismException();
				// ----
				e.user.setText(UserErrorText.LOCAL_GAME_FILE_CAUSED_PROBLEM);
				// ----
				e.code.setText("an invalid MetaLine was found while loading file table");
				e.code.addInfo("metaBlock", MetaType.TOC.toString());
				// ----
				e.addInfo("file", filename);
				// ----
				throw e;
			}
		}
	}

	public MetaFile loadMetaFile(FileIndex fi) throws PrismException
	{
		int index = fi.ordinal();

		if (index >= list.size())
		{
			PrismException e = new PrismException();
			// ----
			e.user.setText(UserErrorText.INTERNAL_PROBLEM);
			// ----
			e.code.setText("a non-existing file was requested from FileTable");
			e.code.addInfo("fileIndex", fi.toString() + " (" + fi.ordinal() + ")");
			e.code.addInfo("fileCount", list.size());
			// ----
			// ----
			throw e;
		}

		FileTableEntry e = list.get(index);
		MetaFile metaFile = e.loadMetaFile();

		return metaFile;
	}
}
