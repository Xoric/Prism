package xoric.prism.creator.common.view;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;

import xoric.prism.creator.common.tools.TempFile;
import xoric.prism.data.tools.Common;
import xoric.prism.data.types.IPath_r;
import xoric.prism.data.types.Path;

class WorkingDirs extends TempFile
{
	private final List<IPath_r> workingDirs;

	public WorkingDirs(String dataName)
	{
		super(Common.GAME_NAME.toLowerCase() + ".creator." + dataName.toLowerCase() + ".tmp");

		workingDirs = new ArrayList<IPath_r>();
	}

	public void addWorkingDirectory(IPath_r path)
	{
		String s = path.toString();
		int i = findEntry(s);

		if (i >= 0)
		{
			if (i >= 1)
			{
				workingDirs.remove(i);
				workingDirs.add(0, new Path(s));
				save();
			}
		}
		else
		{
			workingDirs.add(0, new Path(s));
			save();
		}
	}

	public List<IPath_r> getDirectories()
	{
		return workingDirs;
	}

	private int findEntry(String s)
	{
		for (int i = 0; i < workingDirs.size(); ++i)
			if (workingDirs.get(i).toString().equals(s))
				return i;

		return -1;
	}

	@Override
	public void pack(OutputStream stream) throws IOException
	{
		int n = workingDirs.size();
		if (n > 5)
			n = 5;

		for (int i = 0; i < n; ++i)
		{
			String s = workingDirs.get(i).toString() + '\n';
			stream.write(s.getBytes("UTF-8"));
		}
	}

	@Override
	public void unpack(InputStream stream) throws IOException
	{
		workingDirs.clear();

		BufferedReader in = new BufferedReader(new InputStreamReader(stream, "UTF-8"));
		BufferedReader br = new BufferedReader(in);
		String line;

		while ((line = br.readLine()) != null)
		{
			Path p = new Path(line);
			workingDirs.add(p);
		}
	}
}
