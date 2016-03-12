package me.winterguardian.hub;

import java.util.Arrays;
import java.util.Collection;
import java.util.List;

import me.winterguardian.core.command.AutoRegistrationCommand;
import me.winterguardian.core.message.ErrorMessage;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.permissions.Permission;

public class ReceptionCommand extends AutoRegistrationCommand
{
	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args)
	{
		if(args.length <= 0)
			if(sender instanceof Player)
			{
				if(Hub.getPlugin().getNewPlayersSpawn() != null)
				{
					((Player)sender).teleport(Hub.getPlugin().getNewPlayersSpawn());
					sender.sendMessage("§7Téléportation vers l'accueil de §f§lSekai§6§lMC§7...");
				}
				else
				{
					((Player)sender).teleport(Bukkit.getWorlds().get(0).getSpawnLocation());
					sender.sendMessage("§7Téléportation vers le spawn par défaut de §f§lSekai§6§lMC§7...");
				}
			}
			else
				ErrorMessage.COMMAND_INVALID_SENDER.say(sender);
		else if(sender.hasPermission(Hub.TPOTHERS))
		{
				Player p = Bukkit.getPlayer(args[0]);
				if(p != null)
					if(Hub.getPlugin().getNewPlayersSpawn() != null)
					{
						p.teleport(Hub.getPlugin().getNewPlayersSpawn());
						p.sendMessage("§7Téléportation vers l'accueil de §f§lSekai§6§lMC§7...");
					}
					else
					{
						p.teleport(Bukkit.getWorlds().get(0).getSpawnLocation());
						p.sendMessage("§7Téléportation vers le spawn par défaut de §f§lSekai§6§lMC§7...");
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
		return Arrays.asList("accueil", "réception", "newplayersspawn", "newplayerspawn", "spawn2", "info", "infos", "information", "informations", "spawnnouveau", "spawnnouveaux");
	}

	@Override
	public String getDescription()
	{
		return "Téléporte à l'accueil de Sekai.";
	}

	@Override
	public String getName()
	{
		return "reception";
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
		return "§eSyntaxe: §f/reception";
	}
	
	@Override
	public Collection<Permission> getOtherPermissions()
	{
		return Arrays.asList(Hub.TPOTHERS);
	}
}
