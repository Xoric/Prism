package xoric.prism.develop.meta;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.List;

import xoric.prism.data.exceptions.PrismDevException;
import xoric.prism.data.exceptions.PrismException;
import xoric.prism.data.meta.AttachmentHeader;
import xoric.prism.data.meta.AttachmentTable;
import xoric.prism.data.meta.MetaBlock;
import xoric.prism.data.meta.MetaFile;
import xoric.prism.data.meta.MetaKey;
import xoric.prism.data.meta.MetaLine;
import xoric.prism.data.meta.MetaList;
import xoric.prism.data.meta.MetaType;
import xoric.prism.data.meta.TimeStamp;
import xoric.prism.data.modules.ActorID;
import xoric.prism.data.modules.ErrorCode;
import xoric.prism.data.modules.ErrorID;
import xoric.prism.data.modules.IActor;
import xoric.prism.data.tools.Common;
import xoric.prism.data.types.Heap;
import xoric.prism.data.types.IPath_r;
import xoric.prism.data.types.IntPacker;
import xoric.prism.data.types.Path;

public class MetaFileCreator implements IActor
{
	private static final char CHAR_COMMENT = '%';
	private static final char CHAR_METABLOCK = '#';
	private static final String STRING_METABLOCK = "#";
	private static final String CHAR_KEY_SPLIT = ":";
	private static final String CHAR_PARAM_SPLIT = ";";

	private final IPath_r sourcePath;
	private final IPath_r targetPath;

	public MetaFileCreator(Path sourcePath, Path targetPath)
	{
		this.sourcePath = sourcePath;
		this.targetPath = targetPath;
	}

	private MetaBlock createMetaBlock(String line) throws PrismDevException
	{
		// split line
		line = line.substring(1);
		String[] s = line.split(STRING_METABLOCK);

		// check parameter count
		if (s.length != 2)
		{
			ErrorCode c = new ErrorCode(this, ErrorID.CORRUPT_LINE);
			PrismDevException e = new PrismDevException(c);
			e.appendExpectedInfo("wrong parameter count", 2, s.length);
			e.appendInfo("line", line);
			throw e;
		}

		// create MetaBlock
		MetaType t = MetaType.valueOf(s[0]);
		int v = Integer.valueOf(s[1]);
		MetaBlock block = new MetaBlock(t, v);

		return block;
	}

	private MetaTextLine createMetaTextLine(String line) throws PrismDevException
	{
		// split line
		String[] s = line.split(CHAR_KEY_SPLIT);

		if (s.length != 2)
		{
			ErrorCode c = new ErrorCode(this, ErrorID.CORRUPT_LINE);
			PrismDevException e = new PrismDevException(c);
			e.appendExpectedInfo("wrong parameter count", 2, s.length);
			e.appendInfo("line", line);
			throw e;
		}

		// extract key and parameters
		String key = s[0];
		String[] params = s[1].split(CHAR_PARAM_SPLIT);

		// create MetaLine
		MetaTextLine t = new MetaTextLine(key, params);

		return t;
	}

	private int readVersion(IPath_r path, String filename) throws PrismDevException
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
				ErrorCode c = new ErrorCode(this, ErrorID.READ_ERROR);
				PrismDevException e = new PrismDevException(c);
				e.appendOriginalException(e0);
				e.appendInfo("file", file.toString());
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
			ErrorCode c = new ErrorCode(this, ErrorID.PATH_NOT_FOUND);
			PrismDevException e = new PrismDevException(c);
			e.appendInfo("path", sourcePath.toString());
			throw e;
		}

		// check if text file exists
		File textFile = sourcePath.getFile("meta.txt");
		if (!textFile.exists())
		{
			ErrorCode c = new ErrorCode(this, ErrorID.FILE_NOT_FOUND);
			PrismDevException e = new PrismDevException(c);
			e.appendInfo("file", textFile.toString());
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
			ErrorCode c = new ErrorCode(this, ErrorID.COMMON);
			PrismDevException e = new PrismDevException(c);
			e.appendOriginalException(e0);
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
					ErrorCode c = new ErrorCode(this, ErrorID.UNEXPECTED_LINE);
					PrismDevException e = new PrismDevException(c);
					e.appendInfo("a MetaBlock has to be specified before adding lines");
					e.appendInfo("line", line);
					throw e;
				}

				if (isMetaBlock)
				{
					if (b != null)
						metaList.addMetaBlock(b);

					try
					{
						b = createMetaBlock(line);
					}
					catch (Exception e0)
					{
						ErrorCode c = new ErrorCode(this, ErrorID.CORRUPT_LINE);
						PrismDevException e = new PrismDevException(c);
						e.appendOriginalException(e0);
						e.appendInfo("line", line);
						throw e;
					}
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

		// check if MetaBlock of type DEVELOP exists
		if (!metaList.hasMetaBlock(MetaType.DEVELOP))
		{
			ErrorCode c = new ErrorCode(this, ErrorID.META_BLOCK_MISSING);
			PrismDevException e = new PrismDevException(c);
			e.appendInfo("block", MetaType.DEVELOP.toString());
			throw e;
		}

		// check if a target file is specified
		MetaBlock developBlock = metaList.findMetaBlock(MetaType.DEVELOP);
		Heap targetHeap = developBlock.findKey(MetaKey.TARGET);
		String targetFilename = null;
		if (targetHeap != null && targetHeap.texts.size() == 1)
			targetFilename = targetHeap.texts.get(0).toString().toLowerCase();

		if (targetFilename == null)
		{
			ErrorCode c = new ErrorCode(this, ErrorID.META_LINE_MISSING);
			PrismDevException e = new PrismDevException(c);
			e.appendInfo("block", MetaType.DEVELOP.toString());
			e.appendInfo("missing line", MetaKey.TARGET.toString());
			throw e;
		}

		// gather attachments
		List<MetaLine> attachmentLines = developBlock.findLines(MetaKey.ATTACH);
		final int attachmentCount = attachmentLines.size();
		AttachmentTable table = null;

		if (attachmentCount > 0)
		{
			table = new AttachmentTable(attachmentLines.size());

			for (int i = 0; i < attachmentCount; ++i)
			{
				MetaLine l = attachmentLines.get(i);
				String filename = null;
				if (l.getHeap().texts.size() > 0)
					filename = l.getHeap().texts.get(0).toString().toLowerCase();

				File file = filename == null ? null : sourcePath.getFile(filename);

				if (file == null || !file.exists())
				{
					ErrorCode c = new ErrorCode(this, ErrorID.ATTACHMENT_NOT_FOUND);
					PrismDevException e = new PrismDevException(c);
					e.appendInfo("line", l.toString());
					throw e;
				}

				// insert attachment to table
				AttachmentImporter a = new AttachmentImporter(sourcePath, filename);
				a.importAttachment();
				AttachmentHeader h = a.createtHeader();
				table.set(i, h);
			}
		}

		// read current file version
		int currentVersion = readVersion(targetPath, targetFilename);
		int newVersion = currentVersion + 1;

		// create directories
		File targetFile = targetPath.getFile(targetFilename);
		File makePath = targetFile.getParentFile();
		if (!makePath.isDirectory())
		{
			boolean wasPathCreated = makePath.mkdirs();
			if (!wasPathCreated)
			{
				ErrorCode c = new ErrorCode(this, ErrorID.PATH_NOT_FOUND);
				PrismDevException e = new PrismDevException(c);
				e.appendInfo("path", makePath.toString());
				throw e;
			}
		}

		TimeStamp timeStamp = new TimeStamp();
		try
		{
			// open target file
			FileOutputStream stream = new FileOutputStream(targetFile);

			// 1) write version
			int startOfAttachmentTable = IntPacker.pack_s(stream, newVersion);

			// 2) write TimeStamp
			startOfAttachmentTable += timeStamp.getPackedSize();
			timeStamp.pack(stream);

			// 3) write MetaList
			startOfAttachmentTable += metaList.getPackedSize();
			metaList.pack(stream);

			// 4) write number of attachments
			startOfAttachmentTable += IntPacker.pack_s(stream, attachmentCount);

			// update start of attachments
			int currentAttachmentStart = startOfAttachmentTable;
			if (table != null)
				currentAttachmentStart += table.getPackedSize();

			for (int i = 0; i < attachmentCount; ++i)
			{
				AttachmentHeader h = table.get(i);
				h.setStart(currentAttachmentStart);
				currentAttachmentStart += h.getContentSize();
			}

			// 5) write attachment contents to MetaFile
			if (table != null)
				table.pack(stream);

			// close MetaFile
			stream.close();
		}
		catch (Exception e0)
		{
			ErrorCode c = new ErrorCode(this, ErrorID.WRITE_ERROR);
			PrismDevException e = new PrismDevException(c);
			e.appendOriginalException(e0);
			e.appendInfo("file", targetFilename);
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

	private static void createMetaFile(String sourceDir)
	{
		Path sourcePath = new Path(sourceDir);
		Path targetPath = new Path("E:/Prism/data");

		MetaFileCreator f = new MetaFileCreator(sourcePath, targetPath);
		try
		{
			f.create();
		}
		catch (Exception e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public static void main(String[] args)
	{
		//		readMetaFile();
		try
		{
			createMetaFile("E:/Prism/resource/common/anim-descriptions");
		}
		catch (Exception e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	@Override
	public ActorID getActorID()
	{
		return ActorID.META_FILE_CREATOR;
	}
}
