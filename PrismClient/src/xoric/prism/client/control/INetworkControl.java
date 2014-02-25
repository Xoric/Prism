package xoric.prism.client.control;

import xoric.prism.data.exceptions.IExceptionSink;

/**
 * @author XoricLee
 * @since 18.02.2014, 18:12:53
 */
public interface INetworkControl extends IExceptionSink
{
	public void requestLoginScreen();

	//	public void onNetworkException(Exception e);
}
