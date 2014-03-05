package xoric.prism.world.client;

import xoric.prism.data.exceptions.PrismException;
import xoric.prism.data.types.FloatPoint;
import xoric.prism.data.types.IFloatPoint_r;
import xoric.prism.data.types.PrismColor;
import xoric.prism.scene.IDrawableWorld;
import xoric.prism.scene.art.TextureInfo;
import xoric.prism.scene.art.collections.CollectionArt;
import xoric.prism.scene.materials.art.AllArt;
import xoric.prism.scene.materials.shaders.AllShaders;
import xoric.prism.scene.renderer.IWorldRenderer2;
import xoric.prism.world.growth.Growth;

/**
 * @author XoricLee
 * @since 19.02.2014, 16:15:06
 */
public abstract class DrawableGrowth extends Growth implements IDrawableWorld
{
	private TextureInfo texInfoNormal;
	//	private TextureInfo texInfoDamaged;
	private final FloatPoint fullSize;

	protected final PrismColor currentColor;

	private final FloatPoint currentSize;

	private final FloatPoint tempPosition;
	private final FloatPoint tempSize;

	public DrawableGrowth()
	{
		this.fullSize = new FloatPoint();

		this.currentSize = new FloatPoint();
		this.currentColor = new PrismColor(255, 255, 255, 0);
		this.tempPosition = new FloatPoint();
		this.tempSize = new FloatPoint();
	}

	public void setTextures(CollectionArt art, int objectIndex, boolean flipH) throws PrismException
	{
		this.texInfoNormal = art.getTextureInfo(0, objectIndex, 0, 0);
		//		this.texInfoDamaged = art.getTextureInfo(0, objectIndex, 1, 0);

		if (flipH)
		{
			texInfoNormal.flipH();
			//			texInfoDamaged.flipH();
		}
	}

	public void setFullSize(IFloatPoint_r fullSize)
	{
		this.fullSize.copyFrom(fullSize);
	}

	public void setFullSize(float w, float h)
	{
		this.fullSize.x = w;
		this.fullSize.y = h;
	}

	protected abstract float calcWidthFactor(float f);

	protected abstract float calcHeightFactor(float f);

	//	protected abstract void updateColors(float f, float d);

	@Override
	public void damage(int damage)
	{
		super.damage(damage);

		updateSize();
		updateColors();
	}

	@Override
	public void update(int passedSeconds)
	{
		super.update(passedSeconds);

		updateSize();
		updateColors();
	}

	private void updateSize()
	{
		float f = currentGrowth / (float) lifespanSeconds;
		float damageShrink = 1.0f;// - currentDamage / 100.0f;

		currentSize.x = damageShrink * fullSize.x * calcWidthFactor(f);
		currentSize.y = damageShrink * fullSize.y * calcHeightFactor(f);
	}

	private void updateColors()
	{
		//		float f = currentGrowth / 100.0f;
		float d = currentDamage / 100.0f;
		//		f *= Math.sqrt(1.0f - d);
		//		updateColors(f, d);

		currentColor.setAlpha(d);
		//		damageFactor = d;
	}

	@Override
	public void draw(IWorldRenderer2 ren) throws PrismException
	{
		if (texInfoNormal != null)
		{
			AllShaders.growth.activate();
			AllShaders.growth.setTexture(texInfoNormal.getTexture());
			AllShaders.growth.setMask(AllArt.otherMasks.getTexture(0));
			AllShaders.growth.setColor(currentColor);
			TextureInfo maskInfo = AllArt.otherMasks.getTextureInfo(0, 0, 0, 0);
			ren.reset();
			ren.setTexInfo(0, texInfoNormal);
			ren.setTexInfo(1, maskInfo);
			ren.setSprite(position, currentSize);
			ren.drawObject(2);
		}
	}

	/*
	@Override
	public void draw(IRendererWorld renderer, ICameraTransform cam) throws PrismException
	{
		if (texInfoNormal != null)
		{
			cam.transformPosition(position, tempPosition);
			cam.transformSize(currentSize, tempSize);

			AllShaders.defaultShader.activate();
			AllShaders.defaultShader.setTexture(texInfoNormal.getTexture());
			AllShaders.defaultShader.setColor(currentColor);
			renderer.drawObject(texInfoNormal, tempPosition, tempSize, 0.0f);

			if (currentDamage > 0)
			{
				AllShaders.defaultShader.setColor(currentDamageColor);
				renderer.drawObject(texInfoDamaged, tempPosition, tempSize, 0.0f);
			}
		}
	}
	*/
}
