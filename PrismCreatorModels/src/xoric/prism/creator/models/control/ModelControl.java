package xoric.prism.creator.models.control;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.JOptionPane;

import xoric.prism.creator.common.tools.ExternalImageEditor;
import xoric.prism.creator.models.generators.AnimationsGenerator;
import xoric.prism.creator.models.generators.ModelFilter;
import xoric.prism.creator.models.generators.ModelGenerator;
import xoric.prism.creator.models.model.ModelModel;
import xoric.prism.creator.models.view.NewModelData;
import xoric.prism.data.exceptions.PrismException;
import xoric.prism.data.types.IPath_r;
import xoric.prism.data.types.IPoint_r;
import xoric.prism.data.types.IText_r;
import xoric.prism.data.types.Path;
import xoric.prism.data.types.Point;
import xoric.prism.swing.input.OpenPathDialog;
import xoric.prism.swing.input.PointInput;
import xoric.prism.swing.input.PrismFileDialog;

public class ModelControl extends ControlLayer
{
	public ModelControl(ModelModel model, IBusyControl busyControl)
	{
		super(model, busyControl);
	}

	public boolean saveModel()
	{
		boolean isOK;
		busyControl.setBusy(true);

		try
		{
			model.save();
			isOK = true;
		}
		catch (PrismException e)
		{
			e.user.showMessage();
			isOK = false;
		}

		busyControl.setBusy(false);

		return isOK;
	}

	public ModelModel createNewModel(NewModelData data)
	{
		ModelModel newModel = null;

		busyControl.setBusy(true);

		try
		{
			newModel = new ModelModel(data);
			newModel.preparePath();
		}
		catch (PrismException e)
		{
			newModel = null;
			e.user.showMessage();
		}

		busyControl.setBusy(false);

		return newModel;
	}

	public ModelModel openModel()
	{
		OpenPathDialog d = new OpenPathDialog("Open Model", "Please enter the working directory of the model you want to open.");
		boolean b = d.showOpenPathDialog();
		Path path = b ? d.getResult() : null;

		ModelModel openedModel = null;

		if (path != null)
			openedModel = openModel(path);
		else
			openedModel = null;

		return openedModel;
	}

	public ModelModel openModel(IPath_r path)
	{
		ModelModel openedModel = null;

		File f = path.getFile(ModelModel.mainFilename);

		if (!f.exists())
		{
			PrismException e = new PrismException();
			e.setText("No model could be found in the specified directory.");
			e.addInfo("missing file", ModelModel.mainFilename);
			e.user.showMessage();
		}
		else
		{
			busyControl.setBusy(true);

			openedModel = new ModelModel();
			try
			{
				openedModel.load(path);
			}
			catch (IOException e0)
			{
				openedModel = null;

				PrismException e = new PrismException(e0);
				e.setText("An error occured while trying to load a model from the specified directory.");
				e.user.showMessage();
			}
			catch (PrismException e)
			{
				openedModel = null;

				e.user.showMessage();
			}
			busyControl.setBusy(false);
		}
		return openedModel;
	}

	public void setName(IText_r name)
	{
		boolean b = !model.getName().equals(name);
		if (b)
		{
			model.setName(name);
			saveModel();
		}
	}

	public boolean askChangeSpriteSize(IPoint_r spriteSize)
	{
		StringBuilder sb = new StringBuilder();
		sb.append("<html><p width=\"360\">");
		sb.append("You are about to change the sprite size of the current model. ");
		sb.append("This means that all existing sprites will be resized. ");
		sb.append("<br><br>");

		sb.append("Additional horizontal space (if any) will be appended at the left and right margin equally. ");
		sb.append("Extra vertical space will be appended on top of the images. ");
		sb.append("If the size should decrease in any direction the images will get cropped in these areas. ");
		sb.append("It is therefore recommended to create a backup first.");

		int dx = spriteSize.getX() - model.getSpriteSize().getX();
		int dy = spriteSize.getY() - model.getSpriteSize().getY();

		if (dx != 0 || dy != 0)
		{
			sb.append("<br><br>Effects:<br><code>");

			if (dx != 0)
			{
				sb.append(dx > 0 ? "increase" : "decrease");
				sb.append(" width by " + Math.abs(dx) + " pixel(s)<br>");
			}
			if (dy != 0)
			{
				sb.append(dy > 0 ? "increase" : "decrease");
				sb.append(" height by " + Math.abs(dy) + " pixel(s)<br>");
			}

			sb.append("</code>");
		}
		sb.append("</p></html>");

		String[] options = { "Resize", "Cancel" };
		int n = JOptionPane.showOptionDialog(null, sb.toString(), "Resize sprites", JOptionPane.DEFAULT_OPTION,
				JOptionPane.INFORMATION_MESSAGE, null, options, options[0]);

		return n == 0;
	}

	public void setSpriteSize(IPoint_r spriteSize)
	{
		model.setSpriteSize(spriteSize);
		saveModel();
	}

	public void generateAnimations(ModelModel model)
	{
		AnimationsGenerator g = new AnimationsGenerator(model);
		g.generateAll();
	}

	public void exportModel(ModelModel model)
	{
		String targetFilename = model.getName().toString().toLowerCase() + ModelFilter.dotExtension;
		ModelGenerator g = new ModelGenerator(model);
		g.generateModel(targetFilename);
	}

	public void createNewPortrait(ModelModel model)
	{
		Point size = PointInput.showInputMessage("Create portrait", "Please enter the desired size for the portrait.", "Width", "Height",
				100, 80);

		if (size != null && size.getX() > 0 && size.getY() > 0)
		{
			File portraitFile = model.getPath().getFile("portrait.png");
			try
			{
				BufferedImage bi = new BufferedImage(size.getX(), size.getY(), BufferedImage.TYPE_INT_RGB);
				ImageIO.write(bi, "png", portraitFile);
			}
			catch (Exception e0)
			{
				PrismException e = new PrismException(e0);
				e.setText("An error occured while trying to create a portrait.");
				e.addInfo("portrait file", portraitFile.toString());
				e.user.showMessage();
			}
		}
	}

	public void importPortrait(ModelModel model)
	{
		PrismFileDialog p = new PrismFileDialog("Import Portrait", "Select an image that you want to use as portrait.");
		boolean b = p.showOpenDialog("Import");

		if (b)
		{
			File importFile = p.getResult();
			File portraitFile = model.getPath().getFile("portrait.png");
			try
			{
				BufferedImage bi = ImageIO.read(importFile);
				ImageIO.write(bi, "png", portraitFile);
			}
			catch (Exception e0)
			{
				PrismException e = new PrismException(e0);
				e.setText("An error occured while trying to import a portrait.");
				e.addInfo("source file", importFile.toString());
				e.addInfo("target file", portraitFile.toString());
				e.user.showMessage();
			}
		}
	}

	public void editPortrait(ModelModel model)
	{
		File portraitFile = model.getPath().getFile("portrait.png");
		ExternalImageEditor.getInstance().execute(portraitFile);
	}

	public void deletePortrait(ModelModel model)
	{
		File portraitFile = model.getPath().getFile("portrait.png");
		if (portraitFile.exists())
		{
			String[] options = { "Delete", "Cancel" };
			int n = JOptionPane.showOptionDialog(null, "Please confirm deleting this model's portrait.", "Delete portrait",
					JOptionPane.DEFAULT_OPTION, JOptionPane.QUESTION_MESSAGE, null, options, options[0]);

			if (n == 0)
			{
				try
				{
					boolean b = portraitFile.delete();
					if (!b)
					{
						PrismException e = new PrismException();
						e.setText("Could not delete the model's portrait.");
						e.addInfo("portrait file", portraitFile.toString());
						e.user.showMessage();
					}
				}
				catch (Exception e0)
				{
					PrismException e = new PrismException(e0);
					e.setText("An error occured while trying to delete the model's portrait.");
					e.addInfo("portrait file", portraitFile.toString());
					e.user.showMessage();
				}
			}
		}
	}
}
