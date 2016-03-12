package me.winterguardian.commandcenter.commands;

import java.util.Arrays;
import java.util.List;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.permissions.Permission;
import org.bukkit.permissions.PermissionDefault;

import me.winterguardian.core.Core;
import me.winterguardian.core.command.AutoRegistrationCommand;
import me.winterguardian.core.message.ErrorMessage;

public class ExecuteCommand extends AutoRegistrationCommand {

	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args)
	{
		if(args.length < 2)
		{
			ErrorMessage.COMMAND_INVALID_ARGUMENT.say(sender);
			return true;
		}
		
		String command = args[1];

		for(int i = 2; i < args.length; i++)
			command += " " + args[i];

		if(args[0].equalsIgnoreCase("*") || args[0].equalsIgnoreCase("all"))
		{
			Core.getBungeeMessager().executeEverywhere(command);
			return true;
		}

		Core.getBungeeMessager().execute(args[0], command);
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
		return "execute";
	}

	@Override
	public List<String> getAliases()
	{
		return Arrays.asList("exec", "exe");
	}
	
	@Override
	public Permission getPermission()
	{
		return new Permission("CommandCenter.execute", getDescription(), PermissionDefault.OP);
	}

	@Override
	public String getUsage()
	{
		return "/execute <server|*>";
	}

	@Override
	public String getDescription()
	{
		return "Permer d'éxécuter des commandes sur un autre serveur ou tous à la fois.";
	}

}
