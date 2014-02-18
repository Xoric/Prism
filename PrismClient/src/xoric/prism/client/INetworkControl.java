package xoric.prism.client;

/**
 * @author XoricLee
 * @since 18.02.2014, 18:12:53
 */
public interface INetworkControl
{
	public void requestLoginScreen();

	public void onNetworkException(Exception e);
}
