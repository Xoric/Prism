package xoric.prism.scene.exceptions;

import xoric.prism.data.modules.ErrorCode;
import xoric.prism.exceptions.PrismException;

public class PrismShaderException extends PrismException
{
	private static final long serialVersionUID = 1L;

	public PrismShaderException(ErrorCode errorCode)
	{
		super(errorCode);
	}
}
