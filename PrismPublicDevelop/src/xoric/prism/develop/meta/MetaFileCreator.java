package xoric.prism.develop.meta;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.List;

import xoric.prism.data.exceptions.PrismException;
import xoric.prism.data.exceptions.UserErrorText;
import xoric.prism.data.meta.AttachmentHeader;
import xoric.prism.data.meta.AttachmentTable;
import xoric.prism.data.meta.MetaBlock;
import xoric.prism.data.meta.MetaFile;
import xoric.prism.data.meta.MetaKey;
import xoric.prism.data.meta.MetaLine;
import xoric.prism.data.meta.MetaList;
import xoric.prism.data.meta.MetaTimeStamp;
import xoric.prism.data.meta.MetaType;
import xoric.prism.data.tools.Common;
import xoric.prism.data.types.Heap;
import xoric.prism.data.types.IPath_r;
import xoric.prism.data.types.IntPacker;

public class MetaFileCreator
{
	private static final char CHAR_COMMENT = '%';
	private static final char CHAR_METABLOCK = '#';
	private static final String STRING_METABLOCK = "#";
	private static final String CHAR_KEY_SPLIT = ":";
	private static final String CHAR_PARAM_SPLIT = ";";

	private final IPath_r sourcePath;
	private final IPath_r targetPath;

	private File targetFile;

	private MetaList infusedMetaList;

	public MetaFileCreator(IPath_r sourcePath, IPath_r targetPath)
	{
		this.sourcePath = sourcePath;
		this.targetPath = targetPath;
		this.infusedMetaList = null;
	}

	/**
	 * Supply a previously created MetaList. {@link MetaFileCreator} will then no longer attempt to generate a MetaList from a text file
	 * {@code "meta.txt"} within the source directory.
	 * @param metaList
	 */
	public void infuseMetaList(MetaList metaList)
	{
		this.infusedMetaList = metaList;
	}

	private static MetaBlock createMetaBlock(String line) throws PrismException
	{
		// split line
		line = line.substring(1);
		String[] s = line.split(STRING_METABLOCK);

		// check parameter count
		if (s.length != 2)
		{
			PrismException e = new PrismException();
			// ----
			e.user.setText(UserErrorText.INTERNAL_PROBLEM);
			// ----
			e.code.setText("could not create MetaBlock from the given line (" + s.length + " parameter(s) found instead of 2)");
			e.code.addInfo("line", line);
			// ----
			// ----
			throw e;
		}

		// create MetaBlock
		MetaType t = MetaType.valueOf(s[0]);
		int v = Integer.valueOf(s[1]);
		MetaBlock block = new MetaBlock(t, v);

		return block;
	}

	private static MetaTextLine createMetaTextLine(String line) throws PrismException
	{
		// split line
		String[] s = line.split(CHAR_KEY_SPLIT);

		if (s.length != 2)
		{
			PrismException e = new PrismException();
			// ----
			e.user.setText(UserErrorText.INTERNAL_PROBLEM);
			// ----
			e.code.setText("could not create MetaTextLine from the given line (" + s.length + " parameter(s) found instead of 2)");
			e.code.addInfo("line", line);
			// ----
			// ----
			throw e;
		}

		// extract key and parameters
		String key = s[0];
		String[] params = s[1].split(CHAR_PARAM_SPLIT);

		// create MetaLine
		MetaTextLine t = new MetaTextLine(key, params);

		return t;
	}

	private int readVersion(IPath_r path, String filename) throws PrismException
	{
		int version = 0;
		File file = path.getFile(filename);

		if (file.exists())
		{
			try
			{
				MetaFile metaFile = new MetaFile(targetPath, filename);
				metaFile.load();
				version = metaFile.getLocalFileVersion();
			}
			catch (Exception e0)
			{
				PrismException e = new PrismException(e0);
				// ----
				// ----
				// ----
				e.setText("Could not read file version.");
				e.addInfo("file", file.toString());
				// ----
				throw e;
			}
		}
		return version;
	}

	public void create() throws PrismException
	{
		// check if path exists
		if (!sourcePath.exists())
		{
			PrismException e = new PrismException();
			// ----
			// ----
			// ----
			e.setText("Source directory does not exist.");
			e.addInfo("directory", sourcePath.toString());
			// ----
			throw e;
		}

		// load MetaList from local text file or use infused MetaList
		MetaListInfo info;
		MetaList metaList;
		if (infusedMetaList != null)
		{
			metaList = infusedMetaList;
			info = new MetaListInfo(null);
		}
		else
		{
			File textFile = sourcePath.getFile("meta.txt");
			metaList = loadLocalMetaList(textFile);
			info = new MetaListInfo(textFile);
		}

		// check if MetaList contains all required information
		MetaBlock devBlock = metaList.claimMetaBlock(MetaType.DEVELOP);
		String targetFilename = obtainTargetFilename(devBlock);
		checkDevelopBlock(devBlock, targetFilename, info);

		// gather attachments
		List<MetaLine> attachmentLines = devBlock.findLines(MetaKey.ATTACH);
		AttachmentImporter[] imports = null;
		final int attachmentCount = attachmentLines.size();
		AttachmentTable table = null;

		if (attachmentCount > 0)
		{
			table = new AttachmentTable(attachmentCount);
			imports = new AttachmentImporter[attachmentCount];

			for (int i = 0; i < attachmentCount; ++i)
			{
				// obtain attachment from MetaLine
				MetaLine l = attachmentLines.get(i);
				String filename = null;
				if (l.getHeap().texts.size() > 0)
					filename = l.getHeap().texts.get(0).toString().toLowerCase();

				// check if the attachment file is valid and exists
				File attachmentFile = filename == null ? null : sourcePath.getFile(filename);
				checkAttachment(attachmentFile, l, info);

				// insert attachment to table
				AttachmentImporter a = new AttachmentImporter(sourcePath, filename);
				a.importAttachment();
				imports[i] = a;
				AttachmentHeader h = a.createHeader();
				table.set(i, h);
			}
		}

		// read current file version
		int currentVersion = readVersion(targetPath, targetFilename);
		int newVersion = currentVersion + 1;

		// create directories
		targetFile = targetPath.getFile(targetFilename);
		File makePath = targetFile.getParentFile();
		if (!makePath.isDirectory())
		{
			boolean wasPathCreated = makePath.mkdirs();
			if (!wasPathCreated)
			{
				PrismException e = new PrismException();
				// ----
				e.user.setText(UserErrorText.COULD_NOT_CREATE_DIRECTORY);
				// ----
				e.code.setText("could not create target directory");
				// ----
				e.addInfo("path", makePath.toString());
				// ----
				throw e;
			}
		}

		// remove develop MetaBlock from MetaList
		metaList.dropMetaBlock(devBlock);

		// write file
		MetaTimeStamp timeStamp = new MetaTimeStamp();
		try
		{
			// open target file
			FileOutputStream stream = new FileOutputStream(targetFile);

			// 1) write version
			IntPacker.pack_s(stream, newVersion);

			// 2) write TimeStamp
			timeStamp.pack(stream);

			// 3) write MetaList
			metaList.pack(stream);

			// 4) write number of attachments
			IntPacker.pack_s(stream, attachmentCount);

			// update start of attachments
			int startOfAttachmentTable = (int) targetFile.length();
			int currentAttachmentStart = startOfAttachmentTable;

			if (table != null)
				currentAttachmentStart += table.calcPackedSize();

			for (int i = 0; i < attachmentCount; ++i)
			{
				AttachmentHeader h = table.get(i);
				h.setStart(currentAttachmentStart);
				currentAttachmentStart += h.getContentSize();
			}

			// 5) write attachment headers
			if (table != null)
				table.pack(stream);

			// 6) write attachment contents
			for (int i = 0; i < attachmentCount; ++i)
			{
				AttachmentImporter ai = imports[i];
				stream.write(ai.getContent());
			}

			// close MetaFile
			stream.close();
		}
		catch (Exception e0)
		{
			PrismException e = new PrismException(e0);
			// ----
			e.user.setText(UserErrorText.WRITE_ERROR);
			// ----
			e.code.setText("error while writing MetaFile");
			// ----
			e.addInfo("file", targetFilename);
			// ----
			throw e;
		}

		// print to console
		StringBuffer sb = new StringBuffer();
		sb.append("MetaFile written: " + targetFile.getPath() + " | size: " + Common.getFileSize(targetFile.length()) + " | version: "
				+ newVersion + " | " + timeStamp.toString() + " | blocks: ");
		for (int i = 0; i < metaList.getBlockCount(); ++i)
		{
			MetaBlock block = metaList.getMetaBlock(i);
			if (i > 0)
				sb.append(", ");
			sb.append(block.getMetaType().toString());
		}
		sb.append(" | attachments: " + attachmentCount);

		System.out.println(sb.toString());
	}

	private void checkAttachment(File obtainedAttachmentFile, MetaLine sourceMetaLine, MetaListInfo info) throws PrismException
	{
		if (obtainedAttachmentFile == null || !obtainedAttachmentFile.exists())
		{
			PrismException e = new PrismException();
			// ----
			if (!info.wasInfused())
			{
				e.user.setText(UserErrorText.DEVELOP_FILE_CAUSED_PROBLEM);
				// ----
				e.code.setText("attachment not found");
				e.code.addInfo("line", sourceMetaLine.toString());
				// ----
				e.addInfo("file", info.getSourceFile().toString());
			}
			else
			{
				e.user.setText(UserErrorText.INTERNAL_PROBLEM);
				// ----
				e.code.setText("appearently an infused MetaList supplied an attachment that does not exist");
				e.code.addInfo("metaLine", sourceMetaLine.toString());
			}
			// ----
			throw e;
		}
	}

	private void checkDevelopBlock(MetaBlock devBlock, String targetFilename, MetaListInfo info) throws PrismException
	{
		// check if MetaBlock of type DEVELOP exists
		if (devBlock == null)
		{
			PrismException e = new PrismException();
			// ----
			if (!info.wasInfused())
			{
				e.user.setText(UserErrorText.DEVELOP_FILE_CAUSED_PROBLEM);
				// ----
				e.code.setText("MetaBlock " + MetaType.DEVELOP.toString() + " is missing");
				// ----
				e.addInfo("file", info.getSourceFile().toString());
			}
			else
			{
				e.user.setText(UserErrorText.INTERNAL_PROBLEM);
				// ----
				e.code.setText("appearently an infused MetaList is missing a " + MetaType.DEVELOP.toString() + " MetaBlock");
			}
			// ----
			throw e;
		}

		// check if a target file is specified
		if (targetFilename == null)
		{
			PrismException e = new PrismException();
			// ----
			if (!info.wasInfused())
			{
				e.user.setText(UserErrorText.DEVELOP_FILE_CAUSED_PROBLEM);
				// ----
				e.code.setText("target file is missing");
				// ----
				e.addInfo("file", info.getSourceFile().toString());
			}
			else
			{
				e.user.setText(UserErrorText.INTERNAL_PROBLEM);
				// ----
				e.code.setText("appearently the " + MetaType.DEVELOP.toString()
						+ " MetaBlock of an infused MetaList did not supply a target file");
			}
			// ----
			throw e;
		}
	}

	private String obtainTargetFilename(MetaBlock devBlock) throws PrismException
	{
		MetaLine targetLine = devBlock.claimLine(MetaKey.TARGET);
		Heap targetHeap = targetLine.getHeap();
		String targetFilename = null;
		if (targetHeap != null && targetHeap.texts.size() == 1)
			targetFilename = targetHeap.texts.get(0).toString().toLowerCase();

		return targetFilename;
	}

	public static MetaList loadLocalMetaList(File textFile) throws PrismException
	{
		// check if text file exists
		if (!textFile.exists())
		{
			PrismException e = new PrismException();
			// ----
			// ----
			// ----
			e.setText(UserErrorText.FILE_NOT_FOUND);
			e.addInfo("file", textFile.toString());
			// ----
			throw e;
		}

		// read string list from text file
		ArrayList<String> list = new ArrayList<String>();
		try
		{
			FileReader r = new FileReader(textFile);
			BufferedReader in = new BufferedReader(r);
			String s;
			while ((s = in.readLine()) != null)
				list.add(s);
			in.close();
		}
		catch (Exception e0)
		{
			PrismException e = new PrismException(e0);
			// ----
			// ----
			// ----
			e.setText(UserErrorText.READ_ERROR);
			e.addInfo("file", textFile.toString());
			// ----
			throw e;
		}

		// extract meta blocks
		MetaList metaList = new MetaList();
		MetaBlock b = null;

		for (String line : list)
		{
			if (line.length() > 0 && line.charAt(0) != CHAR_COMMENT)
			{
				char key = line.charAt(0);
				boolean isMetaBlock = key == CHAR_METABLOCK;

				if (b == null && !isMetaBlock)
				{
					PrismException e = new PrismException();
					// ----
					e.user.setText(UserErrorText.DEVELOP_FILE_CAUSED_PROBLEM);
					// ----
					e.code.setText("a MetaBlock has to be specified before adding lines");
					e.code.addInfo("line", line);
					// ----
					e.addInfo("file", textFile.toString());
					// ----
					throw e;
				}

				if (isMetaBlock)
				{
					if (b != null)
						metaList.addMetaBlock(b);

					b = createMetaBlock(line);
				}
				else
				{
					MetaTextLine t = createMetaTextLine(line);
					MetaLine l = t.toMetaLine();
					b.addMetaLine(l);
				}
			}
		}

		// add last MetaBlock if any
		if (b != null)
			metaList.addMetaBlock(b);

		return metaList;
	}

	public File getResultingTargetFile()
	{
		return targetFile;
	}
}
