package xoric.prism.scene;

import xoric.prism.data.exceptions.PrismException;
import xoric.prism.scene.shaders.AllShaders;

public abstract class PrismSceneLoader
{
	public static void loadAll(IScene scene) throws PrismException
	{
		AllShaders.load(scene);
	}
}
