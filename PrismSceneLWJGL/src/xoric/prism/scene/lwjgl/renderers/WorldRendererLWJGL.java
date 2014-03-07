package xoric.prism.scene.lwjgl.renderers;

import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL13;

import xoric.prism.data.types.IFloatPoint_r;
import xoric.prism.scene.art.hotspots.Marker;
import xoric.prism.scene.camera.ICameraTransform;
import xoric.prism.scene.renderer.WorldRenderer;

/**
 * @author XoricLee
 * @since 26.02.2014, 16:01:39
 */
public class WorldRendererLWJGL extends WorldRenderer
{
	private float slope;

	public WorldRendererLWJGL(float slope, ICameraTransform cam)
	{
		super(true);

		this.slope = slope;
		super.setCamera(cam);
	}

	public void setSlope(float slope)
	{
		this.slope = slope;
	}

	public float calcNewZ(float y) // TODO make private
	{
		// TODO reimplement
		y = 0.5f - y;

		float z = -1.5f - (1.0f - y) * slope;
		if (z > -1.499f)
			z = -1.499f;
		else if (z < -6.499f)
			z = -6.499f;

		return z;
	}

	@Override
	public void drawPlane(int texCount)
	{
		/* [this code correctly transforms coordinates where topLeft=(0,0), bottomRight=(1,1)]
		IFloatPoint_r screenPos = super.getScreenPos();
		float sx1 = -0.5f + screenPos.getX();

		IFloatPoint_r spriteSize = super.getSpriteSize();
		float w = spriteSize.getX();
		float h = spriteSize.getY();
		float sx2 = sx1 + w;

		float sy0 = screenPos.getY();
		float sy1 = 0.5f - sy0;
		float sy2 = sy1 - h;

		float z = super.getZ();
		float zFront = calcZ(sy0) + z;
		float zBack = calcZ(sy0 + h) + z;
		*/
		IFloatPoint_r screenPos = super.getScreenPos();
		float sx1 = screenPos.getX();

		IFloatPoint_r spriteSize = super.getSpriteSize();
		float w = spriteSize.getX();
		float h = spriteSize.getY();
		float sx2 = sx1 + w;

		float sy0 = screenPos.getY();
		float sy1 = sy0;
		float sy2 = sy1 + h;

		float z = super.getZ();
		float zFront = calcNewZ(sy0) + z;
		float zBack = calcNewZ(sy0 + h) + z;

		GL11.glBegin(GL11.GL_QUADS);
		// ==========================
		if (texCount == 1)
			GL11.glTexCoord2f(getTexRect(0).getX(), getTexRect(0).getY());
		else
			for (int i = 0; i < texCount; ++i)
				GL13.glMultiTexCoord2f(GL13.GL_TEXTURE0 + i, getTexRect(i).getX(), getTexRect(i).getY());

		GL11.glVertex3f(sx1, sy1, zFront);
		// ==========================
		if (texCount == 1)
			GL11.glTexCoord2f(getTexRect(0).getRight(), getTexRect(0).getY());
		else
			for (int i = 0; i < texCount; ++i)
				GL13.glMultiTexCoord2f(GL13.GL_TEXTURE0 + i, getTexRect(i).getRight(), getTexRect(i).getY());

		GL11.glVertex3f(sx2, sy1, zFront);
		// ==========================
		if (texCount == 1)
			GL11.glTexCoord2f(getTexRect(0).getRight(), getTexRect(0).getBottom());
		else
			for (int i = 0; i < texCount; ++i)
				GL13.glMultiTexCoord2f(GL13.GL_TEXTURE0 + i, getTexRect(i).getRight(), getTexRect(i).getBottom());

		GL11.glVertex3f(sx2, sy2, zBack);
		// ==========================
		if (texCount == 1)
			GL11.glTexCoord2f(getTexRect(0).getX(), getTexRect(0).getBottom());
		else
			for (int i = 0; i < texCount; ++i)
				GL13.glMultiTexCoord2f(GL13.GL_TEXTURE0 + i, getTexRect(i).getX(), getTexRect(i).getBottom());

		GL11.glVertex3f(sx1, sy2, zBack);
		// ==========================
		GL11.glEnd();
	}

	// IWorldRenderer2:
	@Override
	public void drawObject(int texCount)
	{
		/* [this code correctly transforms coordinates where topLeft=(0,0), bottomRight=(1,1)]
		IFloatPoint_r screenPos = super.getScreenPos();
		float sxCenter = -0.5f + screenPos.getX();
		float sy0 = screenPos.getY();

		IFloatPoint_r spriteSize = super.getSpriteSize();
		float wHalf = spriteSize.getX() * 0.5f;
		float h = spriteSize.getY();
		float z = calcZ(sy0) + getZ();

		float sx1 = sxCenter - wHalf;
		float sx2 = sxCenter + wHalf;
		float sy1 = 0.5f - sy0 + h;
		float sy2 = sy1 - h;
		*/
		IFloatPoint_r screenPos = super.getScreenPos();

		float sx0 = screenPos.getX();
		Marker marker = getMarker();
		IFloatPoint_r spriteSize = super.getSpriteSize();
		float sx1, sx2;

		float sy0 = screenPos.getY();
		float h = spriteSize.getY();
		float sy1, sy2;

		if (marker == null)
		{
			float wHalf = spriteSize.getX() * 0.5f;
			sx1 = sx0 - wHalf;
			sx2 = sx0 + wHalf;

			sy1 = sy0 - h;
			sy2 = sy1 + h;
		}
		else
		{
			sx1 = sx0 - marker.getHotspot().getX();
			sx2 = sx1 + spriteSize.getX();

			sy1 = sy0 - h - marker.getHotspot().getY();
			sy2 = sy1 + h;
		}
		float z = calcNewZ(sy0) + getZ();

		GL11.glBegin(GL11.GL_QUADS);
		// ==========================

		if (texCount == 1)
			GL11.glTexCoord2f(getTexRect(0).getX(), getTexRect(0).getY());
		else
			for (int i = 0; i < texCount; ++i)
				GL13.glMultiTexCoord2f(GL13.GL_TEXTURE0 + i, getTexRect(i).getX(), getTexRect(i).getY());

		GL11.glVertex3f(sx1, sy1, z);
		// ==========================
		if (texCount == 1)
			GL11.glTexCoord2f(getTexRect(0).getRight(), getTexRect(0).getY());
		else
			for (int i = 0; i < texCount; ++i)
				GL13.glMultiTexCoord2f(GL13.GL_TEXTURE0 + i, getTexRect(i).getRight(), getTexRect(i).getY());

		GL11.glVertex3f(sx2, sy1, z);
		// ==========================
		if (texCount == 1)
			GL11.glTexCoord2f(getTexRect(0).getRight(), getTexRect(0).getBottom());
		else
			for (int i = 0; i < texCount; ++i)
				GL13.glMultiTexCoord2f(GL13.GL_TEXTURE0 + i, getTexRect(i).getRight(), getTexRect(i).getBottom());

		GL11.glVertex3f(sx2, sy2, z);
		// ==========================
		if (texCount == 1)
			GL11.glTexCoord2f(getTexRect(0).getX(), getTexRect(0).getBottom());
		else
			for (int i = 0; i < texCount; ++i)
				GL13.glMultiTexCoord2f(GL13.GL_TEXTURE0 + i, getTexRect(i).getX(), getTexRect(i).getBottom());

		GL11.glVertex3f(sx1, sy2, z);
		// ==========================
		GL11.glEnd();
	}
}
