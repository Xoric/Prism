package xoric.prism.world.client.map2;

import java.util.Calendar;

import xoric.prism.data.exceptions.PrismException;
import xoric.prism.data.time.IUpdateListener;
import xoric.prism.data.types.Point;
import xoric.prism.data.types.PrismColor;
import xoric.prism.scene.IDrawableWorld;
import xoric.prism.scene.materials.art.AllArt;
import xoric.prism.scene.materials.shaders.AllShaders;
import xoric.prism.scene.renderer.IWorldRenderer2;
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
	private final AnimatedGround lower;
	private final AnimatedGround upper;
	private boolean holeInGround;
	private float z;

	private final PrismColor fadeColor;

	public DrawableGround2(GroundType2 lower, GroundType2 upper, Point coordinates, float z) throws PrismException
	{
		super(lower, upper, coordinates);

		this.lower = new AnimatedGround(lower);
		this.upper = new AnimatedGround(upper);
		this.z = z;

		this.fadeColor = new PrismColor(255, 255, 255, 0);

		super.weatherFader.setListener(this);
		onWeatherChanged();
	}

	public void setHoleInGround(boolean b)
	{
		holeInGround = b;
	}

	@Override
	public void onWeatherChanged()
	{
		fadeColor.setAlpha(weatherFader.getUpperFactor());
	}

	@Override
	public boolean update(int passedMs)
	{
		lower.update(passedMs);
		upper.update(passedMs);
		return true;
	}

	@Override
	public void draw(IWorldRenderer2 ren) throws PrismException
	{
		//		if (holeInGround)
		//			drawWithHole(ren);
		//		else
		drawNormal(ren);
	}

	private void drawNormal(IWorldRenderer2 ren) throws PrismException
	{
		// consider weather effects
		Weather w0 = super.weatherFader.getLower();
		Weather w1 = super.weatherFader.getUpper();

		// process lowerGround - w0 - 1.0
		AllShaders.color.activate();
		AllShaders.color.setTexture(AllArt.env0.getTexture(w0.ordinal()));
		ren.reset();
		ren.setTexInfo(0, lower.getCurrentTexRect());
		ren.setSprite(rect);
		ren.drawPlane(1);

		// process lowerGround - w1 - 1.0
		if (fadeColor.getAlpha() > 0.0f)
		{
			AllShaders.color.setTexture(AllArt.env0.getTexture(w1.ordinal()));
			AllShaders.color.setColor(fadeColor);
			ren.setTexInfo(0, lower.getCurrentTexRect());
			ren.drawPlane(1);
		}

		int n = AllArt.groundMasks.getMeta().getSpriteCount();
		int i = (Calendar.getInstance().get(Calendar.SECOND) / 2) % n;

		// process upperGround - w0 - 1.0
		AllShaders.mask.activate();
		AllShaders.mask.setTexture(AllArt.env0.getTexture(w0.ordinal()));
		AllShaders.mask.setMask(AllArt.groundMasks.getTexture(0));
		ren.setTexInfo(0, upper.getCurrentTexRect());
		ren.setTexInfo(1, AllArt.groundMasks.getMeta().getRect(i));
		ren.drawPlane(2);

		// process upperGround - w1 - 1.0
		if (fadeColor.getAlpha() > 0.0f)
		{
			AllShaders.mask.setTexture(AllArt.env0.getTexture(w1.ordinal()));
			AllShaders.mask.setColor(fadeColor);
			ren.drawPlane(2);
		}
	}

	/*
	private void drawWithHole(IWorldRenderer2 ren) throws PrismException
	{
		// consider weather effects
		Weather w0 = super.weatherFader.getLower();
		Weather w1 = super.weatherFader.getUpper();

		int n = AllArt.groundMasks.getMeta().getSpriteCount();
		int i = (Calendar.getInstance().get(Calendar.SECOND) / 2) % n;

		// initialize hole shader
		AllShaders.hole.activate();
		AllShaders.hole.setMask(AllArt.groundMasks.getTexture(0));
		AllShaders.hole.setMask2(AllArt.groundMasks.getTexture(0));
		IFloatRect_r mFull = AllArt.groundMasks.getMeta().getRect(20);
		IFloatRect_r mHole = AllArt.groundMasks.getMeta().getRect(19);

		// process lowerGround - w0 - 1.0
		AllShaders.hole.setTexture(AllArt.env0.getTexture(w0.ordinal()));
		ren.drawMask2Plane(lower.getCurrentTexRect(), mFull, mHole, tempRect);

		// process lowerGround - w1 - 1.0
		if (fadeColor.getAlpha() > 0.0f)
		{
			AllShaders.hole.setTexture(AllArt.env0.getTexture(w1.ordinal()));
			//			AllShaders.holeShader.setColor(fadeColor);
			//			renderer.drawPlane(lower.getCurrentTexRect(), tempRect, z);
			renderer.drawMask2Plane(lower.getCurrentTexRect(), mFull, mHole, tempRect);
		}

		IFloatRect_r mConnector = AllArt.groundMasks.getMeta().getRect(i);

		// process upperGround - w0 - 1.0
		AllShaders.hole.activate();
		AllShaders.hole.setTexture(AllArt.env0.getTexture(w0.ordinal()));
		AllShaders.hole.setMask(AllArt.groundMasks.getTexture(0));
		AllShaders.hole.setMask2(AllArt.groundMasks.getTexture(0));
		//		renderer.drawMaskPlane(upper.getCurrentTexRect(), AllArt.masks.getMeta().getRect(i), tempRect);
		renderer.drawMask2Plane(upper.getCurrentTexRect(), mConnector, mHole, tempRect);

		// process upperGround - w1 - 1.0
		if (fadeColor.getAlpha() > 0.0f)
		{
			AllShaders.hole.setTexture(AllArt.env0.getTexture(w1.ordinal()));
			//			AllShaders.holeShader.setColor(fadeColor);
			//			renderer.drawMaskPlane(upper.getCurrentTexRect(), AllArt.masks.getMeta().getRect(1), tempRect);
			renderer.drawMask2Plane(upper.getCurrentTexRect(), mConnector, mHole, tempRect);
		}
	}
	*/

	/*
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
	}*/
}
