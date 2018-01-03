package me.winterguardian.pvp.command;

import me.winterguardian.core.command.SubCommand;
import me.winterguardian.core.message.ErrorMessage;
import me.winterguardian.core.message.HardcodedMessage;
import me.winterguardian.core.message.Message;
import me.winterguardian.pvp.PvP;
import me.winterguardian.pvp.PvPArena;
import me.winterguardian.pvp.PvPMessage;
import me.winterguardian.pvp.game.PvPVoteState;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.permissions.Permission;

import java.util.Arrays;
import java.util.List;

/**
 *
 * Created by Alexander Winter on 2015-12-08.
 */
public class VoteSubCommand extends SubCommand
{
	private PvP game;

	public VoteSubCommand(PvP game)
	{
		super("vote", Arrays.asList("choose", "votearena"), null, ErrorMessage.COMMAND_INVALID_PERMISSION, new HardcodedMessage("§cSyntaxe: §f/pvp vote <arène>", false));
		this.game = game;
	}

	@Override
	public boolean onSubCommand(CommandSender sender, String label, String[] args)
	{
		if(!(sender instanceof Player))
		{
			ErrorMessage.COMMAND_INVALID_SENDER.say(sender);
			return true;
		}

		if(!game.contains((Player)sender))
		{
			PvPMessage.LEAVE_NOTINGAME.say(sender);
			return true;
		}

		if(!(game.getState() instanceof PvPVoteState))
		{
			PvPMessage.COMMAND_VOTE_BADSTATE.say(sender);
			return true;
		}

		if(args.length == 0)
		{
			PvPMessage.COMMAND_VOTE_LIST.say(sender);
			String list = "§e";
			for(PvPArena arena : PvPArena.getCompatibleArenaList(((PvPVoteState)game.getState()).getNextGame()))
				list += arena.getName() + ", ";

			sender.sendMessage(list.substring(0, list.length() - 2));
			return true;
		}

		((PvPVoteState)game.getState()).vote((Player)sender, args[0]);
		return true;
	}

	@Override
	public List<String> onSubTabComplete(CommandSender sender, String label, String[] args)
	{
		return null;
	}
}
