package xoric.prism.server.db.mongo;

import java.util.Date;

import xoric.prism.data.exceptions.UserErrorText;
import xoric.prism.data.types.IText_r;
import xoric.prism.server.data.Account;
import xoric.prism.server.data.IAccount;
import xoric.prism.server.db.IAccManager;

import com.mongodb.BasicDBObject;
import com.mongodb.DB;
import com.mongodb.DBCollection;
import com.mongodb.DBObject;
import com.mongodb.WriteResult;

public class AccManager implements IAccManager
{
	private static final String accField = "acc";
	private static final String pwField = "pw";
	private static final String emailField = "email";
	private static final String createdField = "created";
	private static final String lastSeenField = "lastSeen";
	private static final String loginsField = "logins";

	private final DBCollection collection;

	public AccManager(DB dataBase)
	{
		this.collection = dataBase.getCollection("accounts");
	}

	@Override
	public void initializeDatabase()
	{
		collection.createIndex(new BasicDBObject(accField, 1));
	}

	@Override
	public UserErrorText createAccount(IAccount acc)
	{
		UserErrorText error = null;
		boolean exists = findAccount(acc.getAccName());

		if (!exists)
		{
			BasicDBObject doc = new BasicDBObject();
			doc.append(accField, acc.getAccName().toString());
			doc.append(pwField, acc.getPassword());
			doc.append(emailField, acc.getEmail());
			Date d = acc.getDate();
			doc.append(createdField, d);
			doc.append(lastSeenField, d);
			doc.append(loginsField, 0);

			WriteResult r = collection.insert(doc);

			if (r.getError() != null)
				error = UserErrorText.ACCOUNT_COULD_NOT_BE_CREATED;
		}
		else
		{
			error = UserErrorText.ACCOUNT_ALREADY_EXISTS;
		}
		return error;
	}

	@Override
	public boolean findAccount(IText_r acc)
	{
		BasicDBObject ref = new BasicDBObject();
		ref.append(accField, acc.toString());

		DBObject doc = collection.findOne(ref);
		return doc != null;
	}

	@Override
	public Object login(IText_r acc, byte[] pw)
	{
		// extract account name
		BasicDBObject ref = new BasicDBObject();
		ref.append(accField, acc.toString());
		DBObject doc = collection.findOne(ref);
		boolean b = doc != null;

		// if account was found, check password
		if (b)
		{
			byte[] pw0 = (byte[]) doc.get(pwField);
			b = pw != null && pw0 != null && pw.length == pw0.length && pw.length == 32;
			if (b)
				for (int i = 0; i < pw.length; ++i)
					b &= pw[i] == pw0[i];
		}

		// create Account object or set UserErrorText
		Object result = null;
		if (b)
		{
			// load account data
			String email = (String) doc.get(emailField);
			Date created = (Date) doc.get(createdField);
			Date lastSeen = (Date) doc.get(lastSeenField);

			// create Account object
			result = new Account(acc, pw, email, created, lastSeen);

			// TODO: 
			// kick any player that is currently logged on to this account (in server app)

			// finally: update some information in the database
			BasicDBObject upd = new BasicDBObject();
			upd.append("$set", new BasicDBObject().append(lastSeenField, new Date()));
			upd.append("$inc", new BasicDBObject().append(loginsField, 1));
			collection.update(ref, upd);
		}
		else
		{
			// set UserErrorText if no Account object was created
			result = UserErrorText.ACCOUNT_LOGIN_FAILED;
		}

		// return the resulting object
		return result;
	}
}
