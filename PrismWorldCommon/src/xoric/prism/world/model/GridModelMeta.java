package xoric.prism.world.model;

import java.util.ArrayList;
import java.util.List;

import xoric.prism.data.exceptions.PrismException;
import xoric.prism.data.exceptions.UserErrorText;
import xoric.prism.data.heap.Heap_in;
import xoric.prism.data.meta.MetaBlock_in;
import xoric.prism.data.meta.MetaKey;
import xoric.prism.data.meta.MetaLine_in;
import xoric.prism.data.meta.MetaList_in;
import xoric.prism.data.meta.MetaType;
import xoric.prism.data.types.IMetaChild_in;
import xoric.prism.data.types.IPoint_r;
import xoric.prism.data.types.Point;
import xoric.prism.world.animations.AnimationIndex;
import xoric.prism.world.entities.ViewAngle;

public class GridModelMeta implements IMetaChild_in
{
	private final List<AnimationMetaCollection> animationCollections;
	private IPoint_r spriteSize;

	public GridModelMeta()
	{
		animationCollections = new ArrayList<AnimationMetaCollection>();
	}

	@Override
	public void load(MetaList_in metaList) throws PrismException
	{
		// reset
		spriteSize = null;

		// load animations
		MetaBlock_in mb = metaList.claimMetaBlock(MetaType.MODEL_G);
		AnimationMeta currentAnimationMeta = null;

		for (MetaLine_in l : mb.getMetaLines())
		{
			currentAnimationMeta = interpretLine(currentAnimationMeta, l);
		}

		// ensure meta is valid
		ensureSpriteSize(mb);
		ensureAnimations(mb);
	}

	private AnimationMeta interpretLine(AnimationMeta currentAnimationMeta, MetaLine_in metaLine) throws PrismException
	{
		MetaKey key = metaLine.getKey();

		if (key == MetaKey.ITEM)
		{
			// add new animation
			currentAnimationMeta = addAnimation(metaLine);
		}
		else if (key == MetaKey.SUB)
		{
			// add ViewAngle to current AnimationCollection
			addViewAngle(currentAnimationMeta, metaLine);
		}
		else if (key == MetaKey.SIZE)
		{
			// extract sprite size
			extractSpriteSize(metaLine);
		}
		return currentAnimationMeta;
	}

	private AnimationMeta addAnimation(MetaLine_in metaLine) throws PrismException
	{
		AnimationMeta animationMeta = null;
		metaLine.ensureMinima(1, 0, 0);
		Heap_in h = metaLine.getHeap();
		int ai = h.ints.get(0);
		AnimationIndex animationIndex;

		try
		{
			animationIndex = AnimationIndex.valueOf(ai);
		}
		catch (Exception e0)
		{
			PrismException e = new PrismException(e0);
			// ----
			e.user.setText(UserErrorText.LOCAL_GAME_FILE_CAUSED_PROBLEM);
			// ----
			e.code.setText("error casting int (" + ai + ") to AnimationIndex");
			// ----
			metaLine.addExceptionInfoTo(e);
			// ----
			throw e;
		}

		AnimationMetaCollection currentCollection = findOrAddCollection(animationIndex);
		animationMeta = new AnimationMeta();
		currentCollection.addVariation(animationMeta);

		return animationMeta;
	}

	private void addViewAngle(AnimationMeta currentAnimationMeta, MetaLine_in metaLine) throws PrismException
	{
		if (currentAnimationMeta == null)
		{
			PrismException e = new PrismException();
			// ----
			e.user.setText(UserErrorText.LOCAL_GAME_FILE_CAUSED_PROBLEM);
			// ----
			e.code.setText("a ViewAngle was added before an animation was specified");
			// ----
			metaLine.addExceptionInfoTo(e);
			// ----
			throw e;
		}

		metaLine.ensureMinima(2, 0, 0);
		Heap_in h = metaLine.getHeap();
		int vi = h.ints.get(0);
		ViewAngle viewAngle;

		try
		{
			viewAngle = ViewAngle.valueOf(vi);
		}
		catch (Exception e0)
		{
			PrismException e = new PrismException(e0);
			// ----
			e.user.setText(UserErrorText.LOCAL_GAME_FILE_CAUSED_PROBLEM);
			// ----
			e.code.setText("error casting int (" + vi + ") to ViewAngle");
			// ----
			metaLine.addExceptionInfoTo(e);
			// ----
			throw e;
		}

		int columnCount = h.ints.get(1);
		currentAnimationMeta.registerNextViewAngle(viewAngle, columnCount);
	}

	private void extractSpriteSize(MetaLine_in metaLine) throws PrismException
	{
		metaLine.ensureMinima(2, 0, 0);
		Heap_in h = metaLine.getHeap();
		int x = h.ints.get(0);
		int y = h.ints.get(1);
		spriteSize = new Point(x, y);
	}

	private void ensureSpriteSize(MetaBlock_in mb) throws PrismException
	{
		if (spriteSize == null)
		{
			PrismException e = new PrismException();
			// ----
			e.user.setText(UserErrorText.LOCAL_GAME_FILE_CAUSED_PROBLEM);
			// ----
			e.code.setText("sprite size is missing");
			// ----
			mb.addExceptionInfoTo(e);
			// ----
			throw e;
		}
		else if (spriteSize.getX() <= 0 || spriteSize.getY() <= 0)
		{
			PrismException e = new PrismException();
			// ----
			e.user.setText(UserErrorText.LOCAL_GAME_FILE_CAUSED_PROBLEM);
			// ----
			e.code.setText("invalid sprite size " + spriteSize.toString());
			// ----
			mb.addExceptionInfoTo(e);
			// ----
			throw e;
		}
	}

	private void ensureAnimations(MetaBlock_in mb) throws PrismException
	{
		if (animationCollections.size() == 0)
		{
			PrismException e = new PrismException();
			// ----
			e.user.setText(UserErrorText.LOCAL_GAME_FILE_CAUSED_PROBLEM);
			// ----
			e.code.setText("did not find any animations");
			// ----
			mb.addExceptionInfoTo(e);
			// ----
			throw e;
		}
	}

	private AnimationMetaCollection findOrAddCollection(AnimationIndex a)
	{
		for (AnimationMetaCollection am : animationCollections)
			if (am.getAnimationIndex() == a)
				return am;

		AnimationMetaCollection am = new AnimationMetaCollection(a);
		animationCollections.add(am);

		return am;
	}
}
