package me.winterguardian.commandcenter.commands;

import me.winterguardian.core.command.AutoRegistrationCommand;
import me.winterguardian.core.message.ErrorMessage;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.permissions.Permission;
import org.bukkit.permissions.PermissionDefault;
import org.bukkit.potion.PotionEffect;

import java.util.Arrays;
import java.util.List;

public class SetHealthCommand extends AutoRegistrationCommand
{
	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args)
	{
		if(args.length == 0)
		{
			if(!(sender instanceof Player))
				return false;
			
			args = new String[]{sender.getName()};
		}

		Player player = Bukkit.getPlayer(args[0]);

		if(player == null)
		{
			ErrorMessage.COMMAND_INVALID_PLAYER.say(sender);
			return false;
		}

		double health;
		int food;

		try
		{
			health = Double.parseDouble(args[1]);
		}
		catch(Exception e)
		{
			health = player.getMaxHealth();
		}

		try
		{   food = Integer.parseInt(args[2]);
		}
		catch(Exception e)
		{   food = 20;
		}

		player.setHealth(health);
		player.setFoodLevel(food);
		player.setExhaustion(0);

		for(PotionEffect effect : player.getActivePotionEffects())
			player.removePotionEffect(effect.getType());
		
		return true;
	}

	@Override
	public List<String> onTabComplete(CommandSender sender, Command cmd, String label, String[] args)
	{
		return null;
	}

	@Override
	public List<String> getAliases()
	{
		return Arrays.asList("heal", "feed", "health", "setfoodlevel");
	}

	@Override
	public String getDescription()
	{
		return "Permet de changer la vie de soi même ou de d'autres joueurs.";
	}

	@Override
	public String getName()
	{
		return "sethealth";
	}

	@Override
	public Permission getPermission()
	{
		return new Permission("CommandCenter.sethealth", getDescription(), PermissionDefault.OP);
	}

	@Override
	public String getUsage()
	{
		return "§cSyntaxe: /" + getName() + " [player] [health] [food]";
	}

}
