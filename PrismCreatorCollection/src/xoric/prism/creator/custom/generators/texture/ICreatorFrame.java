package xoric.prism.creator.custom.generators.texture;

public interface ICreatorFrame
{
	public void showFrame();

	public void closeFrame();

	public void setAction(String action);

	public void setProgressMax(int n);

	public void setProgress(int p);

	public void setChapterMax(int n);

	//	public void setChapter(int p);

	public void increaseChapter();
}
