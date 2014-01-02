package xoric.prism.develop.li;

import xoric.prism.data.exceptions.PrismMetaFileException;
import xoric.prism.data.meta.AttachmentHeader;
import xoric.prism.data.meta.AttachmentLoader;
import xoric.prism.data.meta.MetaFile;
import xoric.prism.data.tools.Common;
import xoric.prism.data.types.Path;

public class MetaFilePrinter
{
	private static final String INDENT = "\t";

	private static void printFile(Path path, String filename)
	{
		// print name
		System.out.println(filename);

		// open MetaFile
		MetaFile f = new MetaFile(path, filename);
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
		if (args.length == 2)
		{
			Path path = new Path(args[0]);
			printFile(path, args[1]);
		}
	}
}
