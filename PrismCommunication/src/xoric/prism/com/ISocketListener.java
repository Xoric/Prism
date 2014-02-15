package xoric.prism.com;

public interface ISocketListener
{
	public void onSocketListenerCrashing(Exception e);

	public void receiveMessage(MessageBase m);
}
