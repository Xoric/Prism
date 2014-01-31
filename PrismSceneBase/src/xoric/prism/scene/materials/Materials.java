package xoric.prism.scene.materials;

import xoric.prism.data.exceptions.PrismException;
import xoric.prism.data.global.FileTableDirectoryIndex;
import xoric.prism.data.global.Prism;
import xoric.prism.data.global.UIIndex;
import xoric.prism.scene.textures.Art;
import xoric.prism.scene.textures.ITextureBinder;
import xoric.prism.scene.textures.collections.CollectionArt;

public class Materials
{
	public static CollectionArt frames;
	public static Drawer framesDrawer;

	public static void load(ITextureBinder textureBinder) throws PrismException
	{
		Art.textureBinder = textureBinder;

		frames = new CollectionArt(Prism.global.loadMetaFile(FileTableDirectoryIndex.UI, UIIndex.FRAMES.ordinal()));
		frames.load();
		framesDrawer = new Drawer(frames);
	}
}
