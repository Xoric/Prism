package xoric.prism.creator.windows.view;

import xoric.prism.creator.windows.model.WindowModel;
import xoric.prism.creator.windows.scene.SceneAction;
import xoric.prism.data.types.IFloatPoint_r;
import xoric.prism.data.types.IFloatRect_r;

public interface IMainView
{
	public void setModel(WindowModel model);

	public void startScene();

	public void displayTree();

	public IFloatPoint_r getScreenSize();

	public IFloatRect_r getScreenRect();

	public void closeScene();

	public void setSceneAction(SceneAction a);
}
