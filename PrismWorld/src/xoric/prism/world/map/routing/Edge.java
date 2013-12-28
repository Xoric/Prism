package xoric.prism.world.map.routing;

import xoric.prism.world.movement.MoveCaps;
import xoric.prism.world.movement.View8;

/**
 * Represents the transition from one node to another considering the transition-costs.
 * @author Felix M�hrle
 * @since 29.11.2011, 19:07:16
 */
public class Edge
{
	private final INode targetNode;
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
	public Edge(INode targetNode, MoveCaps caps, float transitionCost, View8 transitionAngle)
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
	public INode getTargetNode()
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
