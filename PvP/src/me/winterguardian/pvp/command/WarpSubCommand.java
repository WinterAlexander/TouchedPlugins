package me.winterguardian.pvp.command;

import me.winterguardian.core.command.SubCommand;
import me.winterguardian.core.message.ErrorMessage;
import me.winterguardian.pvp.PvP;
import me.winterguardian.pvp.PvPArena;
import me.winterguardian.pvp.PvPMessage;
import me.winterguardian.pvp.PvPPlugin;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.Arrays;
import java.util.List;

/**
 * Undocumented :(
 * <p>
 * Created on 2020-04-29.
 *
 * @author Alexander Winter
 */
public class WarpSubCommand extends SubCommand
{
	private final PvP game;

	public WarpSubCommand(PvP game)
	{
		super("warp", Arrays.asList("tp"), PvPPlugin.STAFF, ErrorMessage.COMMAND_INVALID_PERMISSION, ErrorMessage.COMMAND_INVALID_ARGUMENT);
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

		if(args.length == 0)
		{
			if(game.getWarp() == null)
			{
				sender.sendMessage("Game lobby not set.");
				return true;
			}
			((Player)sender).teleport(game.getWarp());
			return true;
		}

		PvPArena arena = new PvPArena(args[0]);

		if(!arena.exists())
		{
			PvPMessage.COMMAND_ARENA_INVALIDARENA.say(sender);
			return true;
		}

		arena.load();

		if(arena.getSpawnPoints().size() == 0)
		{
			PvPMessage.COMMAND_ARENA_INVALIDARENA.say(sender);
			return true;
		}

		((Player)sender).teleport(arena.getSpawnPoints().get(0).getLocation());
		return true;
	}

	@Override
	public List<String> onSubTabComplete(CommandSender sender, String label, String[] args)
	{
		return null;
	}
}