package xoric.prism.world.client.map2;

import xoric.prism.data.exceptions.PrismException;
import xoric.prism.data.time.IUpdateListener;
import xoric.prism.data.types.FloatRect;
import xoric.prism.data.types.IFloatRect_r;
import xoric.prism.data.types.PrismColor;
import xoric.prism.scene.IDrawableWorld;
import xoric.prism.scene.art.ITexture;
import xoric.prism.scene.materials.art.AllArt;
import xoric.prism.scene.materials.shaders.AllShaders;
import xoric.prism.scene.renderer.IWorldRenderer2;

/**
 * @author XoricLee
 * @since 19.02.2014, 02:42:15
 */
public class WeatherMark implements IDrawableWorld, IUpdateListener
{
	private final FloatRect rect;
	private final PrismColor color;
	private final int durationMs;
	private int ageMs;

	public WeatherMark(FloatRect rect, int durationMs)
	{
		this.rect = rect;
		this.color = new PrismColor(1.0f, 1.0f, 1.0f, 1.0f);
		this.durationMs = durationMs;
	}

	@Override
	public void draw(IWorldRenderer2 ren) throws PrismException
	{
		ITexture texture = AllArt.marks0.getTexture(0);
		IFloatRect_r texRect = AllArt.marks0.getMeta().getObject(0).getInstance(0).getRect(0); // TODO replace
		AllShaders.color.activate();
		AllShaders.color.setTexture(texture);
		AllShaders.color.setColor(color);
		ren.reset();
		ren.setTexInfo(0, texRect);
		ren.setSprite(rect);
		ren.drawPlane(1);
	}

	@Override
	public boolean update(int passedMs)
	{
		boolean b = true;

		if (durationMs > 0)
		{
			ageMs += passedMs;
			float f = (durationMs - ageMs) / (float) durationMs;
			if (f <= 0.0f)
			{
				f = 0.0f;
				b = false;
			}
			color.setAlpha(f);
		}
		return b;
	}

	@Deprecated
	public void setPosition(float x, float y) // TODO temporary
	{
		rect.setTopLeft(x, y);
		ageMs = 0;
	}
}
