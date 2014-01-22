package xoric.prism.com;

public enum Token
{
	LOGIN;

	static
	{
		LOGIN.enableForClient(1, 0, 1);
	}

	private static Token[] VALUES = values();

	private Minima clientMinima;
	private Minima serverMinima;
	private int floatDecimals;

	private void enableForClient(int intMin, int floatMin, int textMin)
	{
		clientMinima = new Minima(intMin, floatMin, textMin);
	}

	private void enableForServer(int intMin, int floatMin, int textMin)
	{
		serverMinima = new Minima(intMin, floatMin, textMin);
	}

	private void setFloatDecimals(int floatDecimals)
	{
		this.floatDecimals = floatDecimals;
	}

	public Minima getClientMinima()
	{
		return clientMinima;
	}

	public Minima getServerMinima()
	{
		return serverMinima;
	}

	public int getFloatDecimals()
	{
		return floatDecimals;
	}

	public static Token valueOf(int index)
	{
		return VALUES[index];
	}
}
