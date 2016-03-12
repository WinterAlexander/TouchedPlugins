package me.winterguardian.commandcenter.commands;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import me.winterguardian.core.command.AutoRegistrationCommand;
import me.winterguardian.core.game.Game;
import me.winterguardian.core.game.GameManager;
import me.winterguardian.core.message.ErrorMessage;
import me.winterguardian.core.util.TextUtil;

import org.bukkit.Location;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.permissions.Permission;
import org.bukkit.permissions.PermissionDefault;

public class WarpCommand extends AutoRegistrationCommand
{

	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args)
	{
		if(args.length == 0)
		{
			ErrorMessage.COMMAND_INVALID_ARGUMENT.say(sender);
			return false;
		}
		
		if(!(sender instanceof Player))
		{
			ErrorMessage.COMMAND_INVALID_SENDER.say(sender);
			return true;
		}
		
		Location loc = null;
		
		for(Game game : GameManager.getGames())
		{
			if(game.getClass().getSimpleName().equalsIgnoreCase(args[0]))
			{
				loc = game.getWarp();
				break;
			}
		}
		
		if(loc == null)
		{
			ErrorMessage.COMMAND_INVALID_ARGUMENT.say(sender);
			return false;
		}
		
		((Player)sender).teleport(loc);
		return true;
	}

	@Override
	public List<String> onTabComplete(CommandSender sender, Command cmd, String label, String[] args)
	{
		if(args.length != 1)
			return null;
		
		ArrayList<String> list = new ArrayList<String>();
		
		for(Game game : GameManager.getGames())
			list.add(game.getClass().getSimpleName());
		
		return TextUtil.getStringsThatStartWith(args[0], list);
	}

	@Override
	public String getName()
	{
		return "warp";
	}

	@Override
	public Permission getPermission()
	{
		return new Permission("CommandCenter.warp", getDescription(), PermissionDefault.OP);
	}

	@Override
	public List<String> getAliases()
	{
		return Arrays.asList("warptogame");
	}

	@Override
	public String getUsage()
	{
		return "/warp <game>";
	}

	@Override
	public String getDescription()
	{
		return "Une commande pour se téléporter à un jeu.";
	}
}