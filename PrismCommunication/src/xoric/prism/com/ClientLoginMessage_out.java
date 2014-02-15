package xoric.prism.com;

import java.io.IOException;
import java.io.OutputStream;
import java.security.NoSuchAlgorithmException;

import xoric.prism.data.exceptions.PrismException;
import xoric.prism.data.exceptions.UserErrorText;
import xoric.prism.data.packable.HeapPacker_s;
import xoric.prism.data.types.IText_r;

public class ClientLoginMessage_out extends Message_out
{
	private byte[] passwordBuffer;

	public ClientLoginMessage_out()
	{
		super(Token.LOGIN);
	}

	public void setPassword(IText_r pw) throws PrismException
	{
		try
		{
			passwordBuffer = PasswordHash.hash(pw.toString());
		}
		catch (NoSuchAlgorithmException e0)
		{
			PrismException e = new PrismException(e0);
			// ----
			e.user.setText(UserErrorText.PASSWORD_HASH);
			// ----
			e.code.setText("error hashing a user password using " + PasswordHash.hashFunction);
			// ----
			throw e;
		}

		if (passwordBuffer == null || passwordBuffer.length != PasswordHash.bufferSize)
		{
			PrismException e = new PrismException();
			// ----
			e.user.setText(UserErrorText.PASSWORD_HASH);
			// ----
			int n = passwordBuffer == null ? 0 : passwordBuffer.length;
			e.code.setText("password buffer has unexpected size", PasswordHash.bufferSize, n);
			// ----
			throw e;
		}
	}

	@Override
	protected int calcPackedSize() throws PrismException
	{
		return super.calcPackedSize() + PasswordHash.bufferSize;
	}

	@Override
	public void pack(OutputStream stream) throws IOException, PrismException
	{
		writeHeader(stream);
		stream.write(token.ordinal());
		// ---
		stream.write(passwordBuffer, 0, PasswordHash.bufferSize);
		// ---
		HeapPacker_s.pack_s(stream, heap, token.getFloatDecimals());
	}
}
