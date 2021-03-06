package xoric.prism.creator.common.tools;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;

import xoric.prism.data.exceptions.PrismException;
import xoric.prism.data.tools.Common;
import xoric.prism.swing.input.PrismFileDialog;

public class ExternalImageEditor extends TempFile
{
	private static ExternalImageEditor instance;

	private File programFile;

	private ExternalImageEditor()
	{
		super(Common.GAME_NAME + ".external.tmp");
	}

	private boolean isFileSet()
	{
		return programFile != null && programFile.toString().length() > 0;
	}

	public void execute(File imageFile)
	{
		gatherProgramFile();

		if (isFileSet())
		{
			try
			{
				String[] s = { programFile.toString(), imageFile.toString() };
				Runtime.getRuntime().exec(s);
			}
			catch (IOException e0)
			{
				PrismException e = new PrismException(e0);
				e.setText("An error occured while trying to open an image in the external editing program specified by the user.");
				e.addInfo("editing program", programFile.toString());
				e.addInfo("image file", imageFile.toString());
				e.code.print();
				e.user.showMessage();
			}
		}
	}

	private void loadFileIfNotYetDone()
	{
		if (!isFileSet())
			load();
	}

	private void gatherProgramFile()
	{
		if (!isFileSet())
		{
			loadFileIfNotYetDone();

			if (!isFileSet())
				showInput();
		}
	}

	public void showInput()
	{
		loadFileIfNotYetDone();

		PrismFileDialog d = new PrismFileDialog("Choose Image Editor", "Please choose an external image editor for editing sprites.");
		d.setDefaultFile(programFile);
		boolean b = d.showOpenDialog("Choose");

		if (b)
		{
			programFile = d.getResult();
			save();
		}
	}

	@Override
	public void pack(OutputStream stream) throws IOException
	{
		String s = programFile == null ? "" : programFile.toString();
		stream.write(s.getBytes("UTF-8"));
	}

	@Override
	public void unpack(InputStream stream) throws IOException
	{
		BufferedReader in = new BufferedReader(new InputStreamReader(stream, "UTF-8"));
		StringBuilder sb = new StringBuilder();
		BufferedReader br = new BufferedReader(in);
		String line;

		while ((line = br.readLine()) != null)
		{
			sb.append(line);
			line = br.readLine();
		}
		programFile = new File(sb.toString());
	}

	public static ExternalImageEditor getInstance()
	{
		if (instance == null)
			instance = new ExternalImageEditor();

		return instance;
	}
}
