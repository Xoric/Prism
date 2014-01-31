package xoric.prism.scene.materials;

import xoric.prism.data.exceptions.PrismException;
import xoric.prism.data.types.FloatRect;
import xoric.prism.data.types.IFloatPoint_r;
import xoric.prism.scene.IRendererUI;
import xoric.prism.scene.shaders.AllShaders;
import xoric.prism.scene.textures.ITexture;
import xoric.prism.scene.textures.collections.CollectionArt;
import xoric.prism.scene.textures.collections.CollectionMeta;
import xoric.prism.scene.textures.collections.ObjectInstance;
import xoric.prism.scene.textures.collections.ObjectMeta;

public class Drawer
{
	public static IRendererUI renderer;

	private final CollectionArt collection;
	private final CollectionMeta meta;
	private final FloatRect tempScreenRect;
	private final FloatRect tempTexRect;

	public Drawer(CollectionArt collection)
	{
		this.collection = collection;
		this.meta = collection.getMeta();
		this.tempScreenRect = new FloatRect();
		this.tempTexRect = new FloatRect();
	}

	public void drawThreeParter(IFloatPoint_r screenPos, float width, int objectIndex, int objectInstance) throws PrismException
	{
		ObjectMeta om = meta.getObject(objectIndex);
		ObjectInstance oi = om.getInstance(objectInstance);
		ITexture texture = collection.getTexture(0);
		AllShaders.defaultShader.activate();
		AllShaders.defaultShader.setTexture(texture);

		// draw left piece
		IFloatPoint_r size0 = oi.getSize(0);
		tempScreenRect.copyFrom(screenPos, size0);
		renderer.drawSprite(oi.getRect(0), tempScreenRect);
		tempScreenRect.addX(size0.getX());

		// draw middle piece
		IFloatPoint_r size1 = oi.getSize(1);
		IFloatPoint_r size2 = oi.getSize(2);
		tempTexRect.copyFrom(oi.getRect(1));
		tempScreenRect.setWidth(size1.getX());
		float x2 = screenPos.getX() + width - size2.getX();

		//		float w = width - size0.getX() - size2.getX();
		//		float ws = tempScreenRect.getWidth();

		while (tempScreenRect.getX() < x2)
		{
			float w = x2 - tempScreenRect.getX();

			if (w < size1.getX())
			{
				tempTexRect.multiplyWidth(w / size1.getX());
				tempScreenRect.setWidth(w);
				w = 0.0f;
			}
			renderer.drawSprite(tempTexRect, tempScreenRect);
			tempScreenRect.addX(tempScreenRect.getWidth());
		}

		// draw right piece
		tempScreenRect.setWidth(size2.getX());
		renderer.drawSprite(oi.getRect(2), tempScreenRect);
	}
}
