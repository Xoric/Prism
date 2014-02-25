package xoric.prism.scene.materials.art;

import xoric.prism.data.exceptions.PrismException;
import xoric.prism.data.global.FileTableDirectoryIndex;
import xoric.prism.data.global.Prism;
import xoric.prism.data.global.UIIndex;
import xoric.prism.data.global.WorldIndex;
import xoric.prism.scene.textures.Art;
import xoric.prism.scene.textures.ITextureBinder;
import xoric.prism.scene.textures.collections.CollectionArt;
import xoric.prism.scene.textures.grid.GridArt;

/**
 * @author XoricLee
 * @since 25.02.2014, 13:16:27
 */
public abstract class AllArt
{
	public static CollectionArt frames;
	public static GridArt mark0;
	public static GridArt masks;
	public static CollectionArt mush0;
	public static GridArt env0;
	public static GridArt font;

	public static void loadAll(ITextureBinder textureBinder) throws PrismException
	{
		Art.textureBinder = textureBinder;

		frames = new CollectionArt(Prism.global.loadMetaFile(FileTableDirectoryIndex.UI, UIIndex.FRAMES.ordinal()));
		frames.load();

		mark0 = new GridArt(Prism.global.loadMetaFile(FileTableDirectoryIndex.WORLD, WorldIndex.MARK0.ordinal()));
		mark0.load();

		masks = new GridArt(Prism.global.loadMetaFile(FileTableDirectoryIndex.WORLD, WorldIndex.MASKS.ordinal()));
		masks.load();

		mush0 = new CollectionArt(Prism.global.loadMetaFile(FileTableDirectoryIndex.WORLD, WorldIndex.MUSH0.ordinal()));
		mush0.load();

		env0 = new GridArt(Prism.global.loadMetaFile(FileTableDirectoryIndex.WORLD, WorldIndex.ENV0.ordinal()));
		env0.load();

		font = new GridArt(Prism.global.loadMetaFile(FileTableDirectoryIndex.UI, UIIndex.FONT.ordinal()));
		font.load();
	}
}
