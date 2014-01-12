package xoric.prism.creator.drawer.control;

import java.io.File;
import java.util.List;

import xoric.prism.creator.drawer.model.AnimationModel;
import xoric.prism.creator.drawer.model.DrawerModel;
import xoric.prism.creator.drawer.view.IDrawerView2;
import xoric.prism.data.types.IPoint_r;
import xoric.prism.data.types.IText_r;
import xoric.prism.world.entities.AnimationIndex;
import xoric.prism.world.entities.ViewAngle;

public class DrawerControl implements IDrawerControl, IBusyControl
{
	private IDrawerView2 view;
	private DrawerModel model;

	private ModelControl modelControl;
	private AnimationControl animationControl;
	private SpriteControl spriteControl;

	public DrawerControl(IDrawerView2 view)
	{
		this.view = view;
		//		this.model = new DrawerModel();

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
		acceptModel(modelControl.createNewModel(), false);
	}

	@Override
	public void requestOpenModel()
	{
		acceptModel(modelControl.openModel(), false);
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
	public void requestSetTileSize(IPoint_r tileSize)
	{
		model.setTileSize(tileSize);
		view.displayTileSize(tileSize);
		// TODO: every single png has to be changed
	}

	/* *********** animation control ****************** */

	@Override
	public void requestAddAnimation(AnimationIndex a)
	{
		AnimationModel m = model.getAnimation(a);
		m.unlock();
		view.displayAnimationInList(m);
	}

	@Override
	public void requestDeleteAnimation(AnimationIndex a)
	{
		System.out.println("requestDeleteAnimation(" + a + ")");

		//		view.displayAnimation(animation, false);
	}

	/* *********** sprite control ****************** */

	@Override
	public void requestAddSprite(AnimationIndex a, ViewAngle v, int index)
	{
		// add a sprite
		spriteControl.addSprite(a, v, index);

		// reload animation list and sprites
		AnimationModel m = model.getAnimation(a);
		m.load();
		view.displayAnimationInList(m);
		view.updateCurrentAnimation();
	}

	@Override
	public void requestDeleteSprites(AnimationIndex a, ViewAngle v, List<Integer> indices)
	{
		// delete sprites
		spriteControl.deleteSprites(a, v, indices);

		// reload animation list and sprites
		AnimationModel m = model.getAnimation(a);
		m.load();
		view.displayAnimationInList(m);
		view.updateCurrentAnimation();
	}

	@Override
	public void requestEditSprite(File file)
	{
		spriteControl.editSprite(file);
	}

	@Override
	public void requestInputExternalImageEditor()
	{
		spriteControl.inputExternalImageEditor();
	}
}
