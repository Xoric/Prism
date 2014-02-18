package xoric.prism.server.db.mongo;

import xoric.prism.server.db.Database;

import com.mongodb.DB;

/**
 * @author XoricLee
 * @since 18.02.2014, 16:09:09
 */
public class MongoDatabase extends Database
{
	public MongoDatabase(DB dataBase)
	{
		super(new AccManager(dataBase));
	}
}
