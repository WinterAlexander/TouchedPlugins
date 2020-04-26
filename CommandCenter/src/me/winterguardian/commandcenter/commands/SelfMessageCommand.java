package me.winterguardian.commandcenter.commands;

import me.winterguardian.core.command.AutoRegistrationCommand;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.permissions.Permission;

import java.util.Arrays;
import java.util.List;

public class SelfMessageCommand extends AutoRegistrationCommand
{
	@Override
	public boolean onCommand(CommandSender sender, Command command, String label, String[] args)
	{
		String message = "";
		
		for(String arg : args)
			message += arg + " ";
		
		if(message.length() > 1)
			message = message.substring(0, message.length() - 1);
		
		message = message.replaceAll("&", "§");
		
		sender.sendMessage(message);
		return true;
	}

	@Override
	public List<String> onTabComplete(CommandSender sender, Command command, String alias, String[] args)
	{
		return null;
	}

	@Override
	public List<String> getAliases()
	{
		return Arrays.asList("self", "smessage", "self-message", "self-messages", "smessages","selfmsg");
	}

	@Override
	public String getDescription()
	{
		return "Permet de s'envoyer un message à soi-même.";
	}

	@Override
	public String getName()
	{
		return "selfmessage";
	}

	@Override
	public Permission getPermission()
	{
		return null;
	}

	@Override
	public String getUsage()
	{
		return "§cSyntaxe: §f/selfmessage";
	}

}
