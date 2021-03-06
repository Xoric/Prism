package xoric.prism.data.global;

import xoric.prism.data.exceptions.PrismException;
import xoric.prism.data.heap.Heap_in;
import xoric.prism.data.meta.MetaFile;
import xoric.prism.data.types.IPath_r;

public interface IPrismGlobal
{
	public MetaFile loadMetaFile(FileTableDirectoryIndex fd, int index) throws PrismException;

	public IPath_r getDataPath();

	public Heap_in getVersionHeap();
}
