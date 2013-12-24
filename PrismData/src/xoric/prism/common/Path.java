package xoric.prism.common;

import java.io.File;

public class Path
{
	private String path;

	public Path(String path)
	{
		set(path);
	}

	/**
	 * Sets this path. Appends a slash if eventually.
	 * @param path
	 */
	public void set(String path)
	{
		if (path != null && path.length() > 0 && !path.endsWith("\\") && !path.endsWith("/"))
			path += '/';

		this.path = path;
	}

	/**
	 * Returns true if this directory exists.
	 * @return boolean
	 */
	public boolean exists()
	{
		File f = new File(path);
		return f.isDirectory();
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
	public boolean createDirectories()
	{
		File f = new File(path);
		return f.mkdirs();
	}
}
