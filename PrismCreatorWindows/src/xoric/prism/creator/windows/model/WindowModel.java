package xoric.prism.creator.windows.model;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

import xoric.prism.creator.windows.view.NewWindowData;
import xoric.prism.data.exceptions.PrismException;
import xoric.prism.data.meta.MetaBlock_in;
import xoric.prism.data.meta.MetaBlock_out;
import xoric.prism.data.meta.MetaType;
import xoric.prism.data.types.FloatRect;
import xoric.prism.data.types.IFloatPoint_r;
import xoric.prism.data.types.IPath_r;
import xoric.prism.develop.meta.MetaNames;
import xoric.prism.ui.UIComponent;
import xoric.prism.ui.UIFactory;
import xoric.prism.ui.UIWindow;

public class WindowModel
{
	private final FloatRect screenRect;

	private final IPath_r path;
	private final String filename;
	private final File file;

	private UIWindow window;

	public WindowModel(IPath_r path, String filename, IFloatPoint_r screenSize)
	{
		this.screenRect = new FloatRect(0.0f, 0.0f, screenSize.getX(), screenSize.getY());

		this.path = path;
		this.filename = filename;
		this.file = path.getFile(filename);

		//		window = new UIWindow(screenRect.getSize());
		//		window.rearrange(screenRect);
	}

	public WindowModel(NewWindowData result, IFloatPoint_r screenSize)
	{
		this.screenRect = new FloatRect(0.0f, 0.0f, screenSize.getX(), screenSize.getY());

		path = result.getPath();
		filename = MetaNames.makeMetaBlock("window");
		file = path.getFile(filename);

		window = new UIWindow(screenRect.getSize());
		window.setText(result.getName());
		window.rearrange(screenRect);

		window.setXRuler(-150.0f, 0.5f);
		window.setYRuler(-175.0f, 0.5f);
		window.setWidthRuler(300.0f, 0.0f);
		window.setHeightRuler(350.0f, 0.0f);
	}

	public UIWindow getWindow()
	{
		return window;
	}

	public void load() throws PrismException
	{
		//		window = null;
		MetaBlock_in mb;

		try
		{
			FileInputStream stream = new FileInputStream(file);
			mb = new MetaBlock_in();
			mb.unpack(stream);
			stream.close();

			mb.claimMetaType(MetaType.WINDOW);
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

		window = new UIWindow(null);
		window.load(mb);
		window.rearrange(screenRect);
	}

	public void save() throws PrismException
	{
		try
		{
			MetaBlock_out mb = new MetaBlock_out(MetaType.WINDOW, 0);

			// save window
			mb.addMetaLine(UIFactory.saveComponent(window));

			// save components
			for (UIComponent c : window.getComponents())
				mb.addMetaLine(UIFactory.saveComponent(c));

			FileOutputStream stream = new FileOutputStream(file);
			mb.pack(stream);
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
