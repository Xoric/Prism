package xoric.prism.com;

import xoric.prism.data.exceptions.PrismException;
import xoric.prism.data.global.CommonIndex;
import xoric.prism.data.global.FileTableDirectoryIndex;
import xoric.prism.data.global.Prism;
import xoric.prism.data.heap.Heap;
import xoric.prism.data.meta.MetaBlock;
import xoric.prism.data.meta.MetaFile;
import xoric.prism.data.meta.MetaKey;
import xoric.prism.data.meta.MetaLine;
import xoric.prism.data.meta.MetaList;
import xoric.prism.data.meta.MetaType;

public abstract class PrismCommunicationLoader
{
	public static void loadAll() throws PrismException
	{
		loadMessageUrgency();
	}

	private static void loadMessageUrgency() throws PrismException
	{
		MetaFile f = Prism.global.loadMetaFile(FileTableDirectoryIndex.COMMON, CommonIndex.URGENCY.ordinal());
		MetaList metaList = f.getMetaList();
		MetaBlock metaBlock = metaList.claimMetaBlock(MetaType.URGENCY);
		MetaLine ml = metaBlock.claimLine(MetaKey.ITEM);
		ml.ensureMinima(3, 0, 0);
		Heap h = ml.getHeap();

		for (int i = 0; i < 3; ++i)
			MessageDispatcher.thresholds[i] = h.ints.get(i);
	}
}
