package xoric.prism.world.map2;

import xoric.prism.data.types.FloatRect;
import xoric.prism.data.types.IFloatRect_r;
import xoric.prism.data.types.IPoint_r;
import xoric.prism.data.types.Point;
import xoric.prism.world.weather.WeatherFader;

/**
 * @author XoricLee
 * @since 23.02.2014, 22:02:59
 */
public class Ground2
{
	public static final int WIDTH = 120;
	public static final int HEIGHT = 80;

	protected final WeatherFader weatherFader;
	protected final IPoint_r coordinates;
	protected final IFloatRect_r rect;

	private GroundType2 groundType;

	public Ground2(GroundType2 groundType, Point coordinates)
	{
		this.groundType = groundType;
		this.weatherFader = new WeatherFader();

		this.coordinates = coordinates;
		this.rect = new FloatRect(coordinates.x * WIDTH, coordinates.y * HEIGHT, WIDTH, HEIGHT);
	}

	public WeatherFader getWeatherFader()
	{
		return weatherFader;
	}

	public GroundType2 getGroundType()
	{
		return groundType;
	}

	public IPoint_r getCoordinates()
	{
		return coordinates;
	}

	public IFloatRect_r getRect()
	{
		return rect;
	}
}
