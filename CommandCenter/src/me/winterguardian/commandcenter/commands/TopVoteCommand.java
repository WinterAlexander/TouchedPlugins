package me.winterguardian.commandcenter.commands;

import java.util.List;

import me.winterguardian.core.command.AutoRegistrationCommand;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.permissions.Permission;

public class TopVoteCommand extends AutoRegistrationCommand
{
	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args)
	{
		return false;
	}

	@Override
	public List<String> onTabComplete(CommandSender sender, Command cmd, String label, String[] args)
	{
		return null;
	}

	@Override
	public List<String> getAliases()
	{
		return null;
	}

	@Override
	public String getDescription()
	{
		return null;
	}

	@Override
	public String getName()
	{
		return null;
	}

	@Override
	public Permission getPermission()
	{
		return null;
	}

	@Override
	public String getUsage()
	{
		return null;
	}

}
