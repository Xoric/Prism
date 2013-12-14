package xoric.prism.exceptions;

import java.io.File;

import xoric.prism.data.modules.ErrorCode;

public class PrismMetaFileException extends PrismException
{
	private static final long serialVersionUID = 1L;

	public PrismMetaFileException(ErrorCode errorCode, File file)
	{
		super(errorCode);
		appendInfo("file", file.toString());
	}
}
