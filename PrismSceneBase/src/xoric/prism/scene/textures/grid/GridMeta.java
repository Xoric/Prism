package xoric.prism.scene.textures.grid;

import java.util.ArrayList;
import java.util.List;

import xoric.prism.data.exceptions.PrismException;
import xoric.prism.data.exceptions.UserErrorText;
import xoric.prism.data.meta.MetaBlock_in;
import xoric.prism.data.meta.MetaKey;
import xoric.prism.data.meta.MetaLine_in;
import xoric.prism.data.meta.MetaList_in;
import xoric.prism.data.meta.MetaType;
import xoric.prism.data.types.FloatPoint;
import xoric.prism.data.types.FloatRect;
import xoric.prism.data.types.IFloatPoint_r;
import xoric.prism.data.types.IFloatRect_r;
import xoric.prism.data.types.IText_r;
import xoric.prism.scene.textures.ArtMeta;

public class GridMeta extends ArtMeta
{
	private IText_r name;
	private IFloatPoint_r spriteSize;
	private int columnCount;
	private final List<FloatRect> rects;

	public GridMeta()
	{
		rects = new ArrayList<FloatRect>();
	}

	@Override
	public void load(MetaList_in metaList) throws PrismException
	{
		rects.clear();
		columnCount = 0;

		MetaBlock_in mb = metaList.claimMetaBlock(MetaType.GRID);
		IFloatPoint_r textureSize = null;

		for (MetaLine_in ml : mb.getMetaLines())
		{
			textureSize = interpretLine(textureSize, ml);
		}
	}

	public IFloatRect_r getRect(int index)
	{
		return rects.get(index);
	}

	public IFloatPoint_r getSpriteSize()
	{
		return spriteSize;
	}

	public int getSpriteCount()
	{
		return rects.size();
	}

	private IFloatPoint_r interpretLine(IFloatPoint_r textureSize, MetaLine_in ml) throws PrismException
	{
		MetaKey key = ml.getKey();

		if (key == MetaKey.ITEM)
		{
			name = interpretItem(ml);
		}
		else if (key == MetaKey.SIZE)
		{
			textureSize = interpretSize(ml);
		}
		else if (key == MetaKey.ALT)
		{
			columnCount = interpretAlt(ml);
		}
		else if (key == MetaKey.COUNT)
		{
			interpretCount(ml, textureSize);
		}
		return textureSize;
	}

	private IText_r interpretItem(MetaLine_in ml) throws PrismException
	{
		ml.ensureMinima(0, 0, 1);
		IText_r name = ml.getHeap().texts.get(0);
		return name;
	}

	private IFloatPoint_r interpretSize(MetaLine_in ml) throws PrismException
	{
		ml.ensureMinima(4, 0, 0);
		int w = ml.getHeap().ints.get(0);
		int h = ml.getHeap().ints.get(1);
		FloatPoint textureSize = new FloatPoint(w, h);

		w = ml.getHeap().ints.get(2);
		h = ml.getHeap().ints.get(3);
		spriteSize = new FloatPoint(w, h);

		return textureSize;
	}

	private int interpretAlt(MetaLine_in ml) throws PrismException
	{
		ml.ensureMinima(1, 0, 0);
		int columnCount = ml.getHeap().ints.get(0);
		return columnCount;
	}

	private void interpretCount(MetaLine_in ml, IFloatPoint_r textureSize) throws PrismException
	{
		ml.ensureMinima(1, 0, 0);
		ensureColumnSpriteSizeIsSet(ml);
		ensureTextureSizeIsSet(ml, textureSize);
		ensureColumnCountIsSet(ml);
		int n = ml.getHeap().ints.get(0);
		int c = 0;
		int r = 0;
		final float w = spriteSize.getX() / textureSize.getX();
		final float h = spriteSize.getY() / textureSize.getY();

		for (int i = 0; i < n; ++i)
		{
			rects.add(new FloatRect(c * w, r * h, w, h));

			if (++c >= columnCount)
			{
				c = 0;
				++r;
			}
		}
	}

	private void ensureColumnCountIsSet(MetaLine_in ml) throws PrismException
	{
		if (columnCount <= 0)
		{
			PrismException e = new PrismException();
			e.user.setText(UserErrorText.LOCAL_GAME_FILE_CAUSED_PROBLEM);
			e.code.setText("column count of grid was zero");
			ml.addExceptionInfoTo(e);
			throw e;
		}
	}

	private void ensureColumnSpriteSizeIsSet(MetaLine_in ml) throws PrismException
	{
		if (spriteSize == null)
		{
			PrismException e = new PrismException();
			e.user.setText(UserErrorText.LOCAL_GAME_FILE_CAUSED_PROBLEM);
			e.code.setText("sprite size of grid was null");
			ml.addExceptionInfoTo(e);
			throw e;
		}
	}

	private void ensureTextureSizeIsSet(MetaLine_in ml, IFloatPoint_r textureSize) throws PrismException
	{
		if (textureSize == null)
		{
			PrismException e = new PrismException();
			e.user.setText(UserErrorText.LOCAL_GAME_FILE_CAUSED_PROBLEM);
			e.code.setText("texture size of grid was null");
			ml.addExceptionInfoTo(e);
			throw e;
		}
	}
}
