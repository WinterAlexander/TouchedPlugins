package me.winterguardian.commandcenter.commands;

import me.winterguardian.core.command.AutoRegistrationCommand;
import me.winterguardian.core.message.ErrorMessage;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.permissions.Permission;
import org.bukkit.permissions.PermissionDefault;

import java.util.Arrays;
import java.util.List;

public class SudoCommand extends AutoRegistrationCommand
{

	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args)
	{
		if(args.length < 2)
			return false;
		
		Player player = Bukkit.getPlayer(args[0]);
		
		if(player == null)
		{
			ErrorMessage.COMMAND_INVALID_PLAYER.say(sender);
			return false;
		}
		
		String command = "";
		
		for(int i = 1; i < args.length; i++)
			command += args[i] + (i != args.length - 1 ? " " : "");
		
		
		if(command.startsWith("/"))
			player.performCommand(command.substring(1, command.length()));
		else
			player.chat(command);
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
		return Arrays.asList("force");
	}

	@Override
	public String getDescription()
	{
		return "Permet d'éxécuter des commandes à la place des autres.";
	}

	@Override
	public String getName()
	{
		return "sudo";
	}

	@Override
	public Permission getPermission()
	{
		return new Permission("CommandCenter.sudo", getDescription(), PermissionDefault.OP);
	}

	@Override
	public String getUsage()
	{
		return "/sudo <player> <command ...>";
	}

}
