package me.winterguardian.hub;

import me.winterguardian.core.Core;
import me.winterguardian.core.command.AutoRegistrationCommand;
import me.winterguardian.core.message.ErrorMessage;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.permissions.Permission;
import org.bukkit.permissions.PermissionDefault;

import java.util.Arrays;
import java.util.List;

public class SetHubCommand extends AutoRegistrationCommand
{
	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args)
	{
		if(sender instanceof Player)
		{
			if(args.length == 0)
			{
				Hub.getPlugin().setHub(((Player)sender).getLocation());
				sender.sendMessage("§7La position du hub a été placé.");
			}
			else if(args[0].equalsIgnoreCase("reception") || args[0].equalsIgnoreCase("accueil"))
			{
				Hub.getPlugin().setNewPlayersSpawn(((Player)sender).getLocation());
				sender.sendMessage("§7La position du spawn des nouveaux a été placé.");
			}
			else if(args[0].equalsIgnoreCase("region") || args[0].equalsIgnoreCase("région"))
			{
				Hub.getPlugin().setRegion(Core.getWand().getRegion((Player)sender));
				sender.sendMessage("§7La région a été placée.");
			}
		}
		else
			ErrorMessage.COMMAND_INVALID_SENDER.say(sender);
		return true;
	}

	@Override
	public List<String> onTabComplete(CommandSender arg0, Command arg1, String arg2, String[] arg3)
	{
		return null;
	}

	@Override
	public List<String> getAliases()
	{
		return Arrays.asList("setspawn", "setlobby");
	}

	@Override
	public String getDescription()
	{
		return "Permet de modifier la position du hub.";
	}

	@Override
	public String getName()
	{
		return "sethub";
	}

	@Override
	public Permission getPermission()
	{
		return new Permission("Hub.set", "Permet de set le hub", PermissionDefault.OP);
	}

	@Override
	public String getPermissionMessage()
	{
		return ErrorMessage.COMMAND_INVALID_PERMISSION.toString();
	}

	@Override
	public String getUsage()
	{
		return "§eSyntaxe: §f/sethub [reception]";
	}
}
