package xoric.prism.com;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

abstract class PasswordHash
{
	public static final String hashFunction = "SHA-256";
	public static final int bufferSize = 32;

	public static byte[] hash(String password) throws NoSuchAlgorithmException
	{
		MessageDigest sha256 = MessageDigest.getInstance(hashFunction);
		byte[] passBytes = password.getBytes();
		byte[] passHash = sha256.digest(passBytes);
		return passHash;
	}
}
