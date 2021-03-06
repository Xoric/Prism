package xoric.prism.creator.models.control;

import java.io.File;
import java.util.List;

import javax.swing.JOptionPane;

import xoric.prism.creator.common.view.INewDialogResult;
import xoric.prism.creator.models.model.AnimationModel;
import xoric.prism.creator.models.model.ModelModel;
import xoric.prism.creator.models.model.VariationList;
import xoric.prism.creator.models.view.IMainView;
import xoric.prism.creator.models.view.NewModelData;
import xoric.prism.data.types.IPath_r;
import xoric.prism.data.types.IPoint_r;
import xoric.prism.data.types.IText_r;
import xoric.prism.world.animations.AnimationIndex;
import xoric.prism.world.entities.ViewAngle;

public class MainControl implements IMainControl, IBusyControl
{
	private IMainView view;
	private ModelModel model;

	private ModelControl modelControl;
	private AnimationControl animationControl;
	private SpriteControl spriteControl;

	public MainControl(IMainView view)
	{
		this.view = view;

		modelControl = new ModelControl(model, this);
		animationControl = new AnimationControl(model, this);
		spriteControl = new SpriteControl(model, this);

		acceptModel(null, true);
	}

	@Override
	public void setBusy(boolean b)
	{
		view.setHourglass(b);
	}

	private void acceptModel(ModelModel m, boolean acceptNull)
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
	public void requestCreateNewProject(INewDialogResult result)
	{
		NewModelData d = (NewModelData) result;
		ModelModel m = modelControl.createNewModel(d);
		acceptModel(m, false);
	}

	@Override
	public void requestOpenProject(IPath_r path)
	{
		ModelModel m = modelControl.openModel(path);
		acceptModel(m, false);
	}

	@Override
	public void requestCloseProject()
	{
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
	public void requestCreateAnimations()
	{
		modelControl.generateAnimations(model);
	}

	@Override
	public void requestCreateModel()
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
		modelControl.editPortrait(model);
		view.displayPortrait(model.getPath());
	}

	@Override
	public void requestDeletePortrait()
	{
		modelControl.deletePortrait(model);
		view.displayPortrait(model.getPath());
	}

	@Override
	public void requestExit()
	{
		System.exit(0);
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
		JOptionPane.showConfirmDialog(null, "not yet implemented");
		// TODO 
		// implement

		//		view.displayAnimation(animation, false);
	}

	@Override
	public void requestSetAnimationDuration(AnimationIndex a, int variation, int ms)
	{
		// TODO: move to ModelControl?
		animationControl.setDuration(a, variation, ms);
		view.displayCurrentAnimationDuration();
		modelControl.saveModel();
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
		spriteControl.editSprite(file);
	}

	@Override
	public void requestMakeSpritesTransparent(AnimationIndex a, int variation, ViewAngle v, List<Integer> indices)
	{
		spriteControl.makeSpritesTransparent(a, variation, v, indices);

		// reload animation sprites
		view.reloadCurrentAnimationFrames();
	}
}
