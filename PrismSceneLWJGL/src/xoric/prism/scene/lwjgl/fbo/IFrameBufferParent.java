package xoric.prism.scene.lwjgl.fbo;

import xoric.prism.scene.IRendererUI;

/**
 * @author XoricLee
 * @since 25.02.2014, 12:14:46
 */
public interface IFrameBufferParent extends IRendererUI
{
	public void setStage(boolean isWorldStage);

	public void resetOrthoExperimental();
}
