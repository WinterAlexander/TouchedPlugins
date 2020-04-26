package me.winterguardian.test.components;

import me.winterguardian.core.entity.custom.rideable.RideableEntityUtil;
import me.winterguardian.test.TestComponent;
import me.winterguardian.test.TestingCenter;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;

public class MountTest extends TestComponent
{
	public MountTest()
	{
		super("mount", TestingCenter.TEST_LEVEL2, "Nah");
	}
	
	@Override
	public boolean onSubCommand(CommandSender sender, String label, String[] args)
	{
		if(!(sender instanceof Player))
			return false;
		
		Player p = (Player) sender;
		
		if(args.length == 2 && args[0].equalsIgnoreCase("spawnmount"))
		{
			RideableEntityUtil.spawnRideable(EntityType.valueOf(args[1]), p.getLocation());
			sender.sendMessage("done");
			return true;
		}
		
		Entity entity = p.getNearbyEntities(2, 2, 2).get(0);
		
		if(entity == null || entity == p)
		{
			sender.sendMessage("no entity");
			return true;
		}
		
		if(args[0].equalsIgnoreCase("mount"))
		{
			entity.setPassenger(p);
			sender.sendMessage("done");
			return true;
		}
		
		if(args.length < 2)
			return false;
		
		if(!RideableEntityUtil.isRideable(entity))
		{
			sender.sendMessage("Invalid mount");
			return true;
		}
		
		if(args[0].equalsIgnoreCase("setspeed"))
		{
			RideableEntityUtil.setSpeed(entity, Float.parseFloat(args[1]));
			sender.sendMessage("done");
			return true;
		}
		
		if(args[0].equalsIgnoreCase("setbackspeed"))
		{
			RideableEntityUtil.setBackwardSpeed(entity, Float.parseFloat(args[1]));
			sender.sendMessage("done");
			return true;
		}
		
		if(args[0].equalsIgnoreCase("setsidespeed"))
		{
			RideableEntityUtil.setSidewaySpeed(entity, Float.parseFloat(args[1]));
			sender.sendMessage("done");
			return true;
		}
		
		
		if(args[0].equalsIgnoreCase("setjump"))
		{
			RideableEntityUtil.setJumpHeight(entity, Float.parseFloat(args[1]));
			sender.sendMessage("done");
			return true;
		}
		
		if(args[0].equalsIgnoreCase("setclimb"))
		{
			RideableEntityUtil.setClimbHeight(entity, Float.parseFloat(args[1]));
			sender.sendMessage("done");
			return true;
		}
		
		if(args[0].equalsIgnoreCase("setthrust"))
		{
			RideableEntityUtil.setJumpThrust(entity, Float.parseFloat(args[1]));
			sender.sendMessage("done");
			return true;
		}
		
		
		return false;
	}
}
