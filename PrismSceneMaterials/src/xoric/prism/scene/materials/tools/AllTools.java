package xoric.prism.scene.materials.tools;

import xoric.prism.data.exceptions.PrismException;
import xoric.prism.data.global.FileTableDirectoryIndex;
import xoric.prism.data.global.Prism;
import xoric.prism.data.global.UIIndex;
import xoric.prism.scene.IRendererUI;
import xoric.prism.scene.textures.collections.CollectionArt;
import xoric.prism.scene.textures.grid.GridArt;

/**
 * @author XoricLee
 * @since 25.02.2014, 13:14:22
 */
public abstract class AllTools
{
	public static Printer printer;
	public static Drawer uiDrawer;

	public static void loadAll(IRendererUI renderer, CollectionArt frames, GridArt font) throws PrismException
	{
		uiDrawer = new Drawer(frames);

		printer = new Printer(font);
		printer.load(Prism.global.loadMetaFile(FileTableDirectoryIndex.UI, UIIndex.FONT.ordinal()).getMetaList());

		Drawer.renderer = renderer;
		Printer.renderer = renderer;
	}
}
