package me.winterguardian.commandcenter.commands;

import me.winterguardian.commandcenter.CommandCenterMessage;
import me.winterguardian.core.command.AutoRegistrationCommand;
import me.winterguardian.core.message.ErrorMessage;
import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.permissions.Permission;
import org.bukkit.permissions.PermissionDefault;

import java.util.Arrays;
import java.util.Collection;
import java.util.List;

public class SetGamemodeCommand extends AutoRegistrationCommand
{
	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args)
	{
		if(args.length == 0)
		{
			if(sender instanceof Player)
			{
				if(((Player) sender).getGameMode() == GameMode.CREATIVE)
					args = new String[]{"0"};
				else
					args = new String[]{"1"};
			}
			else
			{
				ErrorMessage.COMMAND_INVALID_ARGUMENT.say(sender);
				return false;
			}
		}
		
		Player p = null;
		String message = null;
		
		if(args.length == 2)
		{
			if(!sender.hasPermission(this.getOtherPermissions().iterator().next()))
			{
				ErrorMessage.COMMAND_INVALID_PERMISSION.say(sender);
				return true;
			}
			p = Bukkit.getPlayer(args[1]);
			message = CommandCenterMessage.SETGAMEMODE_OTHERS.toString();
		}
		else if(sender instanceof Player)
		{
			p = (Player) sender;
			message = CommandCenterMessage.SETGAMEMODE.toString();
		}
		
		if(p == null)
		{
			ErrorMessage.COMMAND_INVALID_PLAYER.say(sender);
			return false;
		}
		
		if(args[0].equalsIgnoreCase("survie") || args[0].equalsIgnoreCase("survival") || args[0].equalsIgnoreCase("0") || args[0].equalsIgnoreCase("S"))
		{
			p.setGameMode(GameMode.SURVIVAL);
			sender.sendMessage(message);
			return true;
		}

		if(args[0].equalsIgnoreCase("crea") || args[0].equalsIgnoreCase("créatif") || args[0].equalsIgnoreCase("creative") || args[0].equalsIgnoreCase("1") || args[0].equalsIgnoreCase("C"))
		{
			p.setGameMode(GameMode.CREATIVE);
			sender.sendMessage(message);
			return true;
		}
		
		if(args[0].equalsIgnoreCase("aventure") || args[0].equalsIgnoreCase("adventure") || args[0].equalsIgnoreCase("2") || args[0].equalsIgnoreCase("A"))
		{
			p.setGameMode(GameMode.ADVENTURE);
			sender.sendMessage(message);
			return true;
		}
		
		if(args[0].equalsIgnoreCase("spectate") || args[0].equalsIgnoreCase("spectateur") || args[0].equalsIgnoreCase("spectator") || args[0].equalsIgnoreCase("3") || args[0].equalsIgnoreCase("SP"))
		{
			p.setGameMode(GameMode.SPECTATOR);
			sender.sendMessage(message);
			return true;
		}
		
		ErrorMessage.COMMAND_INVALID_ARGUMENT.say(sender);
		return false;
	}

	@Override
	public List<String> onTabComplete(CommandSender sender, Command cmd, String label, String[] args)
	{
		if(args.length == 1)
			return Arrays.asList("survie", "créatif", "aventure", "spectateur");
		return null;
	}

	@Override
	public String getName()
	{
		return "setgamemode";
	}

	@Override
	public Permission getPermission()
	{
		return new Permission("CommandCenter.set-gamemode.self", "Permet de changer son propre mode de jeu", PermissionDefault.OP);
	}

	@Override
	public List<String> getAliases()
	{
		return Arrays.asList("gamemode", "gm", "set-gamemode");
	}

	@Override
	public String getUsage()
	{
		return "/setgamemode <mode de jeu> [joueur]";
	}

	@Override
	public String getDescription()
	{
		return "Permet de changer son propre mode de jeu ou celui d'un autre joueur.";
	}
	
	@Override
	public Collection<Permission> getOtherPermissions()
	{
		return Arrays.asList(new Permission("CommandCenter.set-gamemode.others", "Permet de changer le mode de jeu des autres joueurs", PermissionDefault.OP));
	}
}
