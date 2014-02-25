package xoric.prism.world.map.routing.search;

import java.util.ArrayList;
import java.util.List;

import xoric.prism.data.types.IPoint_r;
import xoric.prism.world.map.routing.RoutingEdge;
import xoric.prism.world.movement.MoveCaps;

public class RoutingNode implements IRoutingNode
{
	private List<RoutingEdge> edges; // may be null
	private IPoint_r coords;
	private MoveCaps moveCaps;

	public RoutingNode(IPoint_r coords, MoveCaps moveCaps)
	{
		this.coords = coords;
		this.moveCaps = moveCaps;
	}

	public MoveCaps getMoveCaps()
	{
		return moveCaps;
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

	public void clearEdges()
	{
		edges.clear();
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
		// TODO cheaper calculation possible?
		float cost = coords.calcDistance(targetNode.getCoords());
		return cost;
	}

	@Override
	public IPoint_r getCoords()
	{
		return coords;
	}
}
