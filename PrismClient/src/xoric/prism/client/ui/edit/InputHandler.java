package xoric.prism.client.ui.edit;

import java.awt.Toolkit;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.StringSelection;
import java.awt.datatransfer.Transferable;
import java.awt.event.KeyEvent;

import xoric.prism.client.ui.IUISubcomponent;
import xoric.prism.data.exceptions.PrismException;
import xoric.prism.data.heap.Heap_in;
import xoric.prism.data.heap.Heap_out;
import xoric.prism.data.heap.IStackable;
import xoric.prism.data.types.FloatPoint;
import xoric.prism.data.types.FloatRect;
import xoric.prism.data.types.IFloatPoint_r;
import xoric.prism.data.types.IFloatRect_r;
import xoric.prism.data.types.IText_r;
import xoric.prism.data.types.PrismColor;
import xoric.prism.data.types.Text;
import xoric.prism.data.types.TextMap;
import xoric.prism.data.types.Word;
import xoric.prism.scene.IRendererUI;
import xoric.prism.scene.materials.Materials;

/**
 * @author Felix Möhrle
 * @since 22.05.2011, 20:10:40
 */
public class InputHandler implements IUISubcomponent, IStackable
{
	private static final IText_r cursorText = new Text("^");
	private static final IText_r markLeft = new Text("<");
	private static final IText_r markRight = new Text(">");

	private static final float HALF = 0.5f;
	//	private static final float MAJOR_FAC = 0.95f;
	private static final float ALIGN = 0.5f;
	private static final char PASSWORD_CHAR = '*';

	private static final int COPY = 3;
	private static final int PASTE = 22;
	private static final int CUT = 24;

	private final Text text;
	private final Text passwordText;

	private final Word displayLeft = new Word(0, 0);
	private final Word displayMiddle = new Word(0, 0);
	private final Word displayRight = new Word(0, 0);

	private float xOnset;

	private boolean cursorRight;
	private boolean shiftState;
	private boolean controlState;

	private InputFormat inputFormat;
	private int maxLength;
	private FormattedInput input;

	private FloatRect rect;

	private boolean selectionChanged;
	private boolean inputChanged;

	private final FloatRect tempRect;
	private final FloatPoint tempCursor;

	public InputHandler()
	{
		text = new Text();
		passwordText = new Text();

		shiftState = false;
		rect = new FloatRect();
		setInputFormat(InputFormat.NORMAL, 0);
		refreshInput();

		tempRect = new FloatRect();
		tempCursor = new FloatPoint();
	}

	/**
	 * Handles mouse-down event.
	 * @param mouse
	 */
	public void mouseDown(IFloatPoint_r mouse)
	{
		final IText_r t = getDisplayedText();
		final float x = mouse.getX() - rect.getX() - xOnset;
		float wThis;

		// check click left of selection
		int i = 0;
		float w = 0.0f;
		while (i < displayLeft.length)
		{
			int symbol = TextMap.symbolOf(t.charAt(displayLeft.start + i));
			wThis = Materials.printer.getDefaultPadding(symbol);

			if (w + wThis * HALF >= x)
			{
				mouseDownSelect(displayLeft.start + i);
				return;
			}

			++i;
			w += wThis;
		}

		// check click inside selection
		i = 0;
		while (i < displayMiddle.length)
		{
			int symbol = TextMap.symbolOf(t.charAt(displayMiddle.start + i));
			wThis = Materials.printer.getDefaultPadding(symbol);

			if (w + wThis * HALF > x)
			{
				mouseDownSelect(displayMiddle.start + i);
				return;
			}

			++i;
			w += wThis;
		}

		// check click right of selection
		i = 0;
		while (i < displayRight.length)
		{
			int symbol = TextMap.symbolOf(t.charAt(displayRight.start + i));
			wThis = Materials.printer.getDefaultPadding(symbol);

			if (w + wThis * HALF > x)
			{
				mouseDownSelect(displayRight.start + i);
				return;
			}

			++i;
			w += wThis;
		}
		mouseDownSelect(displayRight.start + displayRight.length);
	}

	public void setInputFormat(InputFormat inputFormat, int maxLength)
	{
		this.inputFormat = inputFormat;
		this.maxLength = maxLength;

		switch (inputFormat)
		{
			case NUMBERS:
				input = new NumberInput(maxLength);
				break;
			case ACC_NAME:
				input = new AccNameInput();
				break;
			case CHAR_NAME:
				input = new CharNameInput();
				break;
			default:
				input = new DefaultInput(maxLength);
				break;
		}
		inputChanged = true;
	}

	public void setInput(String s)
	{
		setInput(s.length(), 0, true, s);
	}

	private void setInput(int selStart, int selLength, boolean cursorRight, String s)
	{
		this.cursorRight = cursorRight;
		this.input.setInput(selStart, selLength, s);
		inputChanged = true;
	}

	private void refreshInput()
	{
		inputChanged = false;
		text.set(input.toString());
		generatePasswordText();
		selectionChanged = true;
	}

	/**
	 * Trims a word so that its width is less or equal than the given width.
	 * @param t
	 * @param word
	 * @param width
	 *            Maximum width
	 * @param fromLeft
	 *            Specifies if the Word should get trimmed from the left or right
	 * @return Resulting width
	 */
	private float trimWord(IText_r t, Word word, float width, boolean fromLeft)
	{
		float w; // resulting width

		if (width <= 0.0f)
		{
			if (fromLeft)
				word.start = word.length;

			word.length = 0;
			w = 0.0f;
		}
		else
		{
			w = Materials.printer.calcTextWidth(t, word); // initial word width

			while (w > width) // while the word's width is too large for the display area (width)
			{
				char c; // character to be removed

				if (fromLeft)
				{
					c = t.charAt(word.start++);
					word.length--;
				}
				else
					c = t.charAt(word.start + (--word.length));

				w -= Materials.printer.getDefaultPadding(TextMap.symbolOf(c, -1));
			}
		}
		return w;
	}

	private void refreshSelection()
	{
		if (inputChanged)
			refreshInput();

		selectionChanged = false;
		//		float major = rect.getWidth() * MAJOR_FAC;
		float major = rect.getWidth() - 25.0f;
		int selStart = input.getSelectionStart();
		int selLength = input.getSelectionLength();

		displayMiddle.set(selStart, selLength);

		displayLeft.start = 0;
		displayLeft.length = displayMiddle.start;

		displayRight.start = displayMiddle.start + displayMiddle.length;
		displayRight.length = text.length() - displayRight.start;

		// calculate left, middle, right
		float wl, wm, wr;
		IText_r text = getDisplayedText();

		if (cursorRight)
		{
			wm = trimWord(text, displayMiddle, major, true);
			wl = trimWord(text, displayLeft, major - wm, true);
			wr = trimWord(text, displayRight, rect.getWidth() - wm - wl, false);
		}
		else
		{
			wl = trimWord(text, displayLeft, major, true);
			wm = trimWord(text, displayMiddle, rect.getWidth() - wl, false);
			wr = trimWord(text, displayRight, rect.getWidth() - wl - wm, false);
		}
		xOnset = (rect.getWidth() - wl - wm - wr) * ALIGN;
	}

	public InputFormat getInputFormat()
	{
		return inputFormat;
	}

	/**
	 * Modifies the selection triggered by a mouse-down event.
	 * @param newSelStart
	 *            Selection start
	 */
	private void mouseDownSelect(int newSelStart)
	{
		int selStart = input.getSelectionStart();
		int selLength = input.getSelectionLength();

		if (shiftState)
		{
			int pin = selStart;
			if (!cursorRight)
				pin += selLength;

			if (newSelStart < pin)
				setSelection(newSelStart, pin - newSelStart, cursorRight); // TODO ?
			else
				setSelection(newSelStart, newSelStart - pin, cursorRight); // TODO ?
		}
		else
			setSelection(newSelStart, 0, true);
	}

	public int getMaxLength()
	{
		return input.getMaxLength();
	}

	public void setMaxLength(int max)
	{
		input.setMaxLength(max);
		inputChanged = true;
	}

	/**
	 * Sets the rectangle to draw the text in.
	 * @param rect
	 */
	public void setRect(IFloatRect_r rect)
	{
		this.rect.copyFrom(rect);
		selectionChanged = true; // displayed text could change due to resizing the Edit
	}

	public Text getText()
	{
		return text;
	}

	public int getValue()
	{
		int result = 0;
		if (inputFormat == InputFormat.NUMBERS)
		{
			result = Integer.valueOf(input.toString());
		}
		// else TODO (exception?)
		return result;
	}

	/**
	 * If this Edit contains a password, a String of the same length as the input String filled with PASSWORD_CHAR is created.
	 */
	private void generatePasswordText()
	{
		if (inputFormat == InputFormat.PASSWORD)
		{
			int len = input.length();
			if (len > 0)
				passwordText.fill(len, PASSWORD_CHAR);
			else
				passwordText.set("");
		}
		else
			passwordText.set("");
	}

	public void selectAll()
	{
		cursorRight = true;
		input.selectAll();
		selectionChanged = true;
	}

	/**
	 * Sets the shift state.
	 * @param shiftState
	 */
	public void setShiftState(boolean shiftState)
	{
		this.shiftState = shiftState;
	}

	/**
	 * Sets the control state.
	 * @param controlState
	 */
	public void setControlState(boolean controlState)
	{
		this.controlState = controlState;
	}

	public IText_r getDisplayedText()
	{
		return (inputFormat == InputFormat.PASSWORD) ? passwordText : text;
	}

	public void draw(IRendererUI renderer, boolean drawCursor) throws PrismException
	{
		if (inputChanged)
			refreshInput();
		if (selectionChanged)
			refreshSelection();

		// print left substring
		tempRect.copyFrom(rect);
		tempRect.addX(xOnset);
		//		Printer.pushColor(TriColor.WHITE_OPAQUE);
		Materials.printer.setColor(PrismColor.opaqueWhite);

		IText_r text = getDisplayedText();

		Materials.printer.setText(text);
		Materials.printer.setOnset(displayLeft.start, displayLeft.calcEnd());
		float newX = Materials.printer.print(tempRect.getTopLeft());
		tempRect.setX(newX);
		//		r.getPositionW().setX(Printer.printCentered(renderer, r, getDisplayedText(), FontCenter.VERTICAL, displayLeft));

		// copy cursor position if cursor is left of the selection
		if (!cursorRight)
			tempCursor.x = tempRect.getX();

		// print selected substring
		if (drawCursor)
		{
			//			Printer.pushColor(BlinkColor.selectionColor.getColor());
			Materials.printer.setColor(PrismColor.temp);
		}

		//		float x = Printer.printCentered(renderer, r, getDisplayedText(), FontCenter.VERTICAL, displayMiddle);
		//		r.setX(x);
		//		Materials.printer.setText(text);
		Materials.printer.setOnset(displayMiddle.start, displayMiddle.calcEnd());
		newX = Materials.printer.print(tempRect.getTopLeft());
		tempRect.setX(newX);

		// copy cursor position if cursor is right of the selection
		if (cursorRight)
			tempCursor.x = tempRect.getX();

		// print right substring
		//				Printer.pushColor(TriColor.WHITE_OPAQUE);
		Materials.printer.setColor(PrismColor.opaqueWhite);

		//		x = Printer.printCentered(renderer, r, getDisplayedText(), FontCenter.VERTICAL, displayRight);
		//		r.setX(x);
		//		Materials.printer.setText(text);
		Materials.printer.setOnset(displayRight.start, displayRight.calcEnd());
		newX = Materials.printer.print(tempRect.getTopLeft());
		tempRect.setX(newX);

		// draw the cursor
		if (drawCursor)
		{
			//			Printer.pushColor(BlinkColor.cursorColor.getColor());
			Materials.printer.setColor(PrismColor.temp);

			tempCursor.x -= 3.0f;
			tempCursor.y = rect.getY() + 9.0f;

			//			Printer.printSingle(renderer, cursor, "^");
			Materials.printer.setText(cursorText);
			Materials.printer.print(tempCursor);
		}

		// draw arrow to the left if text is partially invisible
		if (displayLeft.start > 0)
		{
			tempCursor.x = rect.getX() - 8.0f;
			tempCursor.y = rect.getY() + 2.0f;

			//			Printer.printSingle(renderer, cursor, "<");
			Materials.printer.setText(markLeft);
			Materials.printer.print(tempCursor);
		}

		// draw arrow to the right if text is partially invisible
		if (displayRight.calcEnd() < text.length())
		{
			tempCursor.x = tempRect.getX();
			tempCursor.y = rect.getY() + 2.0f;

			//			Printer.printSingle(renderer, cursor, ">");
			Materials.printer.setText(markRight);
			Materials.printer.print(tempCursor);
		}

		Materials.printer.resetColor();
	}

	/**
	 * Handles the end-key and home-key.
	 */
	public void keyEndOrHome(boolean endKey)
	{
		int selStart = input.getSelectionStart();
		int selLength = input.getSelectionLength();

		// calculate selection start position
		int pin = selStart;
		if (!cursorRight)
			pin += selLength;

		if (endKey)
		{
			if (shiftState)
				setSelection(pin, input.length() - pin, true);
			else
				setSelection(input.length(), 0, true);
		}
		else
		{
			if (shiftState)
				setSelection(0, pin, false);
			else
				setSelection(0, 0, true);
		}
	}

	/**
	 * @return Cursor position
	 */
	private int getCursorPos()
	{
		int cursor = input.getSelectionStart();
		if (cursorRight)
			cursor += input.getSelectionLength();

		return cursor;
	}

	/**
	 * Handles arrow keys.
	 * @param key
	 */
	public void handleArrowKey(int key)
	{
		handleArrowKeyOnce(key);

		if (controlState)
		{
			while (!checkControlStop(key))
				handleArrowKeyOnce(key);
		}
	}

	/**
	 * Checks if jumping words with CTRL should stop.
	 * @param key
	 * @return Returns true if loop should stop
	 */
	private boolean checkControlStop(int key)
	{
		int cursor = getCursorPos();

		boolean stop = (cursor <= 0) || (cursor >= input.length());

		if (!stop)
			stop = (input.charAt(cursor - 1) == KeyEvent.VK_SPACE);

		return stop;
	}

	/**
	 * Handles an arrow key.
	 * @param key
	 */
	private void handleArrowKeyOnce(int key)
	{
		// calculate selection start position
		int selStart = input.getSelectionStart();
		int selLength = input.getSelectionLength();
		int cursor = getCursorPos();

		if (key == KeyEvent.VK_LEFT)
		{
			if (shiftState)
			{
				if (cursorRight)
				{
					if (selLength > 0)
						setSelection(selStart, selLength - 1, true);
					else
						setSelection(selStart - 1, selLength + 1, false);
				}
				else
					setSelection(selStart - 1, selLength + 1, false);
			}
			else
				setSelection(cursor - 1, 0, false);
		}
		else
		{
			if (shiftState)
			{
				if (cursorRight)
					setSelection(selStart, selLength + 1, true);
				else
				{
					if (selLength > 0)
						setSelection(selStart + 1, selLength - 1, false);
					else
						setSelection(selStart, selLength + 1, true);
				}
			}
			else
				setSelection(cursor + 1, 0, true);
		}
	}

	public void handleKeyChar(char keyChar)
	{
		if (keyChar == KeyEvent.VK_BACK_SPACE)
			handleBackspace();
		else if (keyChar == KeyEvent.VK_DELETE)
			handleDelete();
		else if (keyChar == COPY)
			setClipboard();
		else if (keyChar == CUT)
		{
			setClipboard();
			if (input.getSelectionLength() > 0)
				handleBackspace();
		}
		else
		{
			String insert;

			if (keyChar == PASTE)
			{
				Text clipboardText = new Text(getClipboard());
				insert = clipboardText.toString();
			}
			else
				insert = TextMap.convertChar(keyChar);

			insertString(insert);
		}
	}

	/**
	 * Inserts a String to the TextInput.
	 * @param insert
	 */
	private void insertString(String insert)
	{
		// make the inserted String valid
		int len = insert.length();
		int selStart = input.getSelectionStart();
		int selLength = input.getSelectionLength();

		if (len > 0)
		{
			StringBuffer newText = new StringBuffer();

			// copy the part left of selection
			if (selStart > 0)
				newText.append(input.substring(0, selStart));

			// append selection and mark cursor position
			newText.append(insert);
			int cursor = newText.length();

			// append the part right of selection
			int selEnd = selStart + selLength;
			if (selEnd < input.length())
				newText.append(input.substring(selEnd));

			// adopt the resulting String
			setInput(cursor, 0, true, newText.toString());
		}
	}

	/**
	 * Tries to get a String from clipboard.
	 * @return String
	 */
	private String getClipboard()
	{
		Clipboard clipboard = Toolkit.getDefaultToolkit().getSystemClipboard();
		Transferable contents = clipboard.getContents(null);
		boolean ok = (contents != null) && contents.isDataFlavorSupported(DataFlavor.stringFlavor);
		String result = "";

		if (ok)
		{
			try
			{
				result = (String) contents.getTransferData(DataFlavor.stringFlavor);
			}
			catch (Exception ex)
			{
			}
		}
		return result;
	}

	/**
	 * Copies the selected substring to clipboard.
	 */
	private void setClipboard()
	{
		Word selection = new Word(input.getSelectionStart(), input.getSelectionLength());
		try
		{
			if (inputFormat != InputFormat.PASSWORD)
			{
				StringSelection ss = new StringSelection(selection.extractFrom(input.toString()));
				Toolkit.getDefaultToolkit().getSystemClipboard().setContents(ss, null);
			}
		}
		catch (Exception e)
		{
		}
	}

	private void handleBackspace()
	{
		int selStart = input.getSelectionStart();
		int selLength = input.getSelectionLength();

		StringBuffer newText = new StringBuffer();
		int cursor = selStart - 1;

		if (input.getSelectionLength() == 0)
		{
			if (cursor > 0)
				newText.append(input.substring(0, cursor));
		}
		else
		{
			newText.append(input.substring(0, selStart));
			++cursor;
		}
		newText.append(input.substring(selStart + selLength));

		setInput(cursor, 0, true, newText.toString());
	}

	private void setSelection(int start, int length, boolean cursorRight)
	{
		this.cursorRight = cursorRight;
		input.setSelection(start, length);
		selectionChanged = true;
	}

	private void handleDelete()
	{
		int selStart = input.getSelectionStart();
		int selLength = input.getSelectionLength();
		StringBuffer newText = new StringBuffer(input.substring(0, selStart));

		if (selLength == 0)
		{
			int start = selStart + 1;
			if (start < text.length())
				newText.append(input.substring(start));
		}
		else
		{
			newText.append(input.substring(selStart + selLength));
		}
		setInput(selStart, 0, true, newText.toString());
	}

	@Override
	public String toString()
	{
		return text.toString();
	}

	@Override
	public void moveBy(float dx, float dy)
	{
		rect.addPosition(dx, dy);
	}

	@Override
	public void rearrange(IFloatRect_r parentRect)
	{
		rect.setTopLeft(parentRect.getX(), parentRect.getY() + 8.0f);
		rect.setSize(parentRect.getWidth(), 20.0f);

		refreshSelection();
	}

	@Override
	public void appendTo(Heap_out h)
	{
		h.ints.add(maxLength);
		h.ints.add(inputFormat.ordinal());
	}

	@Override
	public void extractFrom(Heap_in h) throws PrismException
	{
		int maxLength = h.nextInt();
		setInputFormat(InputFormat.valueOf(h.nextInt()), maxLength);
	}
}
