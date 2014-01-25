package xoric.prism.server.db;

import xoric.prism.data.exceptions.PrismException;
import xoric.prism.data.exceptions.UserErrorText;
import xoric.prism.data.types.IText_r;
import xoric.prism.server.data.Account;
import xoric.prism.server.data.IAccount;

public interface IAccManager
{
	public void initializeDatabase();

	public UserErrorText createAccount(IAccount acc) throws PrismException;

	public boolean findAccount(IText_r acc);

	/**
	 * Performs a login attempt with the given account data. Returns an {@link Account} object if successful or {@link UserErrorText}
	 * otherwise.
	 * @param acc
	 * @param pw
	 * @return Object ({@link Account} or {@link UserErrorText})
	 * @throws PrismException
	 */
	public Object login(IText_r acc, byte[] pw) throws PrismException;
}
