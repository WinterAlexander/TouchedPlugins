package me.winterguardian.hub;

import me.winterguardian.core.command.AutoRegistrationCommand;
import me.winterguardian.core.game.GameManager;
import me.winterguardian.core.message.ErrorMessage;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.permissions.Permission;

import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

public class HubCommand extends AutoRegistrationCommand
{
	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args)
	{
		if(args.length <= 0)
			if(sender instanceof Player)
			{
				GameManager.leaveAll((Player) sender);
				
				if(Hub.getPlugin().getHub() != null)
				{
					((Player)sender).teleport(Hub.getPlugin().getHub());
					sender.sendMessage("§7Téléportation vers le hub de §f§lTouched§6§lCraft§7...");
				}
				else
				{
					((Player)sender).teleport(Bukkit.getWorlds().get(0).getSpawnLocation());
					sender.sendMessage("§7Téléportation vers le spawn par défaut de §f§lTouched§6§lCraft§7...");
				}
			}
			else
				ErrorMessage.COMMAND_INVALID_SENDER.say(sender);
		else if(sender.hasPermission(Hub.TPOTHERS))
		{
			Player p = Bukkit.getPlayer(args[0]);
			if(p != null)
			{
				GameManager.leaveAll(p);
				
				if(Hub.getPlugin().getHub() != null)
				{
					p.teleport(Hub.getPlugin().getHub());
					p.sendMessage("§7Téléportation vers le hub de §f§lTouched§6§lCraft§7...");
				}
				else
				{
					p.teleport(Bukkit.getWorlds().get(0).getSpawnLocation());
					p.sendMessage("§7Téléportation vers le spawn par défaut de §f§lTouched§6§lCraft§7...");
				}
			}
			else
				ErrorMessage.COMMAND_INVALID_PLAYER.say(sender);
		}
		else
			ErrorMessage.COMMAND_INVALID_PERMISSION.say(sender);
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
		return Arrays.asList("spawn", "lobby");
	}

	@Override
	public String getDescription()
	{
		return "Permet d'aller au Hub.";
	}

	@Override
	public String getName()
	{
		return "hub";
	}

	@Override
	public Permission getPermission()
	{
		return Hub.TPTOHUB;
	}

	@Override
	public String getPermissionMessage()
	{
		return ErrorMessage.COMMAND_INVALID_PERMISSION.toString();
	}

	@Override
	public String getUsage()
	{
		return "§cSyntaxe: §f/hub [joueur]";
	}
	
	@Override
	public Collection<Permission> getOtherPermissions()
	{
		return Collections.singletonList(Hub.TPOTHERS);
	}
}
