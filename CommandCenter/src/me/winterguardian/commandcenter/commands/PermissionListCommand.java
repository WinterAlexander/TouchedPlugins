package me.winterguardian.commandcenter.commands;

import me.winterguardian.core.command.AutoRegistrationCommand;
import me.winterguardian.core.message.ErrorMessage;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.permissions.Permission;
import org.bukkit.permissions.PermissionDefault;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class PermissionListCommand extends AutoRegistrationCommand
{
	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args)
	{
		List<Permission> perms = new ArrayList<Permission>();
		String prefix = null;
		
		if(args.length == 2)
			prefix = args[1];
		
		for(Object perm : Bukkit.getPluginManager().getPermissions().toArray())
			if(prefix == null || ((Permission) perm).getName().startsWith(prefix))
				perms.add((Permission) perm);
		
		int page = 1;
		int pagemax = perms.size() / 10 + (perms.size() % 10 == 0 ? 0 : 1);
		
		
		
		if(args.length >= 1)
			try
			{
				page = Integer.parseInt(args[0]);
			}
			catch(Exception e)
			{
				ErrorMessage.COMMAND_INVALID_ARGUMENT.say(sender);
				return false;
			}
			

		if(args.length > 2 || page < 1 || page > pagemax)
		{
			ErrorMessage.COMMAND_INVALID_ARGUMENT.say(sender);
			return false;
		}
		
		sender.sendMessage("§6§lPermissions: §cpage " + page + " sur " + pagemax);
		
		for(int i = 0; i < perms.size(); i++)
			for(int j = 0; j < perms.size(); j++)
				if(perms.get(i).getName().compareTo(perms.get(j).getName()) < 0)
				{
					Permission temp = perms.get(i);
					perms.set(i, perms.get(j));
					perms.set(j, temp);
				}
		
		
		for(int i = 10 * (page - 1); i < 10 * (page - 1) + 10 && i < perms.size(); i++)
			sender.sendMessage("§e" + perms.get(i).getName() + ": §f" + perms.get(i).getDescription());
		
		return true;
	}

	@Override
	public List<String> onTabComplete(CommandSender arg0, Command arg1, String arg2, String[] args)
	{
		return null;
	}

	@Override
	public List<String> getAliases()
	{
		return Arrays.asList("permlist");
	}

	@Override
	public String getDescription()
	{
		return "Affiche la liste des permissions du serveur.";
	}

	@Override
	public String getName()
	{
		return "permissionlist";
	}

	@Override
	public Permission getPermission()
	{
		return new Permission("CommandCenter.permission-list", "Permet de voir la liste complète des permissions du serveur.", PermissionDefault.OP);
	}

	@Override
	public String getUsage()
	{
		return "§cSyntaxe: §f/permissionlist [page]";
	}

}
