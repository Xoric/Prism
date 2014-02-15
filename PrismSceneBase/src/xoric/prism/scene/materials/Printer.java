package xoric.prism.scene.materials;

import xoric.prism.data.exceptions.PrismException;
import xoric.prism.data.meta.MetaBlock_in;
import xoric.prism.data.meta.MetaKey;
import xoric.prism.data.meta.MetaLine_in;
import xoric.prism.data.meta.MetaList_in;
import xoric.prism.data.meta.MetaType;
import xoric.prism.data.types.FloatRect;
import xoric.prism.data.types.IFloatPoint_r;
import xoric.prism.data.types.IFloatRect_r;
import xoric.prism.data.types.IMetaChild;
import xoric.prism.data.types.IText_r;
import xoric.prism.scene.IRendererUI;
import xoric.prism.scene.shaders.AllShaders;
import xoric.prism.scene.textures.ITexture;
import xoric.prism.scene.textures.grid.GridArt;
import xoric.prism.scene.textures.grid.GridMeta;

public class Printer implements IMetaChild
{
	public static final float DEFAULT_SCALE = 0.62f;

	public static IRendererUI renderer;

	private final GridArt font;
	private final GridMeta gridMeta;
	private final float[] padding;
	private final FloatRect tempScreenRect;
	//	private float spriteHeight;
	private float scale;

	private IText_r text;
	private int startIndex;
	private int endIndex;

	public Printer(GridArt font)
	{
		this.font = font;
		this.gridMeta = font.getMeta();
		this.tempScreenRect = new FloatRect();
		this.padding = new float[64];
		setScale(DEFAULT_SCALE);
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
	public float calcTextWidth(IText_r text)
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
			w += padding[text.symbolAt(i)];

		return w;
	}

	public void setOnset(int startIndex, int endIndex)
	{
		this.startIndex = startIndex;
		this.endIndex = endIndex;
	}

	public void setScale(float scale)
	{
		this.scale = scale;
		//		this.spriteHeight = gridMeta.getSpriteSize().getY() * scale;
		this.tempScreenRect.setSize(gridMeta.getSpriteSize().getX() * scale, gridMeta.getSpriteSize().getY() * scale);
	}

	public float getHeight(float scale)
	{
		return gridMeta.getSpriteSize().getY() * scale;
	}

	public void print(IFloatPoint_r screenPos, float scale) throws PrismException
	{
		float s = this.scale;
		setScale(scale);
		print(screenPos);
		setScale(s);
	}

	public void print(IFloatPoint_r screenPos) throws PrismException
	{
		ITexture texture = font.getTexture(0);
		AllShaders.defaultShader.activate();
		AllShaders.defaultShader.setTexture(texture);

		tempScreenRect.setTopLeft(screenPos);

		for (int i = startIndex; i < endIndex; ++i)
		{
			int s = text.symbolAt(i);
			IFloatRect_r texRect = gridMeta.getRect(s);

			renderer.drawSprite(texRect, tempScreenRect);
			tempScreenRect.addX(padding[s] * scale);
		}
	}

	@Override
	public void load(MetaList_in metaList) throws PrismException
	{
		MetaBlock_in mb = metaList.claimMetaBlock(MetaType.COMMON);
		MetaLine_in ml = mb.claimLine(MetaKey.SIZE);
		ml.ensureMinima(padding.length, 0, 0);

		for (int i = 0; i < padding.length; ++i)
			padding[i] = ml.getHeap().ints.get(i);
	}

	public float getWidth(int index, float scale)
	{
		return padding[index] * scale;
	}
}
