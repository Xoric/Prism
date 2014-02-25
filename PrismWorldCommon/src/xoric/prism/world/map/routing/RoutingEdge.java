package xoric.prism.world.map.routing;

import xoric.prism.data.physics.View8;
import xoric.prism.world.map.routing.search.IRoutingNode;
import xoric.prism.world.movement.MoveCaps;

/**
 * Represents the transition from one node to another considering the transition-costs.
 * @author Felix Möhrle
 * @since 29.11.2011, 19:07:16
 */
public class RoutingEdge
{
	private final IRoutingNode targetNode;
	private final MoveCaps caps;
	private final float transitionCost;
	private final View8 transitionAngle;

	/**
	 * Edge constructor.
	 * @param targetNode
	 *            node reached by this edge
	 * @param caps
	 *            MoveCaps for this edge
	 * @param transitionCost
	 *            costs when traveling from this node to the target-node
	 */
	public RoutingEdge(IRoutingNode targetNode, MoveCaps caps, float transitionCost, View8 transitionAngle)
	{
		this.targetNode = targetNode;
		this.caps = caps;
		this.transitionCost = transitionCost;
		this.transitionAngle = transitionAngle;
	}

	/**
	 * Returns the target-node of this edge.
	 * @return INode
	 */
	public IRoutingNode getTargetNode()
	{
		return targetNode;
	}

	public MoveCaps getMoveCaps()
	{
		return caps;
	}

	public View8 getTransitionAngle()
	{
		return transitionAngle;
	}

	/**
	 * Returns the cost for using this transition.
	 * @return float
	 */
	public float getTransitionCost()
	{
		return transitionCost;
	}
}
