package xoric.prism.scene.materials;

import xoric.prism.data.exceptions.PrismException;
import xoric.prism.data.meta.MetaBlock;
import xoric.prism.data.meta.MetaKey;
import xoric.prism.data.meta.MetaLine;
import xoric.prism.data.meta.MetaList;
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
	public static final float DEFAULT_SCALE = 0.75f;

	public static IRendererUI renderer;

	private final GridArt font;
	private final GridMeta gridMeta;
	private final float[] padding;
	private final FloatRect tempScreenRect;
	//	private float spriteHeight;
	private float scale;

	public Printer(GridArt font)
	{
		this.font = font;
		this.gridMeta = font.getMeta();
		this.tempScreenRect = new FloatRect();
		this.padding = new float[64];
		setScale(DEFAULT_SCALE);
	}

	public void setScale(float scale)
	{
		this.scale = scale;
		//		this.spriteHeight = gridMeta.getSpriteSize().getY() * scale;
		this.tempScreenRect.setSize(gridMeta.getSpriteSize().getX() * scale, gridMeta.getSpriteSize().getY() * scale);
	}

	public void print(IFloatPoint_r screenPos, IText_r text, float scale) throws PrismException
	{
		float s = this.scale;
		setScale(scale);
		print(screenPos, text);
		setScale(s);
	}

	public void print(IFloatPoint_r screenPos, IText_r text) throws PrismException
	{
		ITexture texture = font.getTexture(0);
		AllShaders.defaultShader.activate();
		AllShaders.defaultShader.setTexture(texture);

		tempScreenRect.setTopLeft(screenPos);

		for (int i = 0; i < text.length(); ++i)
		{
			int s = text.symbolAt(i);
			IFloatRect_r texRect = gridMeta.getRect(s);

			renderer.drawSprite(texRect, tempScreenRect);
			tempScreenRect.addX(padding[s] * scale);
		}
	}

	@Override
	public void load(MetaList metaList) throws PrismException
	{
		MetaBlock mb = metaList.claimMetaBlock(MetaType.COMMON);
		MetaLine ml = mb.claimLine(MetaKey.SIZE);
		ml.ensureMinima(padding.length, 0, 0);

		for (int i = 0; i < padding.length; ++i)
			padding[i] = ml.getHeap().ints.get(i);
	}

	public float getWidth(int index, float scale)
	{
		return padding[index] * scale;
	}
}
