package xoric.prism.server.view;

public interface IServerView
{
	public void printWelcome();

	public void displayAll();

	public INetView getNetView();
}
