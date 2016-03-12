package me.winterguardian.test.components;

import me.winterguardian.test.TestComponent;
import me.winterguardian.test.TestingCenter;

import org.bukkit.command.CommandSender;
import org.bukkit.craftbukkit.v1_8_R3.entity.CraftEntity;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;

public class EntityMethods extends TestComponent
{
	public EntityMethods()
	{
		super("entity", TestingCenter.TEST_LEVEL2, "NAHNAHNAH");
	}
	
	@Override
	public boolean onSubCommand(CommandSender sender, String label, String[] args)
	{
		if(!(sender instanceof Player))
			return false;
		
		Player p = (Player) sender;
		
		Entity entity = p.getNearbyEntities(2, 2, 2).get(0);
		
		if(entity == null || entity == p)
		{
			sender.sendMessage("no entity");
			return true;
		}
		
		if(args[0].equalsIgnoreCase("setloc") && args.length == 6)
		{	
			((CraftEntity)entity).getHandle().setLocation(Double.parseDouble(args[1]), Double.parseDouble(args[2]), Double.parseDouble(args[3]), Float.parseFloat(args[4]), Float.parseFloat(args[5]));
			sender.sendMessage("done");
			return true;
		}
		
		if(args[0].equalsIgnoreCase("setpos") && args.length == 6)
		{	
			((CraftEntity)entity).getHandle().setPositionRotation(Double.parseDouble(args[1]), Double.parseDouble(args[2]), Double.parseDouble(args[3]), Float.parseFloat(args[4]), Float.parseFloat(args[5]));
			sender.sendMessage("done");
			return true;
		}
		
		if(args[0].equalsIgnoreCase("getloc"))
		{
			sender.sendMessage("x:" + ((CraftEntity)entity).getHandle().locX);
			sender.sendMessage("y:" + ((CraftEntity)entity).getHandle().locY);
			sender.sendMessage("z:" + ((CraftEntity)entity).getHandle().locZ);
			sender.sendMessage("yaw:" + ((CraftEntity)entity).getHandle().yaw);
			sender.sendMessage("pitch:" + ((CraftEntity)entity).getHandle().pitch);
			return true;
		}
		
		return false;
	}
}
