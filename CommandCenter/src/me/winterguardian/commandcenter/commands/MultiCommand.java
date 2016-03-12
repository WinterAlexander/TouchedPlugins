package me.winterguardian.commandcenter.commands;

import java.util.Arrays;
import java.util.List;

import me.winterguardian.core.command.AutoRegistrationCommand;
import me.winterguardian.core.message.ErrorMessage;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;
import org.bukkit.permissions.Permission;
import org.bukkit.permissions.PermissionDefault;

public class MultiCommand extends AutoRegistrationCommand
{

	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args)
	{
		if(args.length == 0)
		{
			ErrorMessage.COMMAND_INVALID_ARGUMENT.say(sender);
			return false;
		}
		
		String allCommands = args[0];
		for(int i = 1; i < args.length; i++)
			allCommands += " " + args[i];
		
		for(String command : allCommands.split("\\s*&&\\s*"))
		{
			if(sender instanceof Player)
			{
				PlayerCommandPreprocessEvent e = new PlayerCommandPreprocessEvent((Player) sender, "/" + command);
				
				Bukkit.getPluginManager().callEvent(e);
				
				if(!e.isCancelled())
					((Player)sender).performCommand(e.getMessage().substring(1));
			}
			else
			{
				Bukkit.dispatchCommand(sender, command);
			}
		}
		
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
		return Arrays.asList("multicmd", "multi-command");
	}

	@Override
	public String getDescription()
	{
		return "Permet d'éxécuter plusieurs commandes à la fois.";
	}

	@Override
	public String getName()
	{
		return "multicommand";
	}

	@Override
	public Permission getPermission()
	{
		return new Permission("CommandCenter.multi-command", "Permet d'éxécuter plusieurs commandes à la fois.", PermissionDefault.OP);
	}

	@Override
	public String getUsage()
	{
		return "§cSyntaxe: §f/multicommand <command1> [command1 args...] && <command2> [command2 args...] &&...";
	}

}
