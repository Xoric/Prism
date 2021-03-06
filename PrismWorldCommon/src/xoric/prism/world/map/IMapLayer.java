package xoric.prism.world.map;

public interface IMapLayer
{
	/**
	 * Resizes the Map to the given number of tiles in x- and y-direction.
	 * @param tilesX
	 * @param tilesY
	 */
	public void setSize(int tilesX, int tilesY);
}
