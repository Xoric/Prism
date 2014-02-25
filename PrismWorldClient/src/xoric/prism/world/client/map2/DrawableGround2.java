package xoric.prism.world.client.map2;

import xoric.prism.data.exceptions.PrismException;
import xoric.prism.data.time.IUpdateListener;
import xoric.prism.data.types.FloatRect;
import xoric.prism.data.types.IFloatRect_r;
import xoric.prism.data.types.Point;
import xoric.prism.data.types.PrismColor;
import xoric.prism.scene.ICameraTransform;
import xoric.prism.scene.IDrawableWorld;
import xoric.prism.scene.IRendererWorld;
import xoric.prism.scene.materials.art.AllArt;
import xoric.prism.scene.materials.shaders.AllShaders;
import xoric.prism.scene.textures.ITexture;
import xoric.prism.world.map2.Ground2;
import xoric.prism.world.map2.GroundType2;
import xoric.prism.world.weather.IWeatherFaderListener;
import xoric.prism.world.weather.Weather;

/**
 * @author XoricLee
 * @since 23.02.2014, 20:43:57
 */
public class DrawableGround2 extends Ground2 implements IUpdateListener, IDrawableWorld, IWeatherFaderListener
{
	private final int interval;
	private final int fullDuration;
	private IFloatRect_r[] texRects;

	private final PrismColor fadeColor;

	private int currentTime;
	private IFloatRect_r currentTexRect;

	private final FloatRect tempRect;

	public DrawableGround2(GroundType2 groundType, Point coordinates) throws PrismException
	{
		super(groundType, coordinates);

		this.interval = groundType.getAnimationInterval();
		this.tempRect = new FloatRect();

		int n = groundType.getAnimationCount();
		if (n > 1)
		{
			texRects = new IFloatRect_r[n];

			int j = 0;
			for (int i = groundType.getAnimationStart(); i < groundType.getAnimationStart() + n; ++i)
				texRects[j++] = AllArt.env0.getMeta().getRect(i);

			currentTexRect = texRects[0];
		}
		else
		{
			currentTexRect = AllArt.env0.getMeta().getRect(groundType.getAnimationStart());
		}
		this.fullDuration = interval * n;
		this.fadeColor = new PrismColor(255, 255, 255, 0);

		super.weatherFader.setListener(this);
		onWeatherChanged();
	}

	// IWeatherFaderListener:
	@Override
	public void onWeatherChanged()
	{
		fadeColor.setAlpha(weatherFader.getUpperFactor());
	}

	// IUpdateListener:
	@Override
	public boolean update(int passedMs)
	{
		if (texRects != null)
		{
			currentTime = (currentTime + passedMs) % fullDuration;
			currentTexRect = texRects[currentTime / interval];
		}
		return true;
	}

	@Override
	public void draw(IRendererWorld renderer, ICameraTransform cam) throws PrismException
	{
		Weather lower = super.weatherFader.getLower();
		Weather upper = super.weatherFader.getUpper();
		float factor = super.weatherFader.getUpperFactor();

		cam.transformWithCameraBounds(rect, tempRect);

		ITexture texture = AllArt.env0.getTexture(lower.ordinal());
		//		IFloatRect_r texRect = Materials.env0.getMeta().getRect(ground.getIndex());
		AllShaders.defaultShader.activate();
		AllShaders.defaultShader.setTexture(texture);
		AllShaders.defaultShader.setColor(PrismColor.opaqueWhite);
		renderer.drawPlane(currentTexRect, tempRect);

		if (factor > 0.0f)
		{
			texture = AllArt.env0.getTexture(upper.ordinal());
			AllShaders.defaultShader.setColor(fadeColor);
			AllShaders.defaultShader.setTexture(texture);
			renderer.drawPlane(currentTexRect, tempRect);
		}
	}
}
