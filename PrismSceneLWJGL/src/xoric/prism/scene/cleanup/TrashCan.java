package xoric.prism.scene.cleanup;

import java.util.ArrayList;
import java.util.List;

import xoric.prism.data.exceptions.PrismException;
import xoric.prism.scene.lwjgl.ICleanUp;

/**
 * @author XoricLee
 * @since 25.02.2014, 12:23:25
 */
public abstract class TrashCan //implements ITrashCan
{
	private static final List<ICleanUp> list = new ArrayList<ICleanUp>();

	//	public TrashCan()
	//	{
	//		list = new ArrayList<ICleanUp>();
	//	}

	@Override
	public String toString()
	{
		return list.size() + " object(s)";
	}

	// ITrashCan:
	public static void addResource(ICleanUp c)
	{
		if (!list.contains(c))
			list.add(c);
	}

	// ICleanUp:
	//	@Override
	public static void cleanUp()
	{
		for (ICleanUp c : list)
		{
			try
			{
				c.cleanUp();
				System.out.println("cleaned up " + c);
			}
			catch (PrismException e)
			{
				e.code.print();
				e.user.showMessage();
			}
			catch (Exception e)
			{
				System.err.println("error while cleaning up " + c + ": " + e.toString());
			}
		}
		list.clear();
	}
}
