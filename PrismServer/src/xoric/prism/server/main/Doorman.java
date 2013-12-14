package xoric.prism.server.main;

import xoric.prism.data.modules.ActorID;
import xoric.prism.data.modules.ErrorCode;
import xoric.prism.data.modules.ErrorID;
import xoric.prism.data.modules.IActor;
import xoric.prism.exceptions.PrismException;

public class Doorman implements IActor
{
	public static void main(String args[])
	{
		Doorman d = new Doorman();
		try
		{
			ErrorCode c = new ErrorCode(d, ErrorID.INVALID_LOGIN);
			PrismException e = new PrismException(c);
			throw e;
		}
		catch (PrismException e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();

			int n = 0;
			StackTraceElement[] t = e.getStackTrace();
			for (StackTraceElement s : t)
			{
				System.out.println("[" + (n++) + "] " + s.toString());

			}
		}
	}

	@Override
	public ActorID getActorID()
	{
		return ActorID.DOORMAN;
	}
}
