package xoric.prism.exceptions;

import xoric.prism.data.modules.ErrorCode;

public class PrismException extends Exception
{
	private static final long serialVersionUID = 1L;

	private StringBuffer stringBuffer;
	private ErrorCode errorCode;

	public PrismException(ErrorCode errorCode)
	{
		this.stringBuffer = new StringBuffer();
		this.errorCode = errorCode;

		appendInfo("module", errorCode.getModuleID().toString());
		appendInfo("actor", errorCode.getActorID().toString());
		appendInfo("error", errorCode.getErrorID().toString());
	}

	public ErrorCode getErrorCode()
	{
		return errorCode;
	}

	public final void appendOriginalException(Exception e)
	{
		appendInfo(e.toString());
	}

	public final void appendInfo(String value)
	{
		if (stringBuffer.length() > 0)
			stringBuffer.append(", ");

		stringBuffer.append(value);
	}

	public final void appendInfo(String key, String value)
	{
		appendInfo(key + "=\"" + value + "\"");
	}

	@Override
	public final String toString()
	{
		return super.toString() + " (0X" + errorCode.toHexString() + "): " + stringBuffer.toString();
	}
}