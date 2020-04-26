package me.winterguardian.pvp.command;

import me.winterguardian.core.command.SubCommand;
import me.winterguardian.core.message.ErrorMessage;
import me.winterguardian.pvp.PvP;
import me.winterguardian.pvp.PvPPlugin;
import me.winterguardian.pvp.game.PvPMatch;
import me.winterguardian.pvp.game.PvPVoteState;
import org.bukkit.command.CommandSender;

import java.util.Arrays;
import java.util.List;

/**
 *
 * Created by Alexander Winter on 2015-12-09.
 */
public class StartSubCommand extends SubCommand
{
	private PvP game;

	public StartSubCommand(PvP game)
	{
		super("start", Arrays.asList("demarre", "commencer", "skip"), PvPPlugin.STAFF, ErrorMessage.COMMAND_INVALID_PERMISSION.toString(), "/pvp start");
		this.game = game;
	}

	@Override
	public boolean onSubCommand(CommandSender sender, String label, String[] args)
	{
		if(game.getState() instanceof PvPVoteState)
			((PvPVoteState)game.getState()).skip();

		if(game.getState() instanceof PvPMatch)
			((PvPMatch)game.getState()).skip();

		return true;
	}

	@Override
	public List<String> onSubTabComplete(CommandSender sender, String label, String[] args)
	{
		return null;
	}
}
