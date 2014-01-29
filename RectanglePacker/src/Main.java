import com.ryanm.droid.rugl.util.RectanglePacker2;

public class Main
{
	public static void main(String[] args)
	{
		RectanglePacker2 p = new RectanglePacker2(400, 300, 0);

		String s0 = "0";
		String s1 = "1";

		p.insert(200, 80, s0);
		p.insert(300, 10, s1);

		RectanglePacker2.Rectangle r0 = p.findRectangle(s0);
		RectanglePacker2.Rectangle r1 = p.findRectangle(s1);

		System.out.println(r0.toString());
		System.out.println(r1.toString());
	}
}
