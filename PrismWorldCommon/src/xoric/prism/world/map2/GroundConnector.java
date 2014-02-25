package xoric.prism.world.map2;

import xoric.prism.data.exceptions.PrismException;
import xoric.prism.data.exceptions.UserErrorText;

/**
 * @author XoricLee
 * @since 24.02.2014, 20:41:45
 */
public enum GroundConnector
{
	/* Edges */
	/* A   B */
	/* C   D */
	// --
	NONE,
	// --
	TOP,
	LEFT,
	TOP_LEFT,
	// --
	LINE_HORZ,
	LINE_VERT,
	// --
	SPIT_TOP,
	SPIT_LEFT,
	// --
	ISLE,
	// --
	EDGE_A,
	EDGE_AB,
	EDGE_AC,
	EDGE_AD,
	EDGE_ABD,
	EDGE_ABCD,
	// --
	TOP_C,
	TOP_CD,
	LEFT_B,
	LEFT_BD,
	TOP_LEFT_D;
	// --

	public static final GroundConnector[] VALUES = values();

	public static GroundConnector valueOf(int v) throws PrismException
	{
		if (v < 0 || v >= VALUES.length)
		{
			PrismException e = new PrismException();
			e.user.setText(UserErrorText.INTERNAL_PROBLEM);
			e.code.setText("invalid conversion from int " + v + " to " + GroundConnector.class.getSimpleName());
			e.code.addInfo("enum count", VALUES.length);
			throw e;
		}
		return VALUES[v];
	}
}
