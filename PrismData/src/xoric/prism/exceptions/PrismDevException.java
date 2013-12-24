package xoric.prism.exceptions;

import xoric.prism.data.modules.ErrorCode;

public class PrismDevException extends PrismException
{
	private static final long serialVersionUID = 1L;

	public PrismDevException(ErrorCode errorCode)
	{
		super(errorCode);
	}
}