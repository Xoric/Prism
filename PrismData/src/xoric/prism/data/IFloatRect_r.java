package xoric.prism.data;

public interface IFloatRect_r extends IStackable_r
{
	public IFloatPoint_r getTopLeft();

	public IFloatPoint_r getSize();

	public FloatPoint calcBottomRight();
}