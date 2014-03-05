package xoric.prism.scene.materials.tools;

import xoric.prism.data.exceptions.PrismException;
import xoric.prism.data.types.FloatRect;
import xoric.prism.data.types.IFloatPoint_r;
import xoric.prism.data.types.IFloatRect_r;
import xoric.prism.scene.art.ITexture;
import xoric.prism.scene.art.collections.CollectionArt;
import xoric.prism.scene.art.collections.CollectionMeta;
import xoric.prism.scene.art.collections.ObjectInstance;
import xoric.prism.scene.art.collections.ObjectMeta;
import xoric.prism.scene.materials.shaders.AllShaders;
import xoric.prism.scene.renderer.IUIRenderer2;

/**
 * Non thread-safe class for drawing simple composite objects. This class was designed to perform well under heavy load disregarding
 * legibility.
 * @author XoricLee
 */
public class Drawer
{
	public static IUIRenderer2 renderer;

	private final CollectionArt collection;
	private final CollectionMeta meta;

	private final FloatRect tempTexRect;

	private final FloatRect screenRect;
	private int objectIndex;
	private int objectInstance;
	private int rectOnset;
	private float heightLimit;

	public Drawer(CollectionArt collection)
	{
		this.collection = collection;
		this.meta = collection.getMeta();
		this.tempTexRect = new FloatRect();

		this.screenRect = new FloatRect();
	}

	public Drawer setup(int objectIndex, int objectInstance) throws PrismException
	{
		this.objectIndex = objectIndex;
		this.objectInstance = objectInstance;

		return this;
	}

	public Drawer setup(int texture, int objectIndex, int objectInstance) throws PrismException
	{
		this.objectIndex = objectIndex;
		this.objectInstance = objectInstance;

		// bind texture
		ITexture t = collection.getTexture(texture);
		AllShaders.color.activate();
		AllShaders.color.setTexture(t);

		return this;
	}

	public void drawSingle(IFloatPoint_r screenPos) throws PrismException
	{
		ObjectMeta om = meta.getObject(objectIndex);
		ObjectInstance oi = om.getInstance(objectInstance);
		IFloatPoint_r size = oi.getSize(rectOnset);
		IFloatRect_r texRect = oi.getRect(rectOnset);

		// setup parameters
		this.screenRect.setTopLeft(screenPos);
		this.screenRect.setSize(size);

		renderer.reset();
		renderer.setTexInfo(0, texRect);
		renderer.setupSprite(screenRect);
		renderer.drawSprite(1);
	}

	public void drawThreeParts(IFloatPoint_r screenPos, float width) throws PrismException
	{
		// setup parameters
		this.screenRect.setTopLeft(screenPos);
		this.screenRect.setWidth(width);
		this.heightLimit = Float.POSITIVE_INFINITY;
		this.rectOnset = 0;

		// draw composite
		drawThreeParts();
	}

	private void drawThreeParts() throws PrismException
	{
		ObjectMeta om = meta.getObject(objectIndex);
		ObjectInstance oi = om.getInstance(objectInstance);

		// draw left piece
		final float x0 = screenRect.getX();
		final float y0 = screenRect.getY();
		final float w0 = screenRect.getWidth();
		final float h0 = screenRect.getHeight();

		IFloatPoint_r size0 = oi.getSize(rectOnset);
		screenRect.setSize(size0);
		float heightFactor = heightLimit / size0.getY();
		boolean scaleHeight = heightFactor < 1.0f;
		IFloatRect_r t;

		if (scaleHeight)
		{
			tempTexRect.copyFrom(oi.getRect(rectOnset));
			tempTexRect.multiplyHeight(heightFactor);
			screenRect.setHeight(heightLimit);
			t = tempTexRect;
		}
		else
			t = oi.getRect(rectOnset);

		renderer.reset();
		renderer.setTexInfo(0, t);
		renderer.setupSprite(screenRect);
		renderer.drawSprite(1);

		screenRect.addX(size0.getX());

		// draw middle piece
		IFloatPoint_r size1 = oi.getSize(rectOnset + 1);
		IFloatPoint_r size2 = oi.getSize(rectOnset + 2);
		tempTexRect.copyFrom(oi.getRect(rectOnset + 1));
		screenRect.setWidth(size1.getX());
		float x2 = x0 + w0 - size2.getX();

		if (scaleHeight)
			tempTexRect.multiplyHeight(heightFactor);

		while (screenRect.getX() < x2)
		{
			float w = x2 - screenRect.getX();

			if (w < size1.getX())
			{
				tempTexRect.multiplyWidth(w / size1.getX());
				screenRect.setWidth(w);
				w = 0.0f;
			}
			renderer.setTexInfo(0, tempTexRect);
			renderer.setupSprite(screenRect);
			renderer.drawSprite(1);

			screenRect.addX(screenRect.getWidth());
		}

		// draw right piece
		screenRect.setSize(size2);

		if (scaleHeight)
		{
			tempTexRect.copyFrom(oi.getRect(rectOnset + 2));
			tempTexRect.multiplyHeight(heightFactor);
			screenRect.setHeight(heightLimit);
			t = tempTexRect;
		}
		else
			t = oi.getRect(rectOnset + 2);

		renderer.setTexInfo(0, t);
		renderer.setupSprite(screenRect);
		renderer.drawSprite(1);

		// reset screen rectangle
		screenRect.set(x0, y0, w0, h0);
	}

	public void drawNineParts(IFloatRect_r screenRect) throws PrismException
	{
		/* ***** draw top ************ */
		// setup parameters
		this.screenRect.copyFrom(screenRect);
		this.heightLimit = Float.POSITIVE_INFINITY;
		this.rectOnset = 0;

		// draw composite
		drawThreeParts();

		/* ***** draw middle ************ */
		// setup parameters
		this.rectOnset = 3;

		ObjectMeta om = meta.getObject(objectIndex);
		ObjectInstance oi = om.getInstance(objectInstance);
		IFloatPoint_r size0 = oi.getSize(0);
		IFloatPoint_r size6 = oi.getSize(6);
		this.screenRect.addY(size0.getY());
		float y2 = screenRect.getY() + screenRect.getHeight() - size6.getY();
		boolean resume = true;
		float h = oi.getSize(3).getY();
		float hm = y2 - this.screenRect.getY();

		do
		{
			if (h > hm)
			{
				h = hm;
				resume = false;
				this.heightLimit = hm;
			}

			// draw composite	
			drawThreeParts();
			hm -= h;
			this.screenRect.addY(h);
		}
		while (resume);

		/* ***** draw bottom ************ */
		// setup parameters
		this.rectOnset = 6;
		this.heightLimit = Float.POSITIVE_INFINITY;

		// draw composite
		drawThreeParts();
	}
}
