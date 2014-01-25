package xoric.prism.creator.drawer.control;

import java.io.File;
import java.util.List;

import xoric.prism.creator.common.WorkingDirs;
import xoric.prism.creator.drawer.model.AnimationModel;
import xoric.prism.creator.drawer.model.DrawerModel;
import xoric.prism.creator.drawer.model.VariationList;
import xoric.prism.creator.drawer.view.IDrawerView;
import xoric.prism.data.types.IPath_r;
import xoric.prism.data.types.IPoint_r;
import xoric.prism.data.types.IText_r;
import xoric.prism.world.animations.AnimationIndex;
import xoric.prism.world.entities.ViewAngle;

public class DrawerControl implements IDrawerControl, IBusyControl
{
	private IDrawerView view;
	private DrawerModel model;

	private final ExternalImageEditor externalEditor;
	private final WorkingDirs workingDirs;

	private ModelControl modelControl;
	private AnimationControl animationControl;
	private SpriteControl spriteControl;

	public DrawerControl(IDrawerView view)
	{
		this.view = view;
		//		this.model = new DrawerModel();

		modelControl = new ModelControl(model, this);
		animationControl = new AnimationControl(model, this);
		spriteControl = new SpriteControl(model, this);

		externalEditor = new ExternalImageEditor();
		workingDirs = new WorkingDirs("models");

		acceptModel(null, true);
	}

	@Override
	public void setBusy(boolean b)
	{
		view.setHourglass(b);
	}

	private void acceptModel(DrawerModel m, boolean acceptNull)
	{
		if (m != null || acceptNull)
		{
			model = m;

			view.setModel(m);
			modelControl.setModel(m);
			animationControl.setModel(m);
			spriteControl.setModel(m);

			view.displayAll(m);
		}
	}

	/* *********** model control ****************** */

	@Override
	public void requestNewModel()
	{
		DrawerModel m = modelControl.createNewModel();
		acceptModel(m, false);

		if (m != null)
		{
			workingDirs.addWorkingDirectory(m.getPath());
			view.displayRecentDirectories(workingDirs);
		}
	}

	@Override
	public void requestOpenModel()
	{
		DrawerModel m = modelControl.openModel();
		acceptModel(m, false);

		if (m != null)
		{
			workingDirs.addWorkingDirectory(m.getPath());
			view.displayRecentDirectories(workingDirs);
		}
	}

	@Override
	public void requestOpenRecent(IPath_r path)
	{
		DrawerModel m = modelControl.openModel(path);
		acceptModel(m, false);

		if (m != null)
		{
			workingDirs.addWorkingDirectory(m.getPath());
			view.displayRecentDirectories(workingDirs);
		}
	}

	@Override
	public void requestCloseModel()
	{
		if (modelControl.closeModel())
			acceptModel(null, true);
	}

	@Override
	public void requestSetName(IText_r name)
	{
		modelControl.setName(name);
		view.displayName(name);
	}

	@Override
	public void requestResizeSprites(IPoint_r spriteSize)
	{
		boolean b = modelControl.askChangeSpriteSize(spriteSize);

		if (b)
		{
			// apply changes to model
			modelControl.setSpriteSize(spriteSize);

			// resize sprites
			spriteControl.resizeAllSprites(model.getPath(), spriteSize);

			// update displays
			view.displaySpriteSize(spriteSize);
			view.reloadCurrentAnimationFrames();
		}
	}

	@Override
	public void requestGenerateAnimations()
	{
		modelControl.generateAnimations(model);
	}

	@Override
	public void requestExportModel()
	{
		modelControl.exportModel(model);
	}

	@Override
	public void requestCreateNewPortrait()
	{
		modelControl.createNewPortrait(model);
		view.displayPortrait(model.getPath());
	}

	@Override
	public void requestImportPortrait()
	{
		modelControl.importPortrait(model);
		view.displayPortrait(model.getPath());
	}

	@Override
	public void requestEditPortrait()
	{
		modelControl.editPortrait(model, externalEditor);
		view.displayPortrait(model.getPath());
	}

	@Override
	public void requestDeletePortrait()
	{
		modelControl.deletePortrait(model);
		view.displayPortrait(model.getPath());
	}

	/* *********** animation control ****************** */

	@Override
	public void requestAddAnimation(AnimationIndex a)
	{
		VariationList l = model.getAnimation(a);
		l.unlock();
		view.displayAnimationInList(l);
	}

	@Override
	public void requestDeleteAnimation(AnimationIndex a, int variation)
	{
		System.out.println("requestDeleteAnimation(" + a + ", variation=" + variation + ")");

		//		view.displayAnimation(animation, false);
	}

	@Override
	public void requestSetAnimationDuration(AnimationIndex a, int variation, int ms)
	{
		// TODO: move to ModelControl?
		animationControl.setDuration(a, variation, ms);
		view.displayCurrentAnimationDuration();
		modelControl.saveModel(true);
	}

	/* *********** sprite control ****************** */

	@Override
	public void requestCloneSprite(AnimationIndex a, int variation, ViewAngle v, int index)
	{
		// add a sprite
		spriteControl.cloneSprite(a, variation, v, index);

		// reload animation list and sprites
		VariationList l = model.getAnimation(a);
		AnimationModel m = l.getVariation(variation);
		m.loadSpriteCount();
		view.displayAnimationInList(l);
		view.reloadCurrentAnimationFrames();
	}

	private void reloadAnimation(AnimationIndex a, int variation)
	{
		VariationList l = model.getAnimation(a);
		AnimationModel m = l.getVariation(variation);
		m.loadSpriteCount();
		view.displayAnimationInList(l);
		view.reloadCurrentAnimationFrames();
	}

	@Override
	public void requestInsertSprite(AnimationIndex a, int variation, ViewAngle v, int index)
	{
		// add a sprite
		spriteControl.insertSprite(a, variation, v, index);

		// reload animation list and sprites
		reloadAnimation(a, variation);
	}

	@Override
	public void requestInsertSpriteFromClipboard(AnimationIndex a, int variation, ViewAngle v, int index)
	{
		// insert sprite
		spriteControl.insertSpriteFromClipboard(a, variation, v, index);

		// reload animation list and sprites
		reloadAnimation(a, variation);
	}

	@Override
	public void requestCopySpriteToClipboard(AnimationIndex a, int variation, ViewAngle v, int index)
	{
		// copy sprite image to clipboard
		spriteControl.copySpriteToClipboard(model.getPath(), a, variation, v, index);
	}

	@Override
	public void requestDeleteSprites(AnimationIndex a, int variation, ViewAngle v, List<Integer> indices)
	{
		// delete sprites
		spriteControl.deleteSprites(a, variation, v, indices);

		// reload animation list and sprites
		reloadAnimation(a, variation);
	}

	@Override
	public void requestEditSprite(File file)
	{
		spriteControl.editSprite(file, externalEditor);
	}

	@Override
	public void requestMakeSpritesTransparent(AnimationIndex a, int variation, ViewAngle v, List<Integer> indices)
	{
		spriteControl.makeSpritesTransparent(a, variation, v, indices);

		// reload animation sprites
		view.reloadCurrentAnimationFrames();
	}

	@Override
	public void requestInputExternalImageEditor()
	{
		externalEditor.showInput();
	}

	@Override
	public void initialize()
	{
		workingDirs.load();
		view.displayRecentDirectories(workingDirs);
	}
}
