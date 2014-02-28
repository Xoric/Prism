package xoric.prism.scene.materials.tools;

import xoric.prism.data.exceptions.PrismException;
import xoric.prism.data.meta.MetaBlock_in;
import xoric.prism.data.meta.MetaKey;
import xoric.prism.data.meta.MetaLine_in;
import xoric.prism.data.meta.MetaList_in;
import xoric.prism.data.meta.MetaType;
import xoric.prism.data.types.FloatRect;
import xoric.prism.data.types.IFloatPoint_r;
import xoric.prism.data.types.IFloatRect_r;
import xoric.prism.data.types.IMetaChild_in;
import xoric.prism.data.types.IText_r;
import xoric.prism.data.types.PrismColor;
import xoric.prism.data.types.Word;
import xoric.prism.scene.IRendererUI;
import xoric.prism.scene.materials.shaders.AllShaders;
import xoric.prism.scene.textures.ITexture;
import xoric.prism.scene.textures.grid.GridArt;
import xoric.prism.scene.textures.grid.GridMeta;

public class Printer implements IMetaChild_in
{
	public static final float DEFAULT_SCALE = 0.62f;

	public static IRendererUI renderer;

	private final GridArt font;
	private final GridMeta gridMeta;
	private final float[] defaultPadding;
	private final FloatRect tempScreenRect;
	private PrismColor color;
	//	private float spriteHeight;
	//	private float scale;

	private final float defaultHeight;

	private IText_r text;
	private int startIndex;
	private int endIndex;

	public Printer(GridArt font)
	{
		this.font = font;
		this.gridMeta = font.getMeta();
		this.tempScreenRect = new FloatRect();
		this.defaultPadding = new float[64];
		resetColor();
		//		setScale(DEFAULT_SCALE);
		this.defaultHeight = gridMeta.getSpriteSize().getY() * DEFAULT_SCALE;
		this.tempScreenRect.setSize(gridMeta.getSpriteSize());
		this.tempScreenRect.multiply(DEFAULT_SCALE);
	}

	public void setColor(PrismColor color)
	{
		this.color = color;
	}

	public void setText(IText_r text)
	{
		this.text = text;
		this.startIndex = 0;
		this.endIndex = text.length();
	}

	/**
	 * Calculates a given text's width using default font scale.
	 * @param text
	 * @return float
	 */
	public float calcDefaultTextWidth(IText_r text)
	{
		return calcTextWidth(text, 0, text.length());
	}

	/**
	 * Calculates a given text substring's width using default font scale.
	 * @param text
	 * @param startIndex
	 * @param endIndex
	 * @return float
	 */
	public float calcTextWidth(IText_r text, int startIndex, int endIndex)
	{
		float w = 0.0f;
		for (int i = startIndex; i < endIndex; ++i)
			w += defaultPadding[text.symbolAt(i)];

		return w;
	}

	public float calcTextWidth(IText_r text, Word word)
	{
		return calcTextWidth(text, word.start, word.calcEnd());
	}

	public void setOnset(int startIndex, int endIndex)
	{
		this.startIndex = startIndex;
		this.endIndex = endIndex;
	}

	public void resetColor()
	{
		this.color = PrismColor.opaqueWhite;
	}

	//	public void resetScale()
	//	{
	//		setScale(DEFAULT_SCALE);
	//	}

	//	public void setScale(float scale)
	//	{
	//		this.scale = scale;
	//		this.tempScreenRect.setSize(gridMeta.getSpriteSize().getX() * scale, gridMeta.getSpriteSize().getY() * scale);
	//	}

	public float getHeight()
	{
		return defaultHeight;
	}

	//	@Deprecated
	//	public void print(IFloatPoint_r screenPos, float scale) throws PrismException
	//	{
	//		float s = this.scale;
	//		setScale(scale);
	//		print(screenPos);
	//		setScale(s);
	//	}

	public float print(IFloatPoint_r screenPos) throws PrismException
	{
		ITexture texture = font.getTexture(0);
		AllShaders.color.activate();
		AllShaders.color.setTexture(texture);
		AllShaders.color.setColor(color);

		tempScreenRect.setTopLeft(screenPos);

		for (int i = startIndex; i < endIndex; ++i)
		{
			int s = text.symbolAt(i);
			IFloatRect_r texRect = gridMeta.getRect(s);

			renderer.drawSprite(texRect, tempScreenRect);
			tempScreenRect.addX(defaultPadding[s]);
		}
		return tempScreenRect.getX();
	}

	public float getDefaultPadding(int symbol)
	{
		return defaultPadding[symbol];
	}

	@Override
	public void load(MetaList_in metaList) throws PrismException
	{
		MetaBlock_in mb = metaList.claimMetaBlock(MetaType.COMMON);
		MetaLine_in ml = mb.claimLine(MetaKey.SIZE);
		ml.ensureMinima(defaultPadding.length, 0, 0);

		for (int i = 0; i < defaultPadding.length; ++i)
			defaultPadding[i] = ml.getHeap().ints.get(i) * DEFAULT_SCALE;
	}
}
