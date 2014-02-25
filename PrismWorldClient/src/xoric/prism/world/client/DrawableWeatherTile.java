package xoric.prism.world.client;

import xoric.prism.data.exceptions.PrismException;
import xoric.prism.data.time.IUpdateListener;
import xoric.prism.data.types.FloatRect;
import xoric.prism.data.types.IFloatRect_r;
import xoric.prism.data.types.IPoint_r;
import xoric.prism.data.types.PrismColor;
import xoric.prism.scene.ICameraTransform;
import xoric.prism.scene.IDrawableWorld;
import xoric.prism.scene.IRendererWorld;
import xoric.prism.scene.materials.art.AllArt;
import xoric.prism.scene.materials.shaders.AllShaders;
import xoric.prism.scene.textures.ITexture;
import xoric.prism.world.map.Ground;
import xoric.prism.world.map.WeatherTile;
import xoric.prism.world.weather.IWeatherFaderListener;
import xoric.prism.world.weather.Weather;

/**
 * @author XoricLee
 * @since 18.02.2014, 23:49:43
 */
@Deprecated
public class DrawableWeatherTile extends WeatherTile implements IDrawableWorld, IWeatherFaderListener, IUpdateListener
{
	private final FloatRect rect;
	private final FloatRect tempRect;
	private final PrismColor color;

	private int timeMs;

	public DrawableWeatherTile(IPoint_r coords, Ground ground)
	{
		super(coords, ground);

		rect = new FloatRect(coords.getX() * WIDTH, coords.getY() * HEIGHT, WIDTH, HEIGHT);
		tempRect = new FloatRect();
		weatherFader.setListener(this);
		color = new PrismColor(1.0f, 1.0f, 1.0f, 1.0f);
	}

	@Override
	public void draw(IRendererWorld renderer, ICameraTransform cam) throws PrismException
	{
		Weather lower = weatherFader.getLower();
		Weather upper = weatherFader.getUpper();
		float factor = weatherFader.getUpperFactor();

		cam.transformWithCameraBounds(rect, tempRect);

		ITexture texture = AllArt.env0.getTexture(lower.ordinal());
		IFloatRect_r texRect = AllArt.env0.getMeta().getRect(ground.getIndex());
		AllShaders.defaultShader.activate();
		AllShaders.defaultShader.setTexture(texture);
		AllShaders.defaultShader.setColor(PrismColor.opaqueWhite);
		renderer.drawPlane(texRect, tempRect);

		if (factor > 0.0f)
		{
			texture = AllArt.env0.getTexture(upper.ordinal());
			AllShaders.defaultShader.setColor(color);
			AllShaders.defaultShader.setTexture(texture);
			renderer.drawPlane(texRect, tempRect);
		}
	}

	@Override
	public void onWeatherChanged()
	{
		color.setAlpha(weatherFader.getUpperFactor());
	}

	@Override
	public boolean update(int passedMs)
	{
		//		if ()
		//		timeMs = (timeMs + passedMs);

		return true;
	}
}
