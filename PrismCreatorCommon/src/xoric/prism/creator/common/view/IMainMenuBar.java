package xoric.prism.creator.common.view;

import java.awt.Component;

import javax.swing.JMenu;
import javax.swing.JMenuItem;

import xoric.prism.creator.common.control.IMainMenuListener;

public interface IMainMenuBar
{
	public void setDialogCreator(INewDialogCreator dialogCreator);

	public void setMainMenuListener(IMainMenuListener listener);

	public void appendAboutHtmlLine(String line);

	public void appendAboutComponent(Component c);

	public void addCreationItem(JMenuItem item);

	public void setModelObjectIsNull(boolean isModelObjectNull);

	public void addMenu(JMenu menu);
}
