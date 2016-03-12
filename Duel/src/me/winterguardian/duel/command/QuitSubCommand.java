package me.winterguardian.duel.command;

import java.util.Arrays;
import java.util.List;

import me.winterguardian.core.command.SubCommand;
import me.winterguardian.core.message.ErrorMessage;
import me.winterguardian.duel.Duel;
import me.winterguardian.duel.DuelMessage;

import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class QuitSubCommand extends SubCommand
{
	public QuitSubCommand()
	{
		super("quitter", Arrays.asList("quit", "partir", "leave", "abandonner", "gtfo"), null, ErrorMessage.COMMAND_INVALID_PERMISSION.toString(), "/duel quitter [joueur]");
	}

	@Override
	public boolean onSubCommand(CommandSender sender, String label, String[] args)
	{
		if(Duel.getInstance().getSettings().isReady())
		{
			if(args.length == 0)
			{
				if(sender instanceof Player)
				{
					if(Duel.getInstance().gameContains((Player) sender))
						Duel.getInstance().leave((Player) sender);
					else
						DuelMessage.DUEL_NOTINGAME.say(sender);
					return true;
				}
				else
				{
					ErrorMessage.COMMAND_INVALID_SENDER.say(sender);
					return false;
				}
			}
			else
			{
				if(sender.hasPermission(Duel.ADMINISTRATION))
				{
					Player p = Bukkit.getPlayer(args[0]);
					
					if(p == null)
					{
						ErrorMessage.COMMAND_INVALID_PLAYER.say(sender);
						return false;
					}
					
					if(Duel.getInstance().getGame().isOnGame(p))
					{
						DuelMessage.DUEL_PLAYERQUIT.say(sender);
						Duel.getInstance().leave(p);
					}
					else
						DuelMessage.DUEL_PLAYERNOTINGAME.say(sender);
					return true;
				}
				else
				{
					ErrorMessage.COMMAND_INVALID_PERMISSION.say(sender);
					return true;
				}
			}
		}
		else
		{
			DuelMessage.DUEL_NOTREADY.say(sender);
			return true;
		}
	}

	@Override
	public List<String> onSubTabComplete(CommandSender sender, String label, String[] args)
	{
		return null;
	}
}