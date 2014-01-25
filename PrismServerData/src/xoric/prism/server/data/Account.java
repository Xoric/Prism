package xoric.prism.server.data;

import java.util.Date;

import xoric.prism.data.types.IText_r;

public class Account implements IAccount
{
	private final IText_r name;
	private final byte[] pw;
	private final String email;
	private final Date creationDate;
	private final Date lastSeenDate;

	public Account(IText_r name, byte[] pw, String email, Date creationDate, Date lastSeenDate)
	{
		this.name = name;
		this.pw = pw;
		this.email = email;
		this.creationDate = creationDate;
		this.lastSeenDate = creationDate;
	}

	@Override
	public IText_r getAccName()
	{
		return name;
	}

	@Override
	public byte[] getPassword()
	{
		return pw;
	}

	@Override
	public String getEmail()
	{
		return email;
	}

	@Override
	public Date getDate()
	{
		return creationDate;
	}

	@Override
	public String toString()
	{
		return name.toString();
	}
}
