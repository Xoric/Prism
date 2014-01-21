import java.net.Socket;

import xoric.prism.data.net.NetConstants;

public abstract class Test
{
	public static void main(String[] args)
	{
		try
		{
			Socket s = new Socket("127.0.0.1", NetConstants.port);
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
	}
}
