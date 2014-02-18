package xoric.prism.client.ui.hints;

import xoric.prism.data.exceptions.PrismException;
import xoric.prism.data.types.FloatPoint;
import xoric.prism.data.types.IFloatPoint_r;
import xoric.prism.data.types.IText_r;
import xoric.prism.data.types.Text;
import xoric.prism.scene.IRendererUI;
import xoric.prism.scene.materials.Materials;

/**
 * @author XoricLee
 * @since 18.02.2014, 18:28:44
 */
public class ConnectionHint extends Hint
{
	private final IText_r[] texts;
	private final FloatPoint position;
	private int age;
	private int textIndex;

	public ConnectionHint()
	{
		texts = new Text[4];
		texts[0] = new Text("Connecting to server");
		texts[1] = new Text("Connecting to server.");
		texts[2] = new Text("Connecting to server..");
		texts[3] = new Text("Connecting to server...");

		position = new FloatPoint(30.0f, 30.0f);
	}

	public void setScreenSize(IFloatPoint_r screenSize)
	{
		position.y = screenSize.getY() - 60.0f;
	}

	@Override
	public void draw(IRendererUI renderer) throws PrismException
	{
		IText_r t = texts[textIndex];

		Materials.printer.setText(t);
		Materials.printer.print(position);
	}

	@Override
	public boolean update(int passedMs)
	{
		age += passedMs;
		textIndex = (age / 800) % texts.length;
		return true;
	}
}
