package me.winterguardian.pvp.command;

import me.winterguardian.core.Core;
import me.winterguardian.core.command.SubCommand;
import me.winterguardian.core.message.ErrorMessage;
import me.winterguardian.pvp.PvP;
import me.winterguardian.pvp.PvPMessage;
import me.winterguardian.pvp.PvPPlugin;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.Arrays;
import java.util.List;

/**
 *
 * Created by Alexander Winter on 2015-12-09.
 */
public class SetupSubCommand extends SubCommand
{
	private PvP game;

	public SetupSubCommand(PvP game)
	{
		super("setup", Arrays.asList("manage", "config"), PvPPlugin.ALL, ErrorMessage.COMMAND_INVALID_PERMISSION.toString(), "§cSyntaxe: §f/pvp setup");
		this.game = game;
	}

	@Override
	public boolean onSubCommand(CommandSender sender, String label, String[] args)
	{
		if(args.length == 0)
			args = new String[]{"info"};

		if(args[0].equalsIgnoreCase("info"))
		{
			sender.sendMessage("coming soon...");
			return true;
		}

		if(args[0].equalsIgnoreCase("help"))
		{
			sender.sendMessage("coming soon...");
			return true;
		}

		if(!(sender instanceof Player))
		{
			ErrorMessage.COMMAND_INVALID_SENDER.say(sender);
			return true;
		}

		if(args[0].equalsIgnoreCase("setregion"))
		{
			if(Core.getWand().getRegion((Player)sender) == null)
			{
				sender.sendMessage("Mauvaise région.");
				return true;
			}

			game.getSetup().setRegion(Core.getWand().getRegion((Player)sender));
			PvPMessage.COMMAND_SETUP_SETREGION.say(sender);
			return true;
		}

		if(args[0].equalsIgnoreCase("setexit"))
		{
			game.getSetup().setExit(((Player)sender).getLocation());
			PvPMessage.COMMAND_SETUP_SETEXIT.say(sender);
			return true;
		}

		if(args[0].equalsIgnoreCase("setlobby"))
		{
			game.getSetup().setLobby(((Player)sender).getLocation());
			PvPMessage.COMMAND_SETUP_SETLOBBY.say(sender);
			return true;
		}

		ErrorMessage.COMMAND_INVALID_ARGUMENT.say(sender);
		return true;
	}

	@Override
	public List<String> onSubTabComplete(CommandSender sender, String label, String[] args)
	{
		return null;
	}
}
