package xoric.prism.scene.materials;

import xoric.prism.data.exceptions.PrismException;
import xoric.prism.scene.IRendererUI;
import xoric.prism.scene.fbo.IFrameBufferIO;
import xoric.prism.scene.materials.art.AllArt;
import xoric.prism.scene.materials.fbo.AllBuffers;
import xoric.prism.scene.materials.shaders.AllShaders;
import xoric.prism.scene.materials.tools.AllTools;
import xoric.prism.scene.shaders.IShaderIO;
import xoric.prism.scene.textures.ITextureBinder;

public class Materials
{
	public static void loadAll(IRendererUI renderer, ITextureBinder textureBinder, IShaderIO shaderIO, IFrameBufferIO frameBufferIO)
			throws PrismException
	{
		AllArt.loadAll(textureBinder);
		AllShaders.loadAll(shaderIO);
		AllTools.loadAll(renderer, AllArt.frames, AllArt.font);
		AllBuffers.loadAll(frameBufferIO);
	}
}
