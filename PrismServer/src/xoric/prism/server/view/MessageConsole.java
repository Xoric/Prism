package xoric.prism.server.view;

import java.awt.Color;
import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.util.Calendar;

import javax.swing.SwingUtilities;
import javax.swing.event.DocumentListener;
import javax.swing.text.BadLocationException;
import javax.swing.text.Document;
import javax.swing.text.JTextComponent;
import javax.swing.text.SimpleAttributeSet;
import javax.swing.text.StyleConstants;

/**
 * Modified version of MessageConsole by Rob Camick (November 8, 2008 at 12:10 pm).
 */
class MessageConsole
{
	private static final int maxLines = 200;
	//	private static final Calendar calendar = Calendar.getInstance();
	private static volatile int currentDay = -1;

	private JTextComponent textComponent;
	private DocumentListener limitLinesListener;
	private Document document;

	public MessageConsole(JTextComponent textComponent)
	{
		this.textComponent = textComponent;
		this.textComponent.setEditable(false);
		this.document = textComponent.getDocument();
	}

	public void redirectAll()
	{
		ConsoleOutputStream cos = new ConsoleOutputStream(null, null);
		System.setOut(new PrintStream(cos, true));

		cos = new ConsoleOutputStream(Color.RED, null);
		System.setErr(new PrintStream(cos, true));
	}

	/*
	 *  To prevent memory from being used up you can control the number of
	 *  lines to display in the console
	 *
	 *  This number can be dynamically changed, but the console will only
	 *  be updated the next time the Document is updated.
	 */
	public void setMessageLines(int lines)
	{
		if (limitLinesListener != null)
			document.removeDocumentListener(limitLinesListener);

		limitLinesListener = new LimitLinesDocumentListener(lines, true);
		document.addDocumentListener(limitLinesListener);
	}

	/*
	 *	Class to intercept output from a PrintStream and add it to a Document.
	 *  The output can optionally be redirected to a different PrintStream.
	 *  The text displayed in the Document can be color coded to indicate
	 *  the output source.
	 */
	class ConsoleOutputStream extends ByteArrayOutputStream
	{
		private final String EOL = System.getProperty("line.separator");
		private StringBuffer buffer = new StringBuffer(maxLines);
		private SimpleAttributeSet attributes;
		private PrintStream printStream;
		private boolean isFirstLine;

		/*
		 *  Specify the option text color and PrintStream
		 */
		public ConsoleOutputStream(Color textColor, PrintStream printStream)
		{
			if (textColor != null)
			{
				attributes = new SimpleAttributeSet();
				StyleConstants.setForeground(attributes, textColor);
			}
			this.printStream = printStream;
			this.isFirstLine = true;
			//			this.calendar = Calendar.getInstance();
			//			this.currentDay = -1;
		}

		/*
		 *  Override this method to intercept the output text. Each line of text
		 *  output will actually involve invoking this method twice:
		 *
		 *  a) for the actual text message
		 *  b) for the newLine string
		 *
		 *  The message will be treated differently depending on whether the line
		 *  will be appended or inserted into the Document
		 */
		@Override
		public void flush()
		{
			String message = toString();

			if (message.length() == 0)
				return;

			handleAppend(message);

			reset();
		}

		private String getTimePrefix(Calendar calendar)
		{
			int h = calendar.get(Calendar.HOUR_OF_DAY);
			int m = calendar.get(Calendar.MINUTE);
			int s = calendar.get(Calendar.SECOND);

			String sh = String.valueOf(h);
			String sm = (m < 10 ? "0" : "") + m;
			String ss = (s < 10 ? "0" : "") + s;

			return "[" + sh + ":" + sm + ":" + ss + "] ";
		}

		private String getDayString(Calendar calendar)
		{
			int d = calendar.get(Calendar.DAY_OF_MONTH);
			int m = calendar.get(Calendar.MONTH) + 1;
			int y = calendar.get(Calendar.YEAR);

			String sd = (d < 10 ? "0" : "") + d;
			String sm = (m < 10 ? "0" : "") + m;
			String sy = (y < 10 ? "0" : "") + y;

			return sd + "." + sm + "." + sy;
		}

		private void handleAppend(final String message)
		{
			Calendar calendar = Calendar.getInstance();
			int d = calendar.get(Calendar.DAY_OF_YEAR);
			final String dd;
			if (d != currentDay)
			{
				currentDay = d;
				dd = '\n' + getDayString(calendar) + '\n';
			}
			else
				dd = "";

			final String prefix = getTimePrefix(calendar);

			SwingUtilities.invokeLater(new Runnable()
			{
				@Override
				public void run()
				{
					if (EOL.equals(message))
					{
						buffer.append(message);
					}
					else
					{
						buffer.append(dd + prefix + message);
						clearBuffer();
					}
				}
			});
		}

		/*
		 *  The message and the newLine have been added to the buffer in the
		 *  appropriate order so we can now update the Document and send the
		 *  text to the optional PrintStream.
		 */
		private void clearBuffer()
		{
			//  In case both the standard out and standard err are being redirected
			//  we need to insert a newline character for the first line only

			if (isFirstLine && document.getLength() != 0)
			{
				buffer.insert(0, "\n");
			}
			isFirstLine = false;
			String line = buffer.toString();

			try
			{
				int offset = document.getLength();
				document.insertString(offset, line, attributes);
				textComponent.setCaretPosition(document.getLength());
			}
			catch (BadLocationException ble)
			{
			}

			if (printStream != null)
			{
				printStream.print(line);
			}
			buffer.setLength(0);
		}
	}
}
