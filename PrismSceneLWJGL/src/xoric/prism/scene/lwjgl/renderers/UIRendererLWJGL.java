package xoric.prism.scene.lwjgl.renderers;

import org.lwjgl.opengl.GL11;

import xoric.prism.data.types.IFloatRect_r;
import xoric.prism.scene.renderer.UIRenderer;

/**
 * @author XoricLee
 * @since 26.02.2014, 16:01:39
 */
public class UIRendererLWJGL extends UIRenderer
{
	public UIRendererLWJGL()
	{
		super(true);
	}

	// IUIRenderer2:
	@Override
	public void drawSprite(int texCount)
	{
		IFloatRect_r screenRect = super.getScreenRect();
		float sx1 = screenRect.getX();
		//		float sy1 = screenRect.getY();
		float sy1 = 1.0f - screenRect.getY();
		float sx2 = screenRect.getRight();
		//		float sy2 =  screenRect.getBottom();
		float sy2 = 1.0f - screenRect.getBottom();

		IFloatRect_r texRect = super.getTexRect(0);
		float tx1 = texRect.getX();
		float ty1 = texRect.getY();
		//		float ty1 = 1.0f - texRect.getY();
		float tx2 = texRect.getRight();
		float ty2 = texRect.getBottom();
		//		float ty2 = 1.0f - texRect.getBottom();

		GL11.glBegin(GL11.GL_QUADS);
		// ==========================
		GL11.glTexCoord2f(tx1, ty1);
		GL11.glVertex2f(sx1, sy1);
		// --
		GL11.glTexCoord2f(tx2, ty1);
		GL11.glVertex2f(sx2, sy1);
		// --
		GL11.glTexCoord2f(tx2, ty2);
		GL11.glVertex2f(sx2, sy2);
		// --
		GL11.glTexCoord2f(tx1, ty2);
		GL11.glVertex2f(sx1, sy2);
		// ==========================
		GL11.glEnd();
	}
}
