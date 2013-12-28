package xoric.prism.world.map.routing;

import xoric.prism.world.movement.View8;

/**
 * Encapsulates a node in the network with additional meta information needed for the A* search.
 * @author Felix M�hrle
 * @since 29.11.2011, 19:10:16
 */
public class MetaNode
{
	private final INode node;
	private MetaNode predecessor;
	private float tentativeCost;
	private float estimatedCost;
	private float totalCost;
	private View8 transitionAngle;

	/**
	 * MetaNode constructor.
	 * @param thisNode
	 *            the node encapsulated by this MetaNode
	 * @param tentativeCost
	 *            total costs from start-node until here
	 * @param predecessor
	 *            node previous to this one on the current route
	 * @param targetNode
	 *            target-node (needed for cost estimation)
	 */
	public MetaNode(INode thisNode, float tentativeCost, MetaNode predecessor, INode targetNode, View8 transitionAngle)
	{
		this.node = thisNode;
		this.tentativeCost = tentativeCost;
		this.predecessor = predecessor;
		this.estimatedCost = thisNode.estimateCost(targetNode);
		this.transitionAngle = transitionAngle;
		updateTotalCost();
	}

	private void updateTotalCost()
	{
		totalCost = tentativeCost + estimatedCost;
	}

	public MetaNode getPredecessor()
	{
		return predecessor;
	}

	/**
	 * Returns the estimated cost (heuristic) from this node to the target-node.
	 * @return float
	 */
	public float getEstimatedCost()
	{
		return estimatedCost;
	}

	/**
	 * Returns the sum of the tentative-cost (cost from start-node until here) plus the estimated costs to the target-node (heuristic).
	 * @return float
	 */
	public float getTotalCost()
	{
		return totalCost;
	}

	/**
	 * Returns the cost from start-node until here.
	 * @return float
	 */
	public float getTentativeCost()
	{
		return tentativeCost;
	}

	/**
	 * Returns the node encapsulated by this MetaNode object.
	 * @return INode
	 */
	public INode getNode()
	{
		return node;
	}

	/**
	 * Returns the angle within the route from the predecessor of this node to itself.
	 * @return View8
	 */
	public View8 getTransitionAngle()
	{
		return transitionAngle;
	}

	/**
	 * Updates the predecessor-node and tentative-cost if cheaper.
	 * @param newPredecessor
	 * @param newTentativeCost
	 *            costs from start-node to this node when using the given (new) predecessor
	 * @return Returns true if the update was carried out, false otherwise
	 */
	public boolean update(MetaNode newPredecessor, float newTentativeCost, Edge edge)
	{
		boolean updated;

		if (newTentativeCost < this.tentativeCost)
		{
			this.tentativeCost = newTentativeCost;
			this.predecessor = newPredecessor;
			this.transitionAngle = edge.getTransitionAngle();
			updateTotalCost();
			updated = true;
		}
		else
			updated = false;

		return updated;
	}
}
