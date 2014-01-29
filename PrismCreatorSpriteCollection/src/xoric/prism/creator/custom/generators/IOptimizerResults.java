package xoric.prism.creator.custom.generators;

import java.awt.image.BufferedImage;

import xoric.prism.data.types.IPoint_r;
import xoric.prism.data.types.Point;

import com.ryanm.droid.rugl.util.RectanglePacker;

interface IOptimizerResults
{
	public boolean wasSuccessful();

	public IPoint_r getOriginalSize();

	public Point getActualSize();

	public boolean isBetterThan(IOptimizerResults other);

	public RectanglePacker<BufferedImage> getPacker();
}
