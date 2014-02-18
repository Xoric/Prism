package xoric.prism.client.ui.edit;

import java.awt.event.KeyEvent;

import xoric.prism.client.ui.IActiveUI;
import xoric.prism.client.ui.UIComponent;
import xoric.prism.client.ui.UIIdentifier;
import xoric.prism.client.ui.UITextLine;
import xoric.prism.data.exceptions.PrismException;
import xoric.prism.data.heap.Heap_in;
import xoric.prism.data.heap.Heap_out;
import xoric.prism.data.types.IFloatPoint_r;
import xoric.prism.data.types.IText_r;
import xoric.prism.data.types.PrismColor;
import xoric.prism.data.types.Text;
import xoric.prism.scene.IRendererUI;
import xoric.prism.scene.materials.Materials;

/**
 * @author Felix Möhrle
 * @since 22.05.2011, 20:02:27
 */
public class UIEdit extends UIComponent
{
	private static final PrismColor titleColor = new PrismColor(200, 200, 200, 150);

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

	@Deprecated
	public void setMaxLength(int max)
	{
		input.setMaxLength(max);
	}

	public IText_r getTitle()
	{
		return titleLine.getText();
	}

	@Deprecated
	public void setInputFormat(InputFormat inputFormat)
	{
		input.setInputFormat(inputFormat, 0);
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

	//	@Override
	//	public boolean hasTextInput()
	//	{
	//		return input.getInputFormat() != InputFormat.NUMBERS;
	//	}
	//
	//	@Override
	//	public boolean hasIntInput()
	//	{
	//		return input.getInputFormat() == InputFormat.NUMBERS;
	//	}

	@Override
	public void draw(IRendererUI renderer) throws PrismException
	{
		Materials.framesDrawer.setup(0, 5, 0);
		Materials.framesDrawer.drawThreeParts(rect.getTopLeft(), rect.getWidth());

		if (input.getText().length() > 0 || hasFocus)
		{
			input.draw(renderer, hasFocus);
		}
		else
		{
			//			Materials.printer.setText(temp);
			Materials.printer.setColor(titleColor);
			//			Materials.printer.print(rect.getTopLeft());
			titleLine.draw(renderer);
			Materials.printer.resetColor();
		}
	}

	//	@Override
	//	public void draw(Renderer renderer) throws IOException, MetaFileException
	//	{
	//		AllTextures.frameTexture.drawButton(renderer, 3, 0, rect.getPositionR(), rect.getWidth());
	//
	//		if ((input.toString().length() > 0) || isActive)
	//			input.draw(renderer, isActive);
	//		else
	//		{
	//			Printer.pushColor(TITLE_COLOR);
	//			Printer.printCentered(renderer, rect, text.toString(), FontCenter.BOTH);
	//		}
	//	}

	//	@Override
	//	public void resized()
	//	{
	//		super.resized();
	//		rect.getSizeW().setY(TriComponent.STD_HEIGHT);
	//		FloatRectW inputRect = new FloatRectW(rect);
	//		inputRect.getPositionW().addX(IDENT);
	//		inputRect.getSizeW().addX(IDENT * -2.0f);
	//		input.setRect(inputRect);
	//	}

	//	@Override
	//	public boolean mouseDown(IFloatPoint_r mouse)
	//	{
	//		if (rect.contains(mouse))
	//		{
	//			// receiveFocus(); // should be invoked by Page
	//			input.mouseDown(mouse);
	//			return true;
	//		}
	//		return false;
	//	}

	//	@Override
	//	public boolean handleKeyEvent(KeyEvent e)
	//	{
	//		boolean handled = false;
	//		int keyCode = e.getKeyCode();
	//
	//		if (e.isActionKey())
	//		{
	//			if ((keyCode == KeyEvent.VK_END) || (keyCode == KeyEvent.VK_HOME))
	//			{
	//				input.keyEndOrHome((keyCode == KeyEvent.VK_END));
	//				handled = true;
	//			}
	//			else if (keyCode == KeyEvent.VK_LEFT || keyCode == KeyEvent.VK_RIGHT)
	//			{
	//				input.handleArrowKey(keyCode);
	//				handled = true;
	//			}
	//		}
	//		else
	//		{
	//			if (keyCode == KeyEvent.VK_SHIFT)
	//			{
	//				input.setShiftState(true);
	//				handled = true;
	//			}
	//			else if (keyCode == KeyEvent.VK_CONTROL)
	//			{
	//				input.setControlState(true);
	//				handled = true;
	//			}
	//			else if (keyCode == 130)
	//			{
	//				input.handleKeyChar('^');
	//			}
	//			else
	//			{
	//				char keyChar = e.getKeyChar();
	//				input.handleKeyChar(keyChar);
	//			}
	//		}
	//		return handled;
	//	}

	//	@Override
	//	public void keyReleased(int keyCode)
	//	{
	//		if (keyCode == KeyEvent.VK_SHIFT)
	//			input.setShiftState(false);
	//		else if (keyCode == KeyEvent.VK_CONTROL)
	//			input.setControlState(false);
	//	}

	//	@Override
	//	public String toString()
	//	{
	//		return "Edit '" + text + "'";
	//	}

	//	@Override
	//	public Text getParam()
	//	{
	//		return input.getText();
	//	}

	//	@Override
	//	public Heap createHeap() throws HeapException
	//	{
	//		Heap heap = super.createHeap();
	//
	//		// add edit meta
	//		heap.getInts().addItem(input.getMaxLength());
	//		heap.getStrings().addItem(input.getInputFormat().toString());
	//
	//		return heap;
	//	}

	/* --------------------------------------------------------------------------------------------------------------------------------*/

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
