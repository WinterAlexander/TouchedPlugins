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

public class JoinSubCommand extends SubCommand
{
	public JoinSubCommand()
	{
		super("joindre", Arrays.asList("rejoindre", "join", "rejoin", "entrer", "enter"), null, ErrorMessage.COMMAND_INVALID_PERMISSION.toString(), "/duel joindre [player]");
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
					if(Duel.getInstance().getGame().isOnGame((Player) sender))
					{
						DuelMessage.DUEL_ALREADYINGAME.say(sender);
						return true;
					}
					
					Duel.getInstance().join((Player) sender);
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
						DuelMessage.DUEL_PLAYERALREADYINGAME.say(sender);
						return true;
					}
					
					DuelMessage.DUEL_PLAYERJOIN.say(sender);
					Duel.getInstance().join(p);
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
