package xoric.prism.data.exceptions;

import xoric.prism.data.modules.ErrorCode_;

@Deprecated
public class PrismMetaFileException_ extends PrismException_
{
	private static final long serialVersionUID = 1L;

	public PrismMetaFileException_(ErrorCode_ errorCode, String filename)
	{
		super(errorCode);
		appendInfo("file", filename);
	}
}
