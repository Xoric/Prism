package xoric.prism.constants;

/**
 * @author XoricLee
 * @since 17.02.2014, 19:53:42
 */
public class Constants
{
	public static final int accNameMinLength = 2;
	public static final int accNameMaxLength = 14;

	public static final int pwMinLength = 8;
	public static final int pwMaxLength = 20;

	public static final int charNameMinLength = 2;
	public static final int charNameMaxLength = 10;

	public static boolean checkAccountLength(int n)
	{
		return n >= accNameMinLength && n <= accNameMaxLength;
	}

	public static boolean checkPasswordLength(int n)
	{
		return n >= pwMinLength && n <= pwMaxLength;
	}

	public static boolean checkCharNameLength(int n)
	{
		return n >= charNameMinLength && n <= charNameMaxLength;
	}
}
