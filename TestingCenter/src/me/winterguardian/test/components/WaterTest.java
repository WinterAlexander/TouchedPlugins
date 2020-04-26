package me.winterguardian.test.components;

import me.winterguardian.test.TestComponent;
import me.winterguardian.test.TestingCenter;
import org.bukkit.command.CommandSender;
import org.bukkit.craftbukkit.v1_8_R3.entity.CraftPlayer;

public class WaterTest extends TestComponent
{
	public WaterTest()
	{
		super("inWater", TestingCenter.TEST_LEVEL4, "Nah");
	}

	@Override
	public boolean onSubCommand(CommandSender sender, String label, String[] args)
	{
		if(!(sender instanceof CraftPlayer))
			return false;
		
		sender.sendMessage("You");
		sender.sendMessage("inWater: " + ((CraftPlayer)sender).getHandle().inWater);
		if(((CraftPlayer)sender).getHandle().vehicle != null)
		{
			sender.sendMessage("Vehicle");
			sender.sendMessage("inWater: " + ((CraftPlayer)sender).getHandle().vehicle.inWater);
		}
		if(((CraftPlayer)sender).getHandle().passenger != null)
		{
			sender.sendMessage("Passenger");
			sender.sendMessage("inWater: " + ((CraftPlayer)sender).getHandle().passenger.inWater);
		}
		return true;
	}
	
}
