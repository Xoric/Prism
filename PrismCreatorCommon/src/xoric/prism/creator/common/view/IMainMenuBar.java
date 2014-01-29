package xoric.prism.creator.common.view;

import javax.swing.JMenuItem;

import xoric.prism.creator.common.control.IMainMenuListener;

public interface IMainMenuBar
{
	public void setDialogCreator(INewDialogCreator dialogCreator);

	public void setMainMenuListener(IMainMenuListener listener);

	public void appendHtmlLine(String line);

	public void addCreationItem(JMenuItem item);

	public void setModelObjectIsNull(boolean isModelObjectNull);
}
