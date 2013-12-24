package xoric.prism.develop.meta;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.List;

import xoric.prism.common.Common;
import xoric.prism.common.Path;
import xoric.prism.data.Heap;
import xoric.prism.data.modules.ActorID;
import xoric.prism.data.modules.ErrorCode;
import xoric.prism.data.modules.ErrorID;
import xoric.prism.data.modules.IActor;
import xoric.prism.exceptions.PrismDevException;
import xoric.prism.exceptions.PrismMetaFileException;
import xoric.prism.meta.MetaBlock;
import xoric.prism.meta.MetaFile;
import xoric.prism.meta.MetaKey;
import xoric.prism.meta.MetaLine;
import xoric.prism.meta.MetaList;
import xoric.prism.meta.MetaType;

public class MetaFileCreator implements IActor
{
	private static final char CHAR_COMMENT = '%';
	private static final char CHAR_METABLOCK = '#';
	private static final String STRING_METABLOCK = "#";
	private static final String CHAR_KEY_SPLIT = ":";
	private static final String CHAR_PARAM_SPLIT = ";";

	private final Path sourcePath;
	private final Path targetPath;

	public MetaFileCreator(Path sourcePath, Path targetPath)
	{
		this.sourcePath = sourcePath;
		this.targetPath = targetPath;
	}

	private MetaBlock createMetaBlock(String line) throws Exception
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

	private int readVersion(File file) throws PrismDevException
	{
		int version = 0;

		if (file.exists())
		{
			try
			{
				MetaFile metaFile = new MetaFile(file);
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

	public void create() throws PrismDevException
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
		File textFile = new File(sourcePath.toString() + "meta.txt");
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
		String targetFile = null;
		if (targetHeap != null && targetHeap.texts.size() == 1)
			targetFile = targetHeap.texts.get(0).toString().toLowerCase();

		if (targetFile == null)
		{
			ErrorCode c = new ErrorCode(this, ErrorID.META_LINE_MISSING);
			PrismDevException e = new PrismDevException(c);
			e.appendInfo("block", MetaType.DEVELOP.toString());
			e.appendInfo("missing line", MetaKey.TARGET.toString());
			throw e;
		}

		// gather attachments
		List<AttachmentWriter> attachments = new ArrayList<AttachmentWriter>();
		int index = 0;
		do
		{
			index = developBlock.findNextIndex(MetaKey.ATTACH, index);
			if (index >= 0)
			{
				MetaLine l = developBlock.getMetaLine(index);
				File file = null;
				if (l.getHeap().texts.size() > 0)
					file = new File(sourcePath.toString() + l.getHeap().texts.get(0).toString().toLowerCase());

				if (file == null || !file.exists())
				{
					ErrorCode c = new ErrorCode(this, ErrorID.ATTACHMENT_NOT_FOUND);
					PrismDevException e = new PrismDevException(c);
					e.appendInfo("line", l.toString());
					throw e;
				}

				// add attachment
				AttachmentWriter a = new AttachmentWriter(file);
				a.load();
				attachments.add(a);

				++index;
			}
		}
		while (index >= 0 && index < developBlock.getLineCount());

		// read current file version
		File f = new File(targetPath.toString() + targetFile);
		int currentVersion = readVersion(f);
		int nextVersion = currentVersion + 1;

		// create directories
		File makePath = f.getParentFile();
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

		// extract attachment sizes
		List<Integer> attachmentSizes = new ArrayList<Integer>();
		for (AttachmentWriter a : attachments)
			attachmentSizes.add(a.getContent().length);

		// create MetaFile
		MetaFile metaFile = new MetaFile(f);
		metaFile.setLocalFileVersion(nextVersion);
		metaFile.getAttachmentLoader().setAttachmentSizes(attachmentSizes);
		try
		{
			metaFile.write();
		}
		catch (Exception e0)
		{
			ErrorCode c = new ErrorCode(this, ErrorID.WRITE_ERROR);
			PrismDevException e = new PrismDevException(c);
			e.appendOriginalException(e0);
			throw e;
		}

		// append attachment contents to MetaFile
		if (attachments.size() > 0)
		{
			try
			{
				FileOutputStream out = new FileOutputStream(f, true);

				for (int i = 0; i < attachments.size(); ++i)
					out.write(attachments.get(i).getContent());

				out.close();
			}
			catch (Exception e0)
			{
				ErrorCode c = new ErrorCode(this, ErrorID.WRITE_ERROR);
				PrismDevException e = new PrismDevException(c);
				e.appendOriginalException(e0);
				e.appendInfo("error while appending attachments");
				throw e;
			}
		}

		// print to console
		StringBuffer sb = new StringBuffer();
		sb.append("MetaFile written: " + f.getPath() + " | size: " + Common.getFileSize(f.length()) + " | version: "
				+ metaFile.getLocalFileVersion() + " | blocks: ");
		for (int i = 0; i < metaList.getBlockCount(); ++i)
		{
			MetaBlock block = metaList.getMetaBlock(i);
			if (i > 0)
				sb.append(", ");
			sb.append(block.getMetaType().toString());
		}
		sb.append(" | attachments: " + attachmentFiles.size());

		System.out.println(sb.toString());
	}

	private static void createMetaFile()
	{
		Path sourcePath = new Path("E:/Prism/resource/shader/default");
		Path targetPath = new Path("E:/Prism/data");

		MetaFileCreator f = new MetaFileCreator(sourcePath, targetPath);
		try
		{
			f.create();
		}
		catch (PrismDevException e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	private static void readMetaFile()
	{
		File file = new File("E:/Prism/data/shader/default.sh");
		MetaFile f = new MetaFile(file);
		try
		{
			f.load();

			System.out.println("loaded MetaFile");
			System.out.println("  version: " + f.getLocalFileVersion());
			int n = f.getAttachmentLoader().getAttachmentCount();

			for (int i = 0; i < n; ++i)
				System.out.println("  attachment[" + i + "]: " + Common.getFileSize(f.getAttachmentLoader().getAttachmentSize(i)));
		}
		catch (PrismMetaFileException e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	private static void testCompression() throws Exception
	{
		File file = new File("E:/Projekte/Java/_Trisma/TriRes/res/model/dyec/colors.txt");

		double d = calcCompressionRate(file);

		System.out.println(d);
	}

	public static void main(String[] args)
	{
		//		readMetaFile();

		try
		{
			testCompression();
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