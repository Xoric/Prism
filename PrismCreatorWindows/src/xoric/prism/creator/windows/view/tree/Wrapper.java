package xoric.prism.creator.windows.view.tree;

import javax.swing.tree.DefaultMutableTreeNode;

public abstract class Wrapper
{
	public static ITree tree;

	protected final DefaultMutableTreeNode node;
	private final String name;

	public Wrapper(DefaultMutableTreeNode node, String name)
	{
		this.node = node;
		this.name = name;
	}

	public void input()
	{
		showDialog();

		if (tree != null)
			tree.onModifiedValue(node);
	}

	protected abstract void showDialog();

	protected abstract String getValue();

	@Override
	public final String toString()
	{
		return name + ": " + getValue();
	}
}
