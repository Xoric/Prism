package xoric.prism.creator.custom.creators;

public interface ICreatorFrame
{
	public void showFrame();

	public void closeFrame();

	public void setAction(String action);

	public void setProgressMax(int n);

	public void setProgress(int p);
}