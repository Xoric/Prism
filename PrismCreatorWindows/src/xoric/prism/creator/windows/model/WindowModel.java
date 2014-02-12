package xoric.prism.creator.windows.model;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

import xoric.prism.client.ui.UIWindow;
import xoric.prism.creator.windows.view.NewWindowData;
import xoric.prism.data.exceptions.PrismException;
import xoric.prism.data.types.FloatRect;
import xoric.prism.data.types.IFloatPoint_r;
import xoric.prism.data.types.IPath_r;

public class WindowModel
{
	private final FloatRect screenRect;

	private final IPath_r path;
	private final String filename;
	private final File file;

	private final UIWindow window;

	public WindowModel(IPath_r path, String filename, IFloatPoint_r screenSize)
	{
		this.screenRect = new FloatRect(0.0f, 0.0f, screenSize.getX(), screenSize.getY());

		this.path = path;
		this.filename = filename;
		this.file = path.getFile(filename);

		window = new UIWindow(screenRect.getSize());
		window.rearrange(screenRect);
	}

	public WindowModel(NewWindowData result, IFloatPoint_r screenSize)
	{
		this.screenRect = new FloatRect(0.0f, 0.0f, screenSize.getX(), screenSize.getY());

		path = result.getPath();
		filename = result.getName().toString().toLowerCase() + ".wn";
		file = path.getFile(filename);

		window = new UIWindow(screenRect.getSize());
		window.rearrange(screenRect);
	}

	public UIWindow getWindow()
	{
		return window;
	}

	public void load() throws PrismException
	{
		FileInputStream stream;
		try
		{
			stream = new FileInputStream(file);
			window.unpack(stream);
			stream.close();
		}
		catch (FileNotFoundException e0)
		{
			PrismException e = new PrismException(e0);
			e.setText("No window could be loaded from the given directory.");
			e.addInfo("missing file", filename);
			throw e;
		}
		catch (IOException e0)
		{
			PrismException e = new PrismException(e0);
			e.setText("There was a problem while trying to load a window from the given destination.");
			e.addInfo("source file", filename);
			throw e;
		}
	}

	public void save() throws PrismException
	{
		try
		{
			FileOutputStream stream = new FileOutputStream(file);
			window.pack(stream);
			stream.close();
		}
		catch (Exception e0)
		{
			PrismException e = new PrismException(e0);
			e.setText("An error occured while trying to save the window.");
			e.addInfo("file", file.toString());
			throw e;
		}
	}
}
