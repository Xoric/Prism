package xoric.prism.data.physics;

public enum View6
{
	RIGHT, TOP_RIGHT, TOP_LEFT, LEFT, BOTTOM_LEFT, BOTTOM_RIGHT;

	public View4 getView(boolean isBidirectional)
	{
		View4 view;
		switch (this)
		{
			case RIGHT:
				view = View4.RIGHT;
				break;
			case TOP_RIGHT:
				view = isBidirectional ? View4.RIGHT : View4.TOP;
				break;
			case TOP_LEFT:
				view = isBidirectional ? View4.LEFT : View4.TOP;
				break;
			case LEFT:
				view = View4.LEFT;
				break;
			case BOTTOM_LEFT:
				view = isBidirectional ? View4.LEFT : View4.BOTTOM;
				break;
			default:
				view = isBidirectional ? View4.RIGHT : View4.BOTTOM;
		}
		return view;
	}
}
