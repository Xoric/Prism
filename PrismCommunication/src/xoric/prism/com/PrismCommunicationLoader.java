package xoric.prism.com;

import xoric.prism.data.exceptions.PrismException;
import xoric.prism.data.global.CommonIndex;
import xoric.prism.data.global.FileTableDirectoryIndex;
import xoric.prism.data.global.Prism;
import xoric.prism.data.heap.Heap_in;
import xoric.prism.data.meta.MetaBlock_in;
import xoric.prism.data.meta.MetaFile;
import xoric.prism.data.meta.MetaKey;
import xoric.prism.data.meta.MetaLine_in;
import xoric.prism.data.meta.MetaList_in;
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
		MetaList_in metaList = f.getMetaList();
		MetaBlock_in metaBlock = metaList.claimMetaBlock(MetaType.URGENCY);
		MetaLine_in ml = metaBlock.claimLine(MetaKey.ITEM);
		ml.ensureMinima(3, 0, 0);
		Heap_in h = ml.getHeap();

		for (int i = 0; i < 3; ++i)
			MessageDispatcher.thresholds[i] = h.ints.get(i);
	}
}
