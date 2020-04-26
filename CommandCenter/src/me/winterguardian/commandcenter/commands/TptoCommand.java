package me.winterguardian.commandcenter.commands;

import me.winterguardian.core.command.AutoRegistrationCommand;
import me.winterguardian.core.message.ErrorMessage;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.permissions.Permission;
import org.bukkit.permissions.PermissionDefault;

import java.util.Arrays;
import java.util.List;

public class TptoCommand extends AutoRegistrationCommand
{

	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args)
	{
		if(args.length == 0)
		{
			ErrorMessage.COMMAND_INVALID_ARGUMENT.say(sender);
			return false;
		}
		
		if(!(sender instanceof Player))
		{
			ErrorMessage.COMMAND_INVALID_SENDER.say(sender);
			return true;
		}
		
		Location loc = null;
		
		Player player;
		if(loc == null && (player = Bukkit.getPlayer(args[0])) != null)
			loc = player.getLocation(); 
		
		if(loc == null)
		{
			ErrorMessage.COMMAND_INVALID_PLAYER.say(sender);
			return false;
		}
		
		((Player)sender).teleport(loc);
		return true;
	}

	@Override
	public List<String> onTabComplete(CommandSender sender, Command cmd, String label, String[] args)
	{
		return null;
	}

	@Override
	public String getName()
	{
		return "tpto";
	}

	@Override
	public Permission getPermission()
	{
		return new Permission("CommandCenter.teleport.tpto", getDescription(), PermissionDefault.OP);
	}

	@Override
	public List<String> getAliases()
	{
		return Arrays.asList("tptohim", "tptoher", "teleportto");
	}

	@Override
	public String getUsage()
	{
		return "/tpto <player>";
	}

	@Override
	public String getDescription()
	{
		return "Une commande pour se téléporter à quelqu'un.";
	}
}
