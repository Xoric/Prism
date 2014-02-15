package xoric.prism.com;

import java.io.IOException;
import java.io.InputStream;

import xoric.prism.data.exceptions.PrismException;
import xoric.prism.data.packable.HeapPacker_s;

public class ClientLoginMessage_in extends Message_in
{
	private byte[] passwordBuffer;

	public ClientLoginMessage_in()
	{
		super(Token.LOGIN);
	}

	public byte[] getPassword()
	{
		return passwordBuffer;
	}

	@Override
	public void unpack(InputStream stream) throws IOException, PrismException
	{
		// first 3 bytes (startByte + size) are already read at this point
		int i = stream.read();
		token = Token.valueOf(i);
		// ---
		passwordBuffer = new byte[PasswordHash.bufferSize];
		stream.read(passwordBuffer);
		// ---
		HeapPacker_s.unpack_s(stream, token.getFloatDecimals(), heap);

		// ensure that the number of ints, floats and texts read is valid
		ensureMinima();
	}
}
