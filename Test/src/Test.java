import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.OutputStream;
import java.util.zip.GZIPOutputStream;

public class Test
{

	public static void main(String[] args)
	{
		try
		{
			File source = new File("E:/Projekte/Java/_Trisma/TriRes/res/ground/tex0/tex.png");
			int n = (int) source.length();
			byte[] buf = new byte[n];
			FileInputStream in = new FileInputStream(source);
			in.read(buf);
			in.close();

			File dest = new File("E:/Temp/Test.txt");
			OutputStream out = new FileOutputStream(dest);
			GZIPOutputStream output = new GZIPOutputStream(out);
			output.write(buf);
			output.close();

			System.out.println(source.length() + " -> " + dest.length());

			double d0 = source.length();
			double d1 = dest.length();
			double d = 100.0 * d1 / d0;
			System.out.println(d + "%");
		}
		catch (Exception e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
