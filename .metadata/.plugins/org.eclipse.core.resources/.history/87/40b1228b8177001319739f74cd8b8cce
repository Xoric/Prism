package xoric.prism.data.exceptions;

import xoric.prism.data.modules.ErrorCode;

public class PrismMetaFileException extends PrismException
{
	private static final long serialVersionUID = 1L;

	public PrismMetaFileException(ErrorCode errorCode, String filename)
	{
		super(errorCode);
		appendInfo("file", filename);
	}
}
