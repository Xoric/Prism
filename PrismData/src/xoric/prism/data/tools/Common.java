package xoric.prism.data.tools;

public abstract class Common
{
	public static String getFileSize(long bytes)
	{
		String s;

		if (bytes < 1024)
			s = bytes + " byte(s)";
		else if (bytes < 1024 * 1024)
			s = (bytes / (1024 * 1024)) + " kB";
		else
			s = (bytes / (1024 * 1024 * 1024)) + " MB";

		return s;
	}

	public static int max(int i, int j)
	{
		return i > j ? i : j;
	}

	public static int min(int i, int j)
	{
		return i < j ? i : j;
	}
}