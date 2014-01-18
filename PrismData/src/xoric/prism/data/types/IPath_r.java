package xoric.prism.data.types;

import java.io.File;

public interface IPath_r
{
	public File getFile(String filename);

	public boolean exists();

	public boolean createDirectories();

	public File[] listFiles();
}
