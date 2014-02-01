package xoric.prism.creator.grid.view;

import xoric.prism.creator.common.view.INewDialogResult;
import xoric.prism.data.types.IPoint_r;
import xoric.prism.data.types.Path;
import xoric.prism.data.types.Text;

public class NewGridData implements INewDialogResult
{
	private final Text name;
	private final IPoint_r spriteSize;
	private final Path path;

	public NewGridData(Text name, IPoint_r spriteSize, Path path)
	{
		this.name = name;
		this.spriteSize = spriteSize;
		this.path = path;
	}

	public Text getName()
	{
		return name;
	}

	public IPoint_r getSpriteSize()
	{
		return spriteSize;
	}

	@Override
	public Path getPath()
	{
		return path;
	}
}
