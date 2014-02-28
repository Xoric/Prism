package xoric.prism.world.map;

import java.util.ArrayList;
import java.util.List;

import xoric.prism.data.exceptions.PrismException;
import xoric.prism.data.exceptions.UserErrorText;
import xoric.prism.data.meta.MetaBlock_in;
import xoric.prism.data.meta.MetaFile;
import xoric.prism.data.meta.MetaKey;
import xoric.prism.data.meta.MetaLine_in;
import xoric.prism.data.meta.MetaType;
import xoric.prism.world.map2.GroundType2;

public class AllGrounds
{
	public static final GroundType2 GROUND0 = new GroundType2(0);

	public static final List<GroundType2> list = new ArrayList<GroundType2>();

	public static void loadAll(MetaFile mf) throws PrismException
	{
		list.clear();
		list.add(GROUND0);

		MetaBlock_in mb = mf.getMetaList().claimMetaBlock(MetaType.GROUND);
		int i = 1;

		for (MetaLine_in ml : mb.getMetaLines())
		{
			ml.assumeMetaKey(MetaKey.ITEM);
			GroundType2 g = new GroundType2(i++);
			ml.getHeap().rewind();
			g.extractFrom(ml.getHeap());
			list.add(g);
		}
	}

	public static GroundType2 getGroundType(int index) throws PrismException
	{
		if (index < 0 || index >= list.size())
		{
			PrismException e = new PrismException();
			// ----
			e.user.setText(UserErrorText.INTERNAL_PROBLEM);
			// ----
			e.code.setText("invalid index (" + index + ") for " + GroundType2.class.getSimpleName());
			e.code.addInfo("index", index);
			e.code.addInfo("count", list.size());
			// ----
			throw e;
		}
		return list.get(index);
	}
}
