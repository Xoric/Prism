package xoric.prism.world.client;

import xoric.prism.data.exceptions.PrismException;
import xoric.prism.data.types.FloatPoint;
import xoric.prism.data.types.IFloatPoint_r;
import xoric.prism.data.types.PrismColor;
import xoric.prism.scene.ICameraTransform;
import xoric.prism.scene.IDrawableWorld;
import xoric.prism.scene.IRendererWorld;
import xoric.prism.scene.materials.shaders.AllShaders;
import xoric.prism.scene.textures.TextureInfo;
import xoric.prism.scene.textures.collections.CollectionArt;
import xoric.prism.world.growth.Growth;

/**
 * @author XoricLee
 * @since 19.02.2014, 16:15:06
 */
public abstract class DrawableGrowth extends Growth implements IDrawableWorld
{
	private TextureInfo texInfoNormal;
	private TextureInfo texInfoDamaged;
	private final FloatPoint fullSize;

	protected final PrismColor currentColor;
	protected final PrismColor currentDamageColor;

	private final FloatPoint currentSize;

	private final FloatPoint tempPosition;
	private final FloatPoint tempSize;

	public DrawableGrowth()
	{
		this.fullSize = new FloatPoint();

		this.currentSize = new FloatPoint();
		this.currentColor = new PrismColor();
		this.currentDamageColor = new PrismColor();
		this.tempPosition = new FloatPoint();
		this.tempSize = new FloatPoint();
	}

	public void setTextures(CollectionArt art, int objectIndex, boolean flipH) throws PrismException
	{
		this.texInfoNormal = art.getTextureInfo(0, objectIndex, 0, 0);
		this.texInfoDamaged = art.getTextureInfo(0, objectIndex, 1, 0);

		//		IPoint_r size = texInfoNormal.getTexture().getSize();

		//		System.out.println("normal | x=" + texInfoNormal.getRect().getX() + ", y=" + texInfoNormal.getRect().getY() + " | x="
		//				+ texInfoNormal.getRect().getX() * size.getX() + ", y=" + texInfoNormal.getRect().getY() * size.getY());
		//		System.out.println("damaged | x=" + texInfoDamaged.getRect().getX() + ", y=" + texInfoDamaged.getRect().getY() + " | x="
		//				+ texInfoDamaged.getRect().getX() * size.getX() + ", y=" + texInfoDamaged.getRect().getY() * size.getY());

		if (flipH)
		{
			texInfoNormal.flipH();
			texInfoDamaged.flipH();
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

	protected abstract void updateColors(float f, float d);

	@Override
	public void damage(int damage)
	{
		super.damage(damage);

		updateColors();
	}

	@Override
	public void update(int passedSeconds)
	{
		super.update(passedSeconds);

		float f = currentGrowth / (float) lifespanSeconds;

		currentSize.x = fullSize.x * calcWidthFactor(f);
		currentSize.y = fullSize.y * calcHeightFactor(f);

		updateColors();
	}

	private void updateColors()
	{
		float f = currentGrowth / 100.0f;
		float d = currentDamage / 100.0f;
		f *= Math.sqrt(1.0f - d);
		updateColors(f, d);
	}

	@Override
	public void draw(IRendererWorld renderer, ICameraTransform cam) throws PrismException
	{
		if (texInfoNormal != null)
		{
			cam.transformWithCameraBounds(position, tempPosition);
			cam.transformWithCameraBounds(currentSize, tempSize);

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
}
