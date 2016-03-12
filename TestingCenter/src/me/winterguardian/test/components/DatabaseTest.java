package me.winterguardian.test.components;

import me.winterguardian.test.TestComponent;
import me.winterguardian.test.TestingCenter;

import org.bukkit.command.CommandSender;

import com.avaje.ebean.CallableSql;
import com.avaje.ebean.EbeanServer;

public class DatabaseTest extends TestComponent
{
	public DatabaseTest()
	{
		super("db", TestingCenter.TEST_LEVEL4, "");
	}
	
	@Override
	public boolean onSubCommand(CommandSender sender, String label, String[] args)
	{
		if(TestingCenter.getInstance().getDatabase() == null)
		{
			sender.sendMessage("database disabled");
			return true;
		}
		
		EbeanServer db = TestingCenter.getInstance().getDatabase();
		
		if(args.length == 0)
			return false;
		
		String request = args[0];
		
		for(int i = 1; i < args.length; i++)
			request += " " + args[1];
		
		CallableSql sql = db.createCallableSql(request);
		sender.sendMessage("" + db.execute(sql));
		return true;
	}
}
