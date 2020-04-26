package me.winterguardian.test.components;

import me.winterguardian.core.message.ErrorMessage;
import me.winterguardian.test.TestComponent;
import me.winterguardian.test.TestingCenter;
import org.bukkit.Material;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

public class SpinningBlock extends TestComponent
{
	public SpinningBlock()
	{
		super("spinningblock", TestingCenter.TEST_LEVEL3, "/test spinningblock <blockid> <data>");
	}
	
	@SuppressWarnings("deprecation")
	@Override
	public boolean onSubCommand(CommandSender sender, String label, String[] args)
	{
		if(!(sender instanceof Player))
		{
			ErrorMessage.COMMAND_INVALID_SENDER.say(sender);
			return true;
		}
		
		if(args.length != 2)
		{
			ErrorMessage.COMMAND_INVALID_ARGUMENT.say(sender);
			return false;
		}
		
		Player p = (Player) sender;
		
		ArmorStand stand = (ArmorStand) p.getWorld().spawnEntity(p.getLocation(), EntityType.ARMOR_STAND);
		stand.setMaximumNoDamageTicks(Integer.MAX_VALUE);
		stand.setVisible(false);
		stand.setBasePlate(false);
		stand.setGravity(false);
		stand.setHelmet(new ItemStack(Material.getMaterial(Integer.parseInt(args[0])), 1, (short) Short.parseShort(args[1])));
		p.setPassenger(stand);
		
		return true;
	}
}
