package me.winterguardian.commandcenter.commands;

import java.util.Arrays;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.block.BlockFace;
import org.bukkit.command.BlockCommandSender;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.permissions.Permission;
import org.bukkit.permissions.PermissionDefault;

import me.winterguardian.core.command.AutoRegistrationCommand;
import me.winterguardian.core.message.ErrorMessage;

public class TphereCommand extends AutoRegistrationCommand
{
	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args)
	{
		if(args.length == 0)
		{
			ErrorMessage.COMMAND_INVALID_ARGUMENT.say(sender);
			return false;
		}
		
		Location loc;
		if(sender instanceof Player)
			loc = ((Player)sender).getLocation();
		else if(sender instanceof BlockCommandSender)
			loc = ((BlockCommandSender)sender).getBlock().getRelative(BlockFace.UP).getLocation();
		else
		{
			ErrorMessage.COMMAND_INVALID_SENDER.say(sender);
			return true;
		}
		
		
		Player player;
		if((player = Bukkit.getPlayer(args[0])) == null)
		{
			ErrorMessage.COMMAND_INVALID_PLAYER.say(sender);
			return false;
		}
		
		player.teleport(loc);
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
		return "tphere";
	}

	@Override
	public Permission getPermission()
	{
		return new Permission("CommandCenter.teleport.tphere", getDescription(), PermissionDefault.OP);
	}

	@Override
	public List<String> getAliases()
	{
		return Arrays.asList("s", "tphim", "tpher", "teleporthere");
	}

	@Override
	public String getUsage()
	{
		return "/tphere <player>";
	}

	@Override
	public String getDescription()
	{
		return "Une commande pour téléporter quelqu'un à soi même.";
	}

}
