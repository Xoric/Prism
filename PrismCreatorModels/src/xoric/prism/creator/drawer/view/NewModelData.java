package xoric.prism.creator.drawer.view;

import xoric.prism.data.types.Path;
import xoric.prism.data.types.Point;
import xoric.prism.data.types.Text;

public class NewModelData
{
	private final Text name;
	private final Path path;
	private final Point tileSize;

	public NewModelData(Text name, Path path, Point tileSize)
	{
		this.name = name;
		this.path = path;
		this.tileSize = tileSize;
	}

	public Text getName()
	{
		return name;
	}

	public Path getPath()
	{
		return path;
	}

	public Point getTileSize()
	{
		return tileSize;
	}
}
