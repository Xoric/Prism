package xoric.prism.creator.drawer.control;

import java.awt.Image;
import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.Transferable;
import java.awt.datatransfer.UnsupportedFlavorException;

class ImageTransfer implements Transferable
{
	private final Image image;

	public ImageTransfer(Image image)
	{
		this.image = image;
	}

	@Override
	public Object getTransferData(DataFlavor flavor) throws UnsupportedFlavorException
	{
		if (isDataFlavorSupported(flavor))
			return image;
		else
			throw new UnsupportedFlavorException(flavor);
	}

	@Override
	public boolean isDataFlavorSupported(DataFlavor flavor)
	{
		return flavor == DataFlavor.imageFlavor;
	}

	@Override
	public DataFlavor[] getTransferDataFlavors()
	{
		return new DataFlavor[] { DataFlavor.imageFlavor };
	}
}