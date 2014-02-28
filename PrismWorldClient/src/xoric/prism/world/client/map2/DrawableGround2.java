package xoric.prism.world.client.map2;

import java.util.Calendar;

import xoric.prism.data.exceptions.PrismException;
import xoric.prism.data.time.IUpdateListener;
import xoric.prism.data.types.FloatRect;
import xoric.prism.data.types.IFloatRect_r;
import xoric.prism.data.types.Point;
import xoric.prism.data.types.PrismColor;
import xoric.prism.scene.IDrawableWorld;
import xoric.prism.scene.IRendererWorld;
import xoric.prism.scene.camera.ICameraTransform;
import xoric.prism.scene.materials.art.AllArt;
import xoric.prism.scene.materials.shaders.AllShaders;
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

	private final FloatRect tempRect;

	public DrawableGround2(GroundType2 lower, GroundType2 upper, Point coordinates, float z) throws PrismException
	{
		super(lower, upper, coordinates);

		this.lower = new AnimatedGround(lower);
		this.upper = new AnimatedGround(upper);
		this.z = z;

		this.tempRect = new FloatRect();

		this.fadeColor = new PrismColor(255, 255, 255, 0);

		super.weatherFader.setListener(this);
		onWeatherChanged();
	}

	public void setHoleInGround(boolean b)
	{
		holeInGround = b;
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
		lower.update(passedMs);
		upper.update(passedMs);
		return true;
	}

	/*
	@Override
	public void draw(IRendererWorld renderer, ICameraTransform cam) throws PrismException
	{
		Weather lower = super.weatherFader.getLower();
		Weather upper = super.weatherFader.getUpper();
		float factor = super.weatherFader.getUpperFactor();

		cam.transformWithCameraBounds(rect, tempRect);

		// draw lower ground to ground buffer
		AllBuffers.groundBuffer.activate();
		AllShaders.defaultShader.activate();

		AllShaders.defaultShader.setTexture(AllArt.env0.getTexture(lower.ordinal()));
		AllBuffers.groundBuffer.drawSprite(currentTexRect, AllBuffers.groundBuffer.getRect());

		// draw upper ground to ground buffer
		if (factor > 0.0f)
		{
			AllShaders.defaultShader.setColor(fadeColor);
			AllShaders.defaultShader.setTexture(AllArt.env0.getTexture(upper.ordinal()));
			AllBuffers.groundBuffer.drawSprite(currentTexRect, AllBuffers.groundBuffer.getRect());
		}

		// deactivate ground buffer (render target will be screen)
		AllBuffers.groundBuffer.resetToUI();

		// draw ground buffer to screen
		AllShaders.defaultShader.activate();
		AllShaders.defaultShader.setTexture(AllBuffers.groundBuffer.getTextureID());
		renderer.drawPlane(AllBuffers.groundBuffer.getRect(), tempRect);
	}
	*/

	@Override
	public void draw(IRendererWorld renderer, ICameraTransform cam) throws PrismException
	{
		if (holeInGround)
			drawWithHole(renderer, cam);
		else
			drawNormal(renderer, cam);
	}

	private void drawNormal(IRendererWorld renderer, ICameraTransform cam) throws PrismException
	{
		// consider weather effects
		Weather w0 = super.weatherFader.getLower();
		Weather w1 = super.weatherFader.getUpper();

		// transform target rect
		cam.transformRect(rect, tempRect);

		// process lowerGround - w0 - 1.0
		AllShaders.color.activate();
		AllShaders.color.setTexture(AllArt.env0.getTexture(w0.ordinal()));
		renderer.drawPlane(lower.getCurrentTexRect(), tempRect, z);

		// process lowerGround - w1 - 1.0
		if (fadeColor.getAlpha() > 0.0f)
		{
			AllShaders.color.setTexture(AllArt.env0.getTexture(w1.ordinal()));
			AllShaders.color.setColor(fadeColor);
			renderer.drawPlane(lower.getCurrentTexRect(), tempRect, z);
		}

		int n = AllArt.groundMasks.getMeta().getSpriteCount();
		int i = (Calendar.getInstance().get(Calendar.SECOND) / 2) % n;

		// process upperGround - w0 - 1.0
		AllShaders.mask.activate();
		AllShaders.mask.setTexture(AllArt.env0.getTexture(w0.ordinal()));
		AllShaders.mask.setMask(AllArt.groundMasks.getTexture(0));
		renderer.drawMaskPlane(upper.getCurrentTexRect(), AllArt.groundMasks.getMeta().getRect(i), tempRect);

		// process upperGround - w1 - 1.0
		if (fadeColor.getAlpha() > 0.0f)
		{
			AllShaders.mask.setTexture(AllArt.env0.getTexture(w1.ordinal()));
			//			AllShaders.maskShader.setColor(fadeColor);
			renderer.drawMaskPlane(upper.getCurrentTexRect(), AllArt.groundMasks.getMeta().getRect(1), tempRect);
		}

		//		if (fadeColor.getAlpha() > 0.0f)
		//		{
		//			AllShaders.maskShader.setColor(fadeColor);
		//			AllShaders.maskShader.setTexture(AllArt.env0.getTexture(w1.ordinal()));
		//			AllShaders.maskShader.setMask(AllArt.masks.getTexture(0));
		//			renderer.drawMaskPlane(currentTexRect, AllArt.mark0.getMeta().getRect(1), screenRect);
		//		}

		// process lower ground
		/*
		lower.prepareGroundBuffer(w0, w1, fadeColor);
		AllShaders.defaultShader.activate();
		AllShaders.defaultShader.setTexture(AllBuffers.groundBuffer.getTextureID());
		renderer.drawPlane(AllBuffers.groundBuffer.getRect(), tempRect);
		*/

		//		renderer.drawPlane(normRect, tempRect);

		// process upper ground
		//		upper.prepareGroundBuffer(w0, w1, fadeColor);
		//		AllShaders.defaultShader.setTexture(AllBuffers.groundBuffer.getTextureID());
		//		renderer.drawPlane(AllBuffers.groundBuffer.getRect(), tempRect);

		//		upper.prepareGroundBuffer(w0, w1, fadeColor);
		//		AllShaders.maskShader.activate();
		//		AllShaders.maskShader.setTexture(AllBuffers.groundBuffer.getTextureID());
		//		AllShaders.maskShader.setMask(AllArt.masks.getTexture(0));
		//		IFloatRect_r maskRect = AllArt.masks.getMeta().getRect(2);
		//		renderer.drawMaskPlane(AllBuffers.groundBuffer.getRect(), maskRect, tempRect);
	}

	private void drawWithHole(IRendererWorld renderer, ICameraTransform cam) throws PrismException
	{
		// consider weather effects
		Weather w0 = super.weatherFader.getLower();
		Weather w1 = super.weatherFader.getUpper();

		// transform target rect
		cam.transformRect(rect, tempRect);

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
		renderer.drawMask2Plane(lower.getCurrentTexRect(), mFull, mHole, tempRect);

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
