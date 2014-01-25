package xoric.prism.server.db.mongo;

import java.net.UnknownHostException;
import java.util.Date;

import xoric.prism.data.types.Text;
import xoric.prism.server.data.Account;

import com.mongodb.DB;
import com.mongodb.MongoClient;

public class MongoLoader
{
	private static MongoClient mongoClient;

	public static void loadAll() throws UnknownHostException
	{
		// initialize MongoClient (thread safe)
		mongoClient = new MongoClient("localhost");
		DB dataBase = mongoClient.getDB("prism");
		//		boolean auth = dataBase.authenticate(myUserName, myPassword);

		// initialize AccManager
		AccManager am = new AccManager(dataBase);

		String name = "lara";
		Account a = new Account(new Text(name), new byte[32], name + "@gmx.de", new Date(), new Date());
		am.createAccount(a);
		//		am.findAccount(a.getAccName());
		//
		//		List<String> list = mongoClient.getDatabaseNames();
		//		System.out.print("databases: ");
		//		for (String s : list)
		//			System.out.print(s + ", ");
		//		System.out.println(" (" + list.size() + ")");
		//		// ----------------
		//		DB db = mongoClient.getDB("test");
		//		//			boolean auth = db.authenticate(myUserName, myPassword);
		//
		//		Set<String> list2 = db.getCollectionNames();
		//		System.out.print("collections: ");
		//		for (String s : list2)
		//			System.out.print(s + ", ");
		//		System.out.println(" (" + list2.size() + ")");
		//		// ----------------
		//		DBCollection coll = db.getCollection("test");
		//		long n = coll.count();
		//		System.out.println("collection count: " + n);
		//		// ----------------
		//
		//		System.out.print("done");
	}

	public static void main(String[] args)
	{
		try
		{
			loadAll();
		}
		catch (UnknownHostException e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
