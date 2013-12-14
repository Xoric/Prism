package xoric.prism.data;

public interface IRect_r extends IPoint_r
{
	public IPoint_r getPosition();

	public IPoint_r getSize();

	public int getWidth();

	public int getHeight();

	public int calcX2();

	public int calcY2();
}
