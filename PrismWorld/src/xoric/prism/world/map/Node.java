package xoric.prism.world.map;

import java.util.ArrayList;
import java.util.List;

import xoric.prism.data.IPoint_r;
import xoric.prism.world.map.routing.Edge;
import xoric.prism.world.map.routing.INode;
import xoric.prism.world.movement.MoveCaps;

public class Node implements INode
{
	private List<Edge> edges; // may be null
	private IPoint_r coords;
	private MoveCaps moveCaps;

	public Node(IPoint_r coords, MoveCaps moveCaps)
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
	public boolean addEdge(Edge edge)
	{
		boolean isValid = edge.getMoveCaps().anyIsTrue();
		if (isValid)
		{
			if (edges == null)
				edges = new ArrayList<Edge>();
			edges.add(edge);
		}
		return isValid;
	}

	public void clearEdges()
	{
		edges.clear();
	}

	@Override
	public List<Edge> getEdges(MoveCaps caps)
	{
		List<Edge> edges;

		if (this.edges != null)
		{
			edges = new ArrayList<Edge>();
			for (Edge e : this.edges)
				if (e.getMoveCaps().isAccessibleFor(caps))
					edges.add(e);
		}
		else
			edges = null;

		return edges;
	}

	@Override
	public float estimateCost(INode targetNode)
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