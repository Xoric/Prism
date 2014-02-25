package xoric.prism.scene;

import xoric.prism.data.types.FloatPoint;
import xoric.prism.data.types.FloatRect;
import xoric.prism.data.types.IFloatPoint_r;
import xoric.prism.data.types.IFloatRect_r;

public interface ICameraTransform
{
	/**
	 * Transforms a given point within the map<br>
	 * <br>
	 * {@code x_in: [camera.left; camera.right]}<br>
	 * {@code y_in: [camera.top; camera.bottom]}<br>
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

	/**
	 * Transforms a given rectangle within the map<br>
	 * <br>
	 * {@code x_in: [camera.left; camera.right]}<br>
	 * {@code y_in: [camera.top; camera.bottom]}<br>
	 * {@code width_in: [0; camera.width]}<br>
	 * {@code height_in: [0; camera.height]}<br>
	 * <br>
	 * to the visible area within camera bounds<br>
	 * <br>
	 * {@code x_out: [0.0f; 1.0f]}<br>
	 * {@code y_out: [0.0f; 1.0f]}.<br>
	 * {@code width_out: [0.0f; 1.0f]}<br>
	 * {@code height_out: [0.0f; 1.0f]}.
	 * @param in
	 *            rectangle in map coordinates
	 * @param out
	 *            rectangle within visible area
	 */
	public void transformWithCameraBounds(IFloatRect_r in, FloatRect out);

	public void transformFrameFractionToWorld(IFloatPoint_r in, FloatPoint out);
}
