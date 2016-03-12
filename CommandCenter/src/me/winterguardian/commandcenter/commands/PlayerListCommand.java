package me.winterguardian.commandcenter.commands;

import java.util.Arrays;
import java.util.List;

import me.winterguardian.core.command.AutoRegistrationCommand;
import me.winterguardian.core.game.Game;
import me.winterguardian.core.game.GameManager;
import me.winterguardian.core.message.ErrorMessage;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.permissions.Permission;
import org.bukkit.permissions.PermissionDefault;

public class PlayerListCommand extends AutoRegistrationCommand
{
	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args)
	{
		if(args.length == 0)
		{
			for(Game game : GameManager.getGames())
			{
				sender.sendMessage(game.getColoredName() + " §7§l: §r" + game.getStatus());
				for(Player p : Bukkit.getOnlinePlayers())
					if(game.contains(p))
					{
						String prefix = game.getChatPrefix(p);
						if(prefix == null)
							prefix = "";
						
						String name = game.getChatName(p);
						if(name == null)
							name = p.getName();
						sender.sendMessage("  " + prefix + " §r" + name);
					}
			}
		}
		else
			sender.sendMessage(ErrorMessage.COMMAND_INVALID_ARGUMENT.toString());
		
		return true;
	}

	@Override
	public List<String> onTabComplete(CommandSender arg0, Command arg1, String arg2, String[] arg3)
	{
		return null;
	}

	@Override
	public List<String> getAliases()
	{
		return Arrays.asList("plist", "list", "worldlist");
	}

	@Override
	public String getDescription()
	{
		return "Permet de voir la liste des joueurs selon le jeu dans lequel ils jouent.";
	}

	@Override
	public String getName()
	{
		return "playerlist";
	}

	@Override
	public Permission getPermission()
	{
		return new Permission("CommandCenter.player-list", "Commande /playerlist", PermissionDefault.OP);
	}

	@Override
	public String getUsage()
	{
		return "/playerlist";
	}
}
