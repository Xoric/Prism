package xoric.prism.creator.windows.view;

import xoric.prism.creator.common.view.INewDialogResult;
import xoric.prism.data.types.Path;
import xoric.prism.data.types.Text;

public class NewWindowData implements INewDialogResult
{
	private final Text name;
	private final Path path;

	public NewWindowData(Text name, Path path)
	{
		this.name = name;
		this.path = path;
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
}
