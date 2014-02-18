package xoric.prism.server.db;

/**
 * @author XoricLee
 * @since 18.02.2014, 16:10:50
 */
public abstract class Database
{
	public Database(IAccManager accounts)
	{
		this.accounts = accounts;
	}

	public final IAccManager accounts;
}
