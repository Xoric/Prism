package xoric.prism.data.types;

import java.io.File;

public class Path implements IPath_r
{
	private String path;
	private File file;

	public Path(String path)
	{
		set(path);
	}

	public Path(IPath_r path)
	{
		set(path.toString());
	}

	public Path(File f)
	{
		set(f.toString());
	}

	@Override
	public File getFile(String filename)
	{
		return new File(path + filename);
	}

	@Override
	public File[] listFiles()
	{
		return file.listFiles();
	}

	/**
	 * Sets this path. Appends a slash if eventually.
	 * @param path
	 */
	public void set(String path)
	{
		String p = path.replace('\\', '/');

		if (!p.endsWith("/"))
			p += '/';

		this.path = p;
		this.file = new File(p);
	}

	/**
	 * Returns true if this directory exists.
	 * @return boolean
	 */
	@Override
	public boolean exists()
	{
		boolean b = path.length() > 1;
		if (b)
		{
			File f = new File(path);
			b = f.isDirectory();
		}
		return b;
	}

	/**
	 * Returns this path with slash as suffix.
	 * @return String
	 */
	@Override
	public String toString()
	{
		return path;
	}

	/**
	 * Creates the directory named by this abstract pathname, including any necessary but nonexistent parent directories.
	 * @return true if and only if the directory was created, along with all necessary parent directories; false otherwise
	 */
	@Override
	public boolean createDirectories()
	{
		File f = new File(path);
		return f.mkdirs();
	}
}
