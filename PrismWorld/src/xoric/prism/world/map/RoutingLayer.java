package xoric.prism.world.map;

import xoric.prism.data.Point;
import xoric.prism.data.physics.View8;
import xoric.prism.world.map.routing.Edge;
import xoric.prism.world.movement.MoveCaps;

public class RoutingLayer
{
	private static final float COST_DIRECT = 1.0f;
	private static final float COST_DIAGONAL = 1.4142135623730950488016887242097f;

	private Node[][] nodes;

	public Node getNode(int x, int y)
	{
		return nodes[y][x];
	}

	public void init(Tile[][] tiles)
	{
		int tileCountX = nodes[0].length;
		int tileCountY = nodes.length;
		nodes = new Node[tileCountY][tileCountX];

		// create nodes
		for (int iy = 0; iy < nodes.length; ++iy)
		{
			for (int ix = 0; ix < nodes[iy].length; ++ix)
			{
				Node n = new Node(new Point(ix, iy), tiles[iy][ix].getGround().getMoveCaps());
				nodes[iy][ix] = n;
			}
		}

		// link nodes
		boolean left, top, right, bottom;
		int lastX = tileCountX - 1;
		int lastY = tileCountY - 1;

		for (int iy = 0; iy < tileCountY; ++iy)
		{
			for (int ix = 0; ix < tileCountX; ++ix)
			{
				Node thisNode = nodes[iy][ix];
				thisNode.clearEdges();

				// left, top, right, bottom
				left = ix > 0 ? addEdge(thisNode, nodes[iy][ix - 1], COST_DIRECT, View8.LEFT) : false;
				top = iy > 0 ? addEdge(thisNode, nodes[iy - 1][ix], COST_DIRECT, View8.TOP) : false;
				right = ix < lastX ? addEdge(thisNode, nodes[iy][ix + 1], COST_DIRECT, View8.RIGHT) : false;
				bottom = iy < lastY ? addEdge(thisNode, nodes[iy + 1][ix], COST_DIRECT, View8.BOTTOM) : false;

				if (top) // diagonal top
				{
					if (left) // top-left
						addEdge(thisNode, nodes[iy - 1][ix - 1], COST_DIAGONAL, View8.TOP_LEFT, nodes[iy - 1][ix]/*crossing top*/,
								nodes[iy][ix - 1]/*crossing left*/);
					if (right) // top-right
						addEdge(thisNode, nodes[iy - 1][ix + 1], COST_DIAGONAL, View8.TOP_RIGHT, nodes[iy - 1][ix]/*crossing top*/,
								nodes[iy][ix + 1]/*crossing right*/);
				}

				if (bottom) // diagonal bottom
				{
					if (left) // bottom-left
						addEdge(thisNode, nodes[iy + 1][ix - 1], COST_DIAGONAL, View8.BOTTOM_LEFT, nodes[iy + 1][ix]/*crossing bottom*/,
								nodes[iy][ix - 1]/*crossing left*/);
					if (right) // bottom-right
						addEdge(thisNode, nodes[iy + 1][ix + 1], COST_DIAGONAL, View8.BOTTOM_RIGHT, nodes[iy + 1][ix]/*crossing bottom*/,
								nodes[iy][ix + 1]/*crossing right*/);
				}
			}
		}
	}

	/**
	 * Checks if the given edge is accessible for at least one MoveType and adds the edge to the source-node if so. Returns true if the edge
	 * was added.
	 * @param sourceNode
	 * @param targetNode
	 * @param cost
	 * @param crossedNodes
	 *            Nodes that are being crossed (when walking diagonally), may be none
	 * @return boolean
	 */
	private static boolean addEdge(Node sourceNode, Node targetNode, float cost, View8 transitionAngle, Node... crossedNodes)
	{
		// calculate the effective MoveCaps for this edge
		// consider crossed nodes' MoveCaps as well
		MoveCaps caps = targetNode.getMoveCaps();
		if (crossedNodes.length > 0)
		{
			caps = new MoveCaps(caps.toInt()); // copy MoveCaps object
			for (Node n : crossedNodes)
				caps.bitwiseAnd(n.getMoveCaps());
		}

		// check if any MoveType is available on this edge
		boolean isValid = caps.anyIsTrue();

		if (isValid) // add this edge if it is accessible for at least one MoveType
		{
			Edge edge = new Edge(targetNode, caps, cost, transitionAngle);
			isValid = sourceNode.addEdge(edge);
		}
		return isValid;
	}
}
