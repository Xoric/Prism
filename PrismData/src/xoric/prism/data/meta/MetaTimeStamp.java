package xoric.prism.data.meta;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Calendar;

import xoric.prism.data.packable.IPackable;

public class MetaTimeStamp implements IPackable
{
	private int year;
	private int month;
	private int day;
	private int hour;

	public MetaTimeStamp()
	{
		Calendar c = Calendar.getInstance();
		year = c.get(Calendar.YEAR);
		month = c.get(Calendar.MONTH) + 1;
		day = c.get(Calendar.DAY_OF_MONTH);
		hour = c.get(Calendar.HOUR_OF_DAY);
	}

	@Override
	public String toString()
	{
		return year + "/" + month + "/" + day + "-" + hour;
	}

	@Override
	public void pack(OutputStream stream) throws IOException
	{
		// [YYYYYYYY] [YY|MMMM|DD] [DDD|HHHHH]
		int i = hour | (day << 5) | (month << 10) | ((year - 2013) << 14);
		stream.write(i);
		stream.write(i >> 8);
		stream.write(i >> 16);
	}

	@Override
	public void unpack(InputStream stream) throws IOException
	{
		int i = stream.read();
		i |= stream.read() << 8;
		i |= stream.read() << 16;

		hour = i & 0x1F;
		day = (i >> 5) & 0x1F;
		month = (i >> 10) & 0xF;
		year = (i >> 14) + 2013;
	}

	//	@Override
	//	public int getPackedSize()
	//	{
	//		return 3;
	//	}
}
