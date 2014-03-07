package xoric.prism.scene.lwjgl;

import java.nio.FloatBuffer;
import java.nio.IntBuffer;

import org.lwjgl.BufferUtils;
import org.lwjgl.input.Mouse;
import org.lwjgl.opengl.GL11;
import org.lwjgl.util.glu.GLU;

import xoric.prism.data.types.FloatPoint;
import xoric.prism.data.types.IFloatPoint_r;

/**
 * @author XoricLee
 * @since 07.03.2014, 14:56:18
 */
public class MouseProjectorLWJGL
{
	private final FloatBuffer modelview;
	private final FloatBuffer projection;
	private final IntBuffer viewport;
	private final FloatBuffer screenDepth;
	private final FloatBuffer worldPos;

	private final FloatPoint mouseInWorld;

	public MouseProjectorLWJGL()
	{
		modelview = BufferUtils.createFloatBuffer(16);
		projection = BufferUtils.createFloatBuffer(16);
		viewport = BufferUtils.createIntBuffer(16);
		screenDepth = BufferUtils.createFloatBuffer(1);
		worldPos = BufferUtils.createFloatBuffer(3);

		mouseInWorld = new FloatPoint();
	}

	public void update()
	{
		int mouseX = Mouse.getX();
		int mouseY = Mouse.getY();

		// get the current modelView matrix, projection matrix and viewport
		GL11.glGetFloat(GL11.GL_MODELVIEW_MATRIX, modelview);
		GL11.glGetFloat(GL11.GL_PROJECTION_MATRIX, projection);
		GL11.glGetInteger(GL11.GL_VIEWPORT, viewport);

		// read the screen depth under the mouse pointer
		GL11.glReadPixels(mouseX, mouseY, 1, 1, GL11.GL_DEPTH_COMPONENT, GL11.GL_FLOAT, screenDepth);

		// transform mouse position to world coordinates
		boolean b = GLU.gluUnProject(mouseX, mouseY, screenDepth.get(0), modelview, projection, viewport, worldPos);

		if (b)
		{
			mouseInWorld.x = worldPos.get(0);
			mouseInWorld.y = worldPos.get(1);
		}
	}

	public IFloatPoint_r getMouseInWorld()
	{
		return mouseInWorld;
	}
}
