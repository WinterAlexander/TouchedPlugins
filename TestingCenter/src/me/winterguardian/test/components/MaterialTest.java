package me.winterguardian.test.components;

import me.winterguardian.test.TestComponent;
import me.winterguardian.test.TestingCenter;
import org.bukkit.Material;
import org.bukkit.command.CommandSender;

public class MaterialTest extends TestComponent
{
	public MaterialTest()
	{
		super("materialtest", TestingCenter.TEST_LEVEL2, "/materialtest <material>");
	}

	@Override
	public boolean onSubCommand(CommandSender sender, String label, String args[])
	{
		if(args.length < 1)
			return false;
		
		Material material = Material.getMaterial(args[0].toUpperCase());
		
		if(material == null)
			return false;
		
		sender.sendMessage("isBlock: " + material.isBlock());
		sender.sendMessage("isBurnable: " + material.isBurnable());
		sender.sendMessage("isEdible: " + material.isEdible());
		sender.sendMessage("isFlammable: " + material.isFlammable());
		sender.sendMessage("isOccluding: " + material.isOccluding());
		sender.sendMessage("isRecord: " + material.isRecord());
		sender.sendMessage("isTransparent: " + material.isTransparent());
		
		return true;
	}
	
}
