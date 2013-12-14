package xoric.prism.server.main;

import xoric.prism.data.modules.ActorID;
import xoric.prism.data.modules.ErrorCode;
import xoric.prism.data.modules.ErrorID;
import xoric.prism.data.modules.IActor;
import xoric.prism.data.modules.ModuleID;
import xoric.prism.data.modules.PrismException;

public class Doorman implements IActor
{
	@Override
	public ErrorCode getErrorCode(ErrorID errorID)
	{
		return new ErrorCode(ModuleID.SERVER, ActorID.DOORMAN, errorID);
	}

	@Override
	public void throwException(ErrorID errorID, String info) throws PrismException
	{
		throw new PrismException(getErrorCode(errorID), info);
	}

	public static void main(String args[])
	{
		Doorman d = new Doorman();
		try
		{
			d.throwException(ErrorID.INVALID_LOGIN, "Test");
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
}
