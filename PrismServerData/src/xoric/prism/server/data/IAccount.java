package xoric.prism.server.data;

import java.util.Date;

import xoric.prism.data.types.IText_r;

public interface IAccount
{
	public IText_r getAccName();

	public byte[] getPassword();

	public String getEmail();

	public Date getDate();
}
