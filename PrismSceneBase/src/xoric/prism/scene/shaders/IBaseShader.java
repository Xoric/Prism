package xoric.prism.scene.shaders;

/**
 * @author XoricLee
 * @since 25.02.2014, 00:48:52
 */
public interface IBaseShader
{
	/**
	 * Activates this shader object and resets certain uniform variables.
	 */
	public void activate();

	/**
	 * Initializes uniform locations.
	 */
	public void initialize();
}
