package xoric.prism.ui.edit;

import java.awt.event.KeyEvent;

import xoric.prism.data.exceptions.PrismException;
import xoric.prism.data.heap.Heap_in;
import xoric.prism.data.heap.Heap_out;
import xoric.prism.data.types.IFloatPoint_r;
import xoric.prism.data.types.IText_r;
import xoric.prism.data.types.PrismColor;
import xoric.prism.data.types.Text;
import xoric.prism.scene.IRendererUI;
import xoric.prism.scene.materials.shaders.AllShaders;
import xoric.prism.scene.materials.tools.AllTools;
import xoric.prism.ui.IActiveUI;
import xoric.prism.ui.UIComponent;
import xoric.prism.ui.UIIdentifier;
import xoric.prism.ui.UITextLine;

/**
 * @author Felix Möhrle
 * @since 22.05.2011, 20:02:27
 */
public class UIEdit extends UIComponent
{
	private static final PrismColor titleColor1 = new PrismColor(200, 200, 200, 150);
	private static final PrismColor titleColor2 = new PrismColor(200, 200, 200, 50);

	private final InputHandler input;
	private final UITextLine titleLine;

	public UIEdit()
	{
		super(UIIdentifier.EDIT);

		registerChild(input = new InputHandler());
		registerChild(titleLine = new UITextLine());
	}

	public int getMaxLength()
	{
		return input.getMaxLength();
	}

	public IText_r getTitle()
	{
		return titleLine.getText();
	}

	public void setInputFormat(InputFormat inputFormat, int maxLength)
	{
		input.setInputFormat(inputFormat, maxLength);
	}

	@Override
	protected Integer importInt()
	{
		return input.getValue();
	}

	@Override
	protected Text importText()
	{
		return input.getText();
	}

	@Override
	public void draw(IRendererUI renderer) throws PrismException
	{
		AllTools.uiDrawer.setup(0, 5, 0);
		AllTools.uiDrawer.drawThreeParts(rect.getTopLeft(), rect.getWidth());

		int n = input.getText().length();

		if (n > 0 || hasFocus)
		{
			input.draw(renderer, hasFocus);

			if (n == 0)
			{
				AllTools.printer.setColor(titleColor2);
				titleLine.draw(renderer);
			}
		}
		else
		{
			AllTools.printer.setColor(titleColor1);
			titleLine.draw(renderer);
		}
		AllShaders.defaultShader.setColor(PrismColor.opaqueWhite); // TODO temp
		AllTools.printer.setColor(PrismColor.opaqueWhite); // TODO temp
	}

	@Override
	public void appendTo(Heap_out h)
	{
		super.appendTo(h);

		titleLine.appendTo(h);
		input.appendTo(h);
	}

	@Override
	public void extractFrom(Heap_in h) throws PrismException
	{
		super.extractFrom(h);

		titleLine.extractFrom(h);
		input.extractFrom(h);
	}

	@Override
	public void mouseClick() throws PrismException
	{
	}

	@Override
	protected IActiveUI mouseDownConfirmed(IFloatPoint_r mouse)
	{
		input.mouseDown(mouse);
		return this;
	}

	@Override
	protected void mouseUpConfirmed()
	{
	}

	@Override
	public void onControlKey(int keyCode, boolean isDown)
	{
		if (isDown)
		{
			//			boolean handled = false;

			if (keyCode == KeyEvent.VK_END || keyCode == KeyEvent.VK_HOME)
			{
				input.keyEndOrHome((keyCode == KeyEvent.VK_END));
				//			handled = true;
			}
			else if (keyCode == KeyEvent.VK_LEFT || keyCode == KeyEvent.VK_RIGHT)
			{
				input.handleArrowKey(keyCode);
				//			handled = true;
			}
			else if (keyCode == KeyEvent.VK_SHIFT)
			{
				input.setShiftState(true);
				//			handled = true;
			}
			else if (keyCode == KeyEvent.VK_CONTROL)
			{
				input.setControlState(true);
				//			handled = true;
			}
			else if (keyCode == 130)
			{
				input.handleKeyChar('^');
			}
			else
			{
				//			char keyChar = e.getKeyChar();
				char c = (char) keyCode;
				input.handleKeyChar(c);
			}
			//		return handled;
		}
		else
		{
			if (keyCode == KeyEvent.VK_SHIFT)
				input.setShiftState(false);
			else if (keyCode == KeyEvent.VK_CONTROL)
				input.setControlState(false);
		}
	}

	@Override
	public void onCharacterKey(char c, boolean isDown)
	{
		if (isDown)
			input.handleKeyChar(c);
	}

	public void setTitle(Text t)
	{
		titleLine.setText(t);
	}

	public InputFormat getInputFormat()
	{
		return input.getInputFormat();
	}
}
