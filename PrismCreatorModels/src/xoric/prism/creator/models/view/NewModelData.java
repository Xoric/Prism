package xoric.prism.creator.models.view;

import xoric.prism.creator.common.view.INewDialogResult;
import xoric.prism.data.types.Path;
import xoric.prism.data.types.Point;
import xoric.prism.data.types.Text;

public class NewModelData implements INewDialogResult
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

	@Override
	public Path getPath()
	{
		return path;
	}

	public Point getTileSize()
	{
		return tileSize;
	}
}
