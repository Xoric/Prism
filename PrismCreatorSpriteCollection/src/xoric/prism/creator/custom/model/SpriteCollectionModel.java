package xoric.prism.creator.custom.model;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import xoric.prism.data.types.IPath_r;

public class SpriteCollectionModel
{
	private final IPath_r path;
	private final List<SpriteModel> models;

	public SpriteCollectionModel(IPath_r path)
	{
		this.path = path;
		this.models = new ArrayList<SpriteModel>();
	}

	public void load()
	{
		models.clear();

		int i = 0;
		boolean b;
		do
		{
			String filename = "sprite." + i + ".png";
			File f = path.getFile(filename);
			b = f.exists();

			if (b)
			{
				SpriteModel m = new SpriteModel(filename);
				models.add(m);
			}
			++i;
		}
		while (b);
	}

	public IPath_r getPath()
	{
		return path;
	}

	public int getCount()
	{
		return models.size();
	}

	public SpriteModel getModel(int index)
	{
		return models.get(index);
	}

	public List<SpriteModel> getModels()
	{
		return models;
	}
}
