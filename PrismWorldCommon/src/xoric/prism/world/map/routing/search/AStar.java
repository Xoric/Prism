package xoric.prism.world.map.routing.search;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import xoric.prism.data.physics.View8;
import xoric.prism.data.types.IPoint_r;
import xoric.prism.world.map.routing.RoutingEdge;
import xoric.prism.world.movement.MoveCaps;

/**
 * Executes an A* Search for a given network with a given start-node and target-node. All nodes implement the {@link IRoutingNode} interface.
 * @author Felix Möhrle
 * @since 29.11.2011, 18:20:54
 */
public class AStar
{
	private final IRoutingNode startNode;
	private final IRoutingNode targetNode;
	private List<MetaRoutingNode> openList; // ordered list, the node with the lowest (estimated) total-cost is at index zero
	private Map<IRoutingNode, IRoutingNode> closedMap; // contains all visited nodes
	private MoveCaps caps;

	public AStar(IRoutingNode startNode, IRoutingNode targetNode, MoveCaps caps)
	{
		this.startNode = startNode;
		this.targetNode = targetNode;
		this.caps = caps;
		this.closedMap = new HashMap<IRoutingNode, IRoutingNode>();
		this.openList = new LinkedList<MetaRoutingNode>();
	}

	/**
	 * Calculates the best route if any exists. Returns a list of nodes starting with the target-node at index zero and the start-node at
	 * the highest index. Returns null if no route exists.
	 * @return List<IPoint_r> or null
	 */
	public List<IPoint_r> calcRoute()
	{
		MetaRoutingNode current = new MetaRoutingNode(startNode, 0.0f, null, targetNode, null);
		boolean success = false;

		do // try to find a route from start-node towards target-node
		{
			if (current.getNode() == targetNode)
			{
				// target-node has been reached
				success = true;
			}
			else
			{
				// expand and close the current node
				expandNode(current);
				closeNode(current);

				// pick next node (the one with minimum estimated cost)
				current = pickNextNode();
			}
		}
		while (current != null && !success);

		// reconstruct route if any
		List<IPoint_r> nodes;
		if (success)
			nodes = compressRoute(current);
		else
			nodes = null;

		// return route
		return nodes;
	}

	/**
	 * Removes and returns the node with the lowest estimated cost from the open-list.
	 * @return MetaNode
	 */
	private MetaRoutingNode pickNextNode()
	{
		MetaRoutingNode node = openList.isEmpty() ? null : openList.remove(0);
		return node;
	}

	/**
	 * Reconstructs and compresses the route starting at the target-node and going back towards the start-node. Filters out redundant nodes.
	 * @param target
	 *            target-node
	 * @return List<IPoint_r>
	 */
	private List<IPoint_r> compressRoute(MetaRoutingNode target)
	{
		// walk backwards and drop unnecessary nodes in the process
		List<MetaRoutingNode> list = new ArrayList<MetaRoutingNode>();
		list.add(target);

		MetaRoutingNode node = target;
		View8 angle = target.getTransitionAngle();

		MetaRoutingNode predecessor;
		View8 nextAngle;

		do
		{
			predecessor = node.getPredecessor();
			if (predecessor != null)
			{
				nextAngle = predecessor.getTransitionAngle();

				if (nextAngle != angle)
				{
					list.add(predecessor);
					angle = nextAngle;
				}
			}
			node = predecessor;
		}
		while (predecessor != null);

		// create route
		List<IPoint_r> nodes = new ArrayList<IPoint_r>();
		int count = list.size();

		for (int i = 0; i < count; ++i)
		{
			MetaRoutingNode n = list.get(i);
			final int g = 1;
			if (i < count - g)
				angle = list.get(i + g).getTransitionAngle();
			else
				angle = null;

			// angle = list.get(i).getTransitionAngle();

			nodes.add(n.getNode().getCoords());
		}
		return nodes;
	}

	/**
	 * Marks a node as closed.
	 * @param metaNode
	 */
	private void closeNode(MetaRoutingNode metaNode)
	{
		closedMap.put(metaNode.getNode(), null);
	}

	/**
	 * Expands the given node by adding all its successors to the open-list. Successors that have already been visited and closed are
	 * ignored and are not being visited again.
	 * @param metaNode
	 */
	private void expandNode(MetaRoutingNode metaNode)
	{
		List<RoutingEdge> edges = metaNode.getNode().getEdges(caps);
		if (edges != null)
		{
			for (RoutingEdge e : edges)
			{
				// handle the successor-node reached by the current edge
				IRoutingNode successorNode = e.getTargetNode();

				if (closedMap.containsKey(successorNode))
					; // do nothing (this successor has already been visited)
				else
				{
					// calculate tentative cost for the current successor-node when using this route
					float cost = metaNode.getTentativeCost() + e.getTransitionCost();

					// update the open-list if it already contains the successor-node
					RoutingUpdateState state = updateOpenList(successorNode, metaNode, cost, e);
					if (state == RoutingUpdateState.FOUND_AND_DISCARDED)
						; // do nothing (node is already in the open-list and previous route is cheaper)
					else
					{
						if (state == RoutingUpdateState.NOT_FOUND)
						{
							// add this node to the open-list
							MetaRoutingNode successorMeta = new MetaRoutingNode(successorNode, cost, metaNode, targetNode, e.getTransitionAngle());
							addToOpenList(successorMeta);
						}
					}
				}
			}
		}
	}

	/**
	 * Marks the given node as open.
	 * @param metaNode
	 */
	private void addToOpenList(MetaRoutingNode metaNode)
	{
		float cost = metaNode.getTotalCost();

		for (int i = 0; i < openList.size(); ++i)
		{
			if (cost < openList.get(i).getTotalCost())
			{
				openList.add(i, metaNode);
				return;
			}
		}
		openList.add(metaNode);
	}

	/**
	 * Updates the given node in the open-list if the new route is cheaper. Returns NOT_FOUND if this node is not yet in the open-list.
	 * Returns FOUND_AND_UPDATED if this node was found and updated with the new cheaper route. Returns FOUND_AND_DISCARDED if this node was
	 * found but the previous route is cheaper.
	 * @param node
	 * @param newPredecessor
	 * @param newCost
	 * @return {@link RoutingUpdateState} (NOT_FOUND, FOUND_AND_UPDATED or FOUND_AND_DISCARDED)
	 */
	private RoutingUpdateState updateOpenList(IRoutingNode node, MetaRoutingNode newPredecessor, float newCost, RoutingEdge edge)
	{
		for (MetaRoutingNode m : openList)
		{
			if (m.getNode() == node)
			{
				// update cost and predecessor if the new route to this node is cheaper
				boolean updated = m.update(newPredecessor, newCost, edge);
				if (updated)
				{
					openList.remove(m);
					addToOpenList(m); // re-insert this node after updating its cost
					return RoutingUpdateState.FOUND_AND_UPDATED;
				}
				else
					return RoutingUpdateState.FOUND_AND_DISCARDED;
			}
		}
		return RoutingUpdateState.NOT_FOUND; // the given node is not yet in the open-list
	}
}
