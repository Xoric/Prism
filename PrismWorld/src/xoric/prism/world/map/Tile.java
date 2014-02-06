package xoric.prism.world.map;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;

import xoric.prism.data.packable.IPackable;
import xoric.prism.data.packable.IntPacker;
import xoric.prism.data.types.IPoint_r;
import xoric.prism.world.map.routing.Edge;
import xoric.prism.world.map.routing.INode;
import xoric.prism.world.movement.MoveCaps;

public class Tile implements INode, IPackable
{
	public static final int WIDTH = 160;
	public static final int HEIGHT = 120;

	private Ground ground;
	protected List<Edge> edges; // may be null
	protected IPoint_r coords;

	public Tile(IPoint_r coords, Ground ground)
	{
		this.coords = coords;
		this.ground = ground;
	}

	public void setIndex(int index)
	{
		this.ground = AllGrounds.list.get(index);
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

	/**
	 * Removes all edges.
	 */
	public void clearEdges()
	{
		edges = null;
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

	//	@Override
	//	public int getPackedSize()
	//	{
	//		// TODO Auto-generated method stub
	//		return 0;
	//	}
}
