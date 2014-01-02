package xoric.prism.world.map.routing;

import java.util.List;

import xoric.prism.data.IPoint_r;
import xoric.prism.data.Point;
import xoric.prism.world.movement.MoveCaps;

/**
 * Interface implemented by all nodes in the network.
 * @author Felix Möhrle
 * @since 29.11.2011, 18:32:22
 */
public interface INode
{
	/**
	 * Returns edges to all directly reachable nodes, may be null if no edges exist.
	 * @return List<Edge> or null
	 */
	public List<Edge> getEdges(MoveCaps caps);

	/**
	 * Calculates and returns the estimated cost (heuristic) from this node to the given target-node.
	 * @param targetNode
	 * @return float
	 */
	public float estimateCost(INode targetNode);

	/**
	 * Returns the node's coordinates needed for cost estimation (e.g. {@link Point}).
	 * @return Point
	 */
	public IPoint_r getCoords();
}
