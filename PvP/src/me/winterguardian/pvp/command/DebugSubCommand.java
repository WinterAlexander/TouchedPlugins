package me.winterguardian.pvp.command;

import me.winterguardian.core.command.SubCommand;
import me.winterguardian.core.message.ErrorMessage;
import me.winterguardian.pvp.PvP;
import me.winterguardian.pvp.PvPPlugin;
import me.winterguardian.pvp.game.PvPMatch;
import org.bukkit.command.CommandSender;

import java.util.ArrayList;
import java.util.List;

/**
 * Undocumented :(
 * <p>
 * Created on 2020-04-27.
 *
 * @author Alexander Winter
 */
public class DebugSubCommand extends SubCommand
{
	private PvP game;

	public DebugSubCommand(PvP game)
	{
		super("debug", new ArrayList<>(), PvPPlugin.ALL, ErrorMessage.COMMAND_INVALID_PERMISSION.toString(), "/pvp debug <something>");
		this.game = game;
	}

	@Override
	public boolean onSubCommand(CommandSender sender, String label, String[] args)
	{
		if(args.length <= 0)
			return false;

		if(args[0].equalsIgnoreCase("updatedboard") && game.getState() instanceof PvPMatch)
			((PvPMatch)game.getState()).updateBoard();

		return true;
	}

	@Override
	public List<String> onSubTabComplete(CommandSender sender, String label, String[] args)
	{
		return null;
	}
}