package xoric.prism.scene;

import xoric.prism.data.FloatPoint;
import xoric.prism.data.IFloatPoint_r;

public interface ICameraTransform
{
	/**
	 * Transforms a given point within the map<br>
	 * <br>
	 * {@code x_in: [0; width]}<br>
	 * {@code y_in: [0; height]}<br>
	 * <br>
	 * to the visible area within camera bounds<br>
	 * <br>
	 * {@code x_out: [0.0f; 1.0f]}<br>
	 * {@code y_out: [0.0f; 1.0f]}.
	 * @param in
	 *            point in map coordinates
	 * @param out
	 *            point within visible area
	 */
	public void transformWithCameraBounds(IFloatPoint_r in, FloatPoint out);
}