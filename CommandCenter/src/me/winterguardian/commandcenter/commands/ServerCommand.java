package me.winterguardian.commandcenter.commands;

import java.util.Arrays;
import java.util.List;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.permissions.Permission;
import org.bukkit.permissions.PermissionDefault;

import me.winterguardian.commandcenter.CommandCenterMessage;
import me.winterguardian.core.Core;
import me.winterguardian.core.command.AutoRegistrationCommand;
import me.winterguardian.core.message.ErrorMessage;

public class ServerCommand extends AutoRegistrationCommand
{
	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args)
	{
		if(args.length < 1)
		{
			ErrorMessage.COMMAND_INVALID_ARGUMENT.say(sender);
			return true;
		}
		
		if(!(sender instanceof Player))
		{
			ErrorMessage.COMMAND_INVALID_SENDER.say(sender);
			return true;
		}
		
		Core.getBungeeMessager().sendToServer((Player) sender, args[0]);
		CommandCenterMessage.SERVER_SENDING.say(sender, "<server>", args[0]);
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
		return "server";
	}

	@Override
	public Permission getPermission()
	{
		return new Permission("CommandCenter.server", getDescription(), PermissionDefault.TRUE);
	}

	@Override
	public String getUsage()
	{
		return "/server [server]";
	}

	@Override
	public String getDescription()
	{
		return "Commande côté spigot pour changer de serveur.";
	}

	@Override
	public List<String> getAliases()
	{
		return Arrays.asList("serveur", "goto");
	}
}
