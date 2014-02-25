package xoric.prism.world.weather;

/**
 * @author XoricLee
 * @since 18.02.2014, 22:47:20
 */
public class WeatherFader
{
	private static final int MAX = 300;
	private static final int MIN = 0;

	// 0: 	snow
	// 100:	rain
	// 200:	normal
	// 300: burned
	private int weather;

	// TODO: server does only need heat field - split class
	private Weather lower;
	private Weather upper;
	private float upperFactor;

	private IWeatherFaderListener listener;

	public WeatherFader()
	{
		setWeather(200);
	}

	public int getWeather()
	{
		return weather;
	}

	public void setWeather(int weather)
	{
		if (weather < MIN)
			weather = MIN;
		else if (weather > MAX)
			weather = MAX;

		this.weather = weather;

		update();
	}

	private void update()
	{
		if (weather < 100)
		{
			lower = Weather.SNOW;
			upper = Weather.RAIN;
		}
		else if (weather < 200)
		{
			lower = Weather.RAIN;
			upper = Weather.NORMAL;
		}
		else if (weather == 300)
		{
			lower = Weather.BURNED;
			upper = Weather.BURNED;
		}
		else
		{
			lower = Weather.NORMAL;
			upper = Weather.BURNED;
		}
		upperFactor = (weather % 100) / 100.0f;

		if (listener != null)
			listener.onWeatherChanged();
	}

	public Weather getLower()
	{
		return lower;
	}

	public Weather getUpper()
	{
		return upper;
	}

	public float getUpperFactor()
	{
		return upperFactor;
	}

	public void balance()
	{
		if (weather > 200)
		{
			weather--;
			update();
		}
		else if (weather < 200)
		{
			weather++;
			update();
		}
	}

	public void applyHeat(int steps)
	{
		if (weather < MAX)
		{
			weather += steps + Math.random() * 3;

			if (weather > MAX)
				weather = MAX;

			update();
		}
	}

	public void applyRain(int steps)
	{
		if (weather > 100)
		{
			weather -= steps + Math.random() * 3;
			if (weather < 100)
				weather = 100;
			update();
		}
		else if (weather < 100)
		{
			weather += steps + Math.random() * 3;
			if (weather > 100)
				weather = 100;
			update();
		}
	}

	public void applySnow(int steps)
	{
		if (weather > MIN)
		{
			weather -= steps + Math.random() * 3;

			if (weather < MIN)
				weather = MIN;

			update();
		}
	}

	public void setListener(IWeatherFaderListener listener)
	{
		this.listener = listener;
	}
}
