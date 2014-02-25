package xoric.prism.world.map;

import xoric.prism.data.types.IPoint_r;
import xoric.prism.world.map.routing.RoutingTile;
import xoric.prism.world.weather.WeatherFader;

/**
 * @author XoricLee
 * @since 18.02.2014, 22:47:20
 */
@Deprecated
public class WeatherTile extends RoutingTile
{
	protected final WeatherFader weatherFader;

	public WeatherTile(IPoint_r coords, Ground ground)
	{
		super(coords, ground);

		weatherFader = new WeatherFader();
	}

	public WeatherFader getWeatherFader()
	{
		return weatherFader;
	}
}
