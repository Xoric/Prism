package xoric.prism.world.map.routing;

import java.util.ArrayList;
import java.util.List;

import xoric.prism.data.IFloatPoint_r;
import xoric.prism.data.IPoint_r;

/**
 * @author XoricLee
 * @since 02.01.2012, 13:29:52
 */
public class Route
{
	private List<IPoint_r> nodes;
	private IFloatPoint_r finalPos;

	public Route(IFloatPoint_r finalPos)
	{
		this.finalPos = finalPos;
		this.nodes = new ArrayList<IPoint_r>();
	}

	/**
	 * Adds a node to the route.
	 * @param coords
	 */
	public void addNode(IPoint_r coords)
	{
		nodes.add(coords);
	}
}
