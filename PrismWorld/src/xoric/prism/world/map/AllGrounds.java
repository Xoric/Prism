package xoric.prism.world.map;

import java.util.ArrayList;
import java.util.List;

public class AllGrounds
{
	public static final Ground GROUND0 = new Ground(0);

	public static final List<Ground> list = new ArrayList<Ground>();

	public static void load()
	{
		list.clear();
		list.add(GROUND0);
	}
}
