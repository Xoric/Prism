package xoric.prism.data.physics;

import xoric.prism.data.types.IFloatPoint_r;

public interface IAngle_r
{
	public View4 getView(boolean bidirectional);

	public View6 getViewInfo();

	public int getDegree();

	public IFloatPoint_r getComponents();
}
