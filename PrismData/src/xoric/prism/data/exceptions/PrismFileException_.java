package xoric.prism.data.exceptions;

import java.io.File;

import xoric.prism.data.modules.ErrorCode_;

@Deprecated
public class PrismFileException_ extends PrismException_
{
	private static final long serialVersionUID = 1L;

	public PrismFileException_(ErrorCode_ errorCode, File file)
	{
		super(errorCode);
		appendInfo("file", file.toString());
	}
}
