package xoric.prism.global;

import java.util.ArrayList;
import java.util.List;

import xoric.prism.data.exceptions.PrismException;
import xoric.prism.data.exceptions.UserErrorText;
import xoric.prism.data.global.FileTableDirectoryIndex;
import xoric.prism.data.heap.Heap_in;
import xoric.prism.data.meta.MetaBlock_in;
import xoric.prism.data.meta.MetaFile;
import xoric.prism.data.meta.MetaKey;
import xoric.prism.data.meta.MetaLine_in;
import xoric.prism.data.meta.MetaList_in;
import xoric.prism.data.meta.MetaType;
import xoric.prism.data.types.IPath_r;

public class FileTable
{
	public static final int TOC = 0;
	public static final int ANIM_DESCRIPTIONS = 1;

	private final IPath_r dataPath;
	private final List<FileTableDirectory> list;
	private Heap_in versionHeap;

	public FileTable(IPath_r dataPath)
	{
		this.dataPath = dataPath;
		this.list = new ArrayList<FileTableDirectory>();
	}

	public void load(String filename) throws PrismException
	{
		list.clear();

		MetaFile f = new MetaFile(dataPath, filename);
		f.load();
		MetaList_in metaList = f.getMetaList();
		MetaBlock_in metaBlock = metaList.claimMetaBlock(MetaType.TOC);

		FileTableDirectory currentDir = null;

		for (MetaLine_in l : metaBlock.getMetaLines())
		{
			currentDir = interpretLine(currentDir, l);
		}
	}

	public void createVersionHeap() throws PrismException
	{
		versionHeap = new Heap_in();

		for (FileTableDirectory d : list)
		{
			for (FileTableEntry e : d.getEntries())
			{
				MetaFile f = e.loadMetaFile();
				int v = f.getLocalFileVersion();
				versionHeap.ints.add(v);
			}
		}
	}

	public Heap_in getVersionHeap()
	{
		return versionHeap;
	}

	public FileTableDirectory interpretLine(FileTableDirectory currentDir, MetaLine_in metaLine) throws PrismException
	{
		MetaKey key = metaLine.getKey();

		if (key == MetaKey.ITEM)
		{
			currentDir = loadDirectoryLine(metaLine);
		}
		else if (key == MetaKey.SUB)
		{
			loadFileLine(currentDir, metaLine);
		}
		return currentDir;
	}

	private FileTableDirectory loadDirectoryLine(MetaLine_in metaLine) throws PrismException
	{
		metaLine.ensureMinima(0, 0, 1);
		Heap_in h = metaLine.getHeap();
		String dir = h.texts.get(0).toString().toLowerCase();
		FileTableDirectory d = new FileTableDirectory(dir);
		list.add(d);

		return d;
	}

	private void loadFileLine(FileTableDirectory currentDir, MetaLine_in metaLine) throws PrismException
	{
		if (currentDir == null)
		{
			PrismException e = new PrismException();
			// ----
			e.user.setText(UserErrorText.LOCAL_GAME_FILE_CAUSED_PROBLEM);
			// ----
			e.code.setText("a file was added before a directory was specified");
			// ----
			metaLine.addExceptionInfoTo(e);
			// ----
			throw e;
		}

		metaLine.ensureMinima(0, 0, 1);
		Heap_in h = metaLine.getHeap();
		String s = h.texts.get(0).toString().toLowerCase();

		String filename = currentDir.getPath().getFile(s).toString().replace('\\', '/');
		FileTableEntry e = new FileTableEntry(dataPath, filename);
		currentDir.addEntry(e);
	}

	public MetaFile loadMetaFile(FileTableDirectoryIndex fd, int index) throws PrismException
	{
		int dirIndex = fd.ordinal();

		if (dirIndex >= list.size())
		{
			PrismException e = new PrismException();
			// ----
			e.user.setText(UserErrorText.INTERNAL_PROBLEM);
			// ----
			e.code.setText("a non-existing file was requested from FileTable");
			e.code.addInfo("FileDirectory", fd.toString() + " (" + fd.ordinal() + ")");
			e.code.addInfo("index", index);
			// ----
			// ----
			throw e;
		}

		FileTableDirectory directory = list.get(dirIndex);
		FileTableEntry entry = directory.get(index);
		MetaFile metaFile = entry.loadMetaFile();

		return metaFile;
	}
}
