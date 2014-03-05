package xoric.prism.scene.materials;

import xoric.prism.data.exceptions.PrismException;
import xoric.prism.scene.art.ITextureBinder;
import xoric.prism.scene.materials.art.AllArt;
import xoric.prism.scene.materials.shaders.AllShaders;
import xoric.prism.scene.materials.tools.AllTools;
import xoric.prism.scene.renderer.IUIRenderer2;
import xoric.prism.scene.shaders.IShaderIO;

public class Materials
{
	public synchronized static void loadAll(IUIRenderer2 renderer, ITextureBinder textureBinder, IShaderIO shaderIO) throws PrismException
	{
		AllArt.loadAll(textureBinder);
		AllShaders.loadAll(shaderIO);
		AllTools.loadAll(renderer, AllArt.frames, AllArt.font);
		//		AllBuffers.loadAll(frameBufferIO);
	}
}
