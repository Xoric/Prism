package xoric.prism.client.net;

import xoric.prism.com.Message_out;
import xoric.prism.data.exceptions.PrismException;

/**
 * @author XoricLee
 * @since 18.02.2014, 12:57:24
 */
public interface INetwork
{
	public void send(Message_out m) throws PrismException;
}
