package xoric.prism.world.map.routing;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;

import xoric.prism.data.packable.IPackable;
import xoric.prism.data.packable.IntPacker;
import xoric.prism.data.types.IPoint_r;
import xoric.prism.world.map.Ground;
import xoric.prism.world.map.routing.search.IRoutingNode;
import xoric.prism.world.movement.MoveCaps;

@Deprecated
public class RoutingTile implements IRoutingNode, IPackable
{
	public static final int WIDTH = 120;
	public static final int HEIGHT = 80;

	protected Ground ground;
	protected List<RoutingEdge> edges; // may be null
	protected IPoint_r coords;

	public RoutingTile(IPoint_r coords, Ground ground)
	{
		this.coords = coords;
		this.ground = ground;
	}

	public void setIndex(int index)
	{
		this.ground = null; // AllGrounds.list.get(index); // (disabled)
	}

	public Ground getGround()
	{
		return ground;
	}

	/**
	 * Checks the target-node of the given edge for accessibility and adds it if valid. Returns true if the edge was added.
	 * @param edge
	 * @return boolean
	 */
	public boolean addEdge(RoutingEdge edge)
	{
		boolean isValid = edge.getMoveCaps().anyIsTrue();
		if (isValid)
		{
			if (edges == null)
				edges = new ArrayList<RoutingEdge>();
			edges.add(edge);
		}
		return isValid;
	}

	/**
	 * Removes all edges.
	 */
	public void clearEdges()
	{
		edges = null;
	}

	@Override
	public List<RoutingEdge> getEdges(MoveCaps caps)
	{
		List<RoutingEdge> edges;

		if (this.edges != null)
		{
			edges = new ArrayList<RoutingEdge>();
			for (RoutingEdge e : this.edges)
				if (e.getMoveCaps().isAccessibleFor(caps))
					edges.add(e);
		}
		else
			edges = null;

		return edges;
	}

	@Override
	public float estimateCost(IRoutingNode targetNode)
	{
		float cost = coords.calcDistance(targetNode.getCoords());
		return cost;
	}

	@Override
	public IPoint_r getCoords()
	{
		return coords;
	}

	@Override
	public void pack(OutputStream stream) throws IOException
	{
		IntPacker.pack_s(stream, ground.getIndex());
	}

	@Override
	public void unpack(InputStream stream) throws IOException
	{
		int index = IntPacker.unpack_s(stream);
		setIndex(index);
	}
}
