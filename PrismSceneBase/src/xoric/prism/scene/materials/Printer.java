package xoric.prism.scene.materials;

import xoric.prism.data.exceptions.PrismException;
import xoric.prism.data.types.FloatRect;
import xoric.prism.data.types.IFloatPoint_r;
import xoric.prism.data.types.IFloatRect_r;
import xoric.prism.data.types.IText_r;
import xoric.prism.scene.IRendererUI;
import xoric.prism.scene.shaders.AllShaders;
import xoric.prism.scene.textures.ITexture;
import xoric.prism.scene.textures.grid.GridArt;
import xoric.prism.scene.textures.grid.GridMeta;

public class Printer
{
	public static IRendererUI renderer;

	private final GridArt grid;
	private final GridMeta meta;
	private final FloatRect tempScreenRect;
	private float spriteWidth; // TODO: replace by array
	private float spriteHeight;
	private float padding;
	private float scale;

	public Printer(GridArt grid)
	{
		this.grid = grid;
		this.meta = grid.getMeta();
		this.tempScreenRect = new FloatRect();
		setScale(1.0f);
	}

	public void setScale(float scale)
	{
		this.scale = scale;
		this.spriteWidth = meta.getSpriteSize().getX() * scale;
		this.spriteHeight = meta.getSpriteSize().getY() * scale;
		this.tempScreenRect.setSize(meta.getSpriteSize().getX() * scale, meta.getSpriteSize().getY() * scale);
		this.padding = 8.0f * scale;
	}

	public void print(IFloatPoint_r screenPos, IText_r text) throws PrismException
	{
		ITexture texture = grid.getTexture(0);
		AllShaders.defaultShader.activate();
		AllShaders.defaultShader.setTexture(texture);

		tempScreenRect.setY(screenPos.getY() - spriteHeight);
		tempScreenRect.setX(screenPos.getX());

		for (int i = 0; i < text.length(); ++i)
		{
			int s = text.symbolAt(i);
			IFloatRect_r texRect = meta.getRect(s);

			renderer.drawSprite(texRect, tempScreenRect);
			tempScreenRect.addX(spriteWidth - padding);
		}
	}
}
