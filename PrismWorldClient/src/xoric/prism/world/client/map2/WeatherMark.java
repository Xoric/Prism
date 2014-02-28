package xoric.prism.world.client.map2;

import xoric.prism.data.exceptions.PrismException;
import xoric.prism.data.time.IUpdateListener;
import xoric.prism.data.types.FloatRect;
import xoric.prism.data.types.IFloatRect_r;
import xoric.prism.data.types.PrismColor;
import xoric.prism.scene.IDrawableWorld;
import xoric.prism.scene.IRendererWorld;
import xoric.prism.scene.camera.ICameraTransform;
import xoric.prism.scene.materials.art.AllArt;
import xoric.prism.scene.materials.shaders.AllShaders;
import xoric.prism.scene.textures.ITexture;

/**
 * @author XoricLee
 * @since 19.02.2014, 02:42:15
 */
public class WeatherMark implements IDrawableWorld, IUpdateListener
{
	private final FloatRect rect;
	private final FloatRect tempRect;
	private final PrismColor color;
	private final int durationMs;
	private int ageMs;

	public WeatherMark(FloatRect rect, int durationMs)
	{
		this.rect = rect;
		this.tempRect = new FloatRect();
		this.color = new PrismColor(1.0f, 1.0f, 1.0f, 1.0f);
		this.durationMs = durationMs;
	}

	@Override
	public void draw(IRendererWorld renderer, ICameraTransform cam) throws PrismException
	{
		cam.transformRect(rect, tempRect);

		ITexture texture = AllArt.mark0.getTexture(0);
		IFloatRect_r texRect = AllArt.mark0.getMeta().getRect(0);
		AllShaders.color.activate();
		AllShaders.color.setTexture(texture);
		AllShaders.color.setColor(color);
		renderer.drawPlane(texRect, tempRect, 0.0f);
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
