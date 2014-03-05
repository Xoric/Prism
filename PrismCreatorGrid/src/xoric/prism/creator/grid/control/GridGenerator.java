package xoric.prism.creator.grid.control;

import java.io.File;
import java.io.FileInputStream;

import xoric.prism.creator.common.factory.SuccessMessage;
import xoric.prism.creator.grid.model.GridModel;
import xoric.prism.data.exceptions.PrismException;
import xoric.prism.data.meta.MetaBlock_in;
import xoric.prism.data.meta.MetaBlock_out;
import xoric.prism.data.meta.MetaKey;
import xoric.prism.data.meta.MetaLine_out;
import xoric.prism.data.meta.MetaList_in;
import xoric.prism.data.meta.MetaList_out;
import xoric.prism.data.meta.MetaType;
import xoric.prism.data.types.IPath_r;
import xoric.prism.data.types.Text;
import xoric.prism.develop.meta.MetaFileCreator;

public class GridGenerator
{
	private final GridModel model;

	public GridGenerator(GridModel model)
	{
		this.model = model;
	}

	public void create()
	{
		try
		{
			// generate model and show success message
			String targetFilename = model.getName().toString().toLowerCase() + ".gd";
			File targetFile = tryGenerate(targetFilename);
			showSuccess(targetFile);
		}
		catch (PrismException e)
		{
			e.code.print();
			e.user.showMessage();
		}
	}

	private File tryGenerate(String targetFilename) throws PrismException
	{
		// create dev MetaBlock
		MetaBlock_out devBlock = new MetaBlock_out(MetaType.DEVELOP, 0);

		// load collection MetaBlock
		MetaBlock_out gridBlock = loadTextureBlock();

		// make sure texture is there
		ensureTexture();

		// add target file
		MetaLine_out targetLine = new MetaLine_out(MetaKey.TARGET);
		targetLine.getHeap().texts.add(new Text(targetFilename));
		devBlock.addMetaLine(targetLine);

		// add texture
		MetaLine_out attachLine = new MetaLine_out(MetaKey.ATTACH);
		attachLine.getHeap().texts.add(new Text("TEXTURE.PNG"));
		devBlock.addMetaLine(attachLine);

		// create MetaList
		MetaList_out metaList_out = new MetaList_out();
		metaList_out.addMetaBlock(gridBlock);
		metaList_out.addMetaBlock(devBlock);

		// convert MetaList to _in
		MetaList_in metaList_in = new MetaList_in(metaList_out);

		// pass information to MetaFileCreator
		IPath_r path = model.getPath();
		MetaFileCreator c = new MetaFileCreator(path, path);
		c.infuseMetaList(metaList_in);
		c.create(false);

		// get target file
		File targetFile = c.getResultingTargetFile();
		return targetFile;
	}

	private MetaBlock_out loadTextureBlock() throws PrismException
	{
		File file = model.getPath().getFile("texture.meta");
		MetaBlock_in gridBlock;
		try
		{
			gridBlock = new MetaBlock_in(MetaType.GRID, 0);
			FileInputStream stream = new FileInputStream(file);
			gridBlock.unpack(stream);
			stream.close();
		}
		catch (Exception e0)
		{
			PrismException e = new PrismException(e0);
			e.setText("There was an error loading information about the previous generated texture.");
			e.addInfo("file", file.toString());
			throw e;
		}
		return new MetaBlock_out(gridBlock);
	}

	private void ensureTexture() throws PrismException
	{
		File file = model.getPath().getFile("texture.png");
		if (!file.exists())
		{
			PrismException e = new PrismException();
			e.setText("No texture could be found. Generate a texture first.");
			e.addInfo("missing file", file.toString());
			throw e;
		}
	}

	private void showSuccess(File targetFile)
	{
		SuccessMessage m = new SuccessMessage("grid");
		m.addFile(targetFile);
		m.addIcon(model.getPath().getFile("texture.png"));
		m.showMessage();
	}
}
