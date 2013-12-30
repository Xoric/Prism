package xoric.prism.develop.li;

import java.io.File;

import xoric.prism.common.Common;
import xoric.prism.data.AttachmentHeader;
import xoric.prism.exceptions.PrismMetaFileException;
import xoric.prism.meta.AttachmentLoader;
import xoric.prism.meta.MetaFile;

public class MetaFilePrinter
{
	private static final String INDENT = "\t";

	private static void printFile(File file)
	{
		// print name
		System.out.println(file.getName().toString());

		// open MetaFile
		MetaFile f = new MetaFile(file);
		try
		{
			f.load();

			// print version and TimeStamp
			System.out.println(INDENT + "version: " + f.getLocalFileVersion());
			System.out.println(INDENT + "timeStamp: " + f.getTimeStamp().toString());

			AttachmentLoader a = f.getAttachmentLoader();
			int n = a.getAttachmentCount();

			for (int i = 0; i < n; ++i)
			{
				AttachmentHeader h = a.get(i);

				StringBuffer sb = new StringBuffer();
				sb.append(INDENT);
				sb.append("[" + i + "] ");
				sb.append(h.getName().toString());
				sb.append(" - " + Common.getFileSize(h.getContentSize()));

				if (h.isCompressed())
					sb.append(" **");

				System.out.println(sb.toString());
			}
		}
		catch (PrismMetaFileException e)
		{
			e.printStackTrace();
		}
	}

	public static void main(String[] args)
	{
		if (args.length > 0)
		{
			File f = new File(args[0]);
			printFile(f);
		}
	}
}