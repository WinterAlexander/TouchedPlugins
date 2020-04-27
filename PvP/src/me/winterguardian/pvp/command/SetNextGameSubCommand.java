package me.winterguardian.pvp.command;

import me.winterguardian.core.command.SubCommand;
import me.winterguardian.core.message.ErrorMessage;
import me.winterguardian.pvp.PvP;
import me.winterguardian.pvp.PvPPlugin;
import me.winterguardian.pvp.game.PvPMatch;
import me.winterguardian.pvp.game.PvPVoteState;
import me.winterguardian.pvp.game.infected.Infected;
import me.winterguardian.pvp.game.solo.Brawl;
import me.winterguardian.pvp.game.solo.FreeForAll;
import me.winterguardian.pvp.game.solo.KingOfTheHill;
import me.winterguardian.pvp.game.solo.OneInTheChamber;
import me.winterguardian.pvp.game.solo.Switch;
import me.winterguardian.pvp.game.team.CaptureTheFlag;
import me.winterguardian.pvp.game.team.Domination;
import me.winterguardian.pvp.game.team.TeamDeathMatch;
import org.bukkit.command.CommandSender;

import java.util.Arrays;
import java.util.List;

/**
 *
 * Created by Alexander Winter on 2015-12-16.
 */
public class SetNextGameSubCommand extends SubCommand
{
	private PvP game;

	public SetNextGameSubCommand(PvP game)
	{
		super("setnextgame", Arrays.asList("setgame", "setnext", "nextgame"), PvPPlugin.ALL, ErrorMessage.COMMAND_INVALID_PERMISSION.toString(), "/pvp setnextgame <mode>");
		this.game = game;
	}

	@Override
	public boolean onSubCommand(CommandSender sender, String label, String[] args)
	{
		if(args.length <= 0)
			return false;
		PvPMatch next = null;

		if(args[0].equalsIgnoreCase("ffa"))
			next = new FreeForAll(PvPPlugin.getGame());

		if(args[0].equalsIgnoreCase("oitc"))
			next = new OneInTheChamber(PvPPlugin.getGame());

		if(args[0].equalsIgnoreCase("koth"))
			next = new KingOfTheHill(PvPPlugin.getGame());

		if(args[0].equalsIgnoreCase("swi"))
			next = new Switch(PvPPlugin.getGame());

		if(args[0].equalsIgnoreCase("brawl"))
			next = new Brawl(PvPPlugin.getGame());

		if(args[0].equalsIgnoreCase("tdm"))
			next = new TeamDeathMatch(PvPPlugin.getGame(), Integer.parseInt(args[1]));

		if(args[0].equalsIgnoreCase("ctf"))
			next = new CaptureTheFlag(PvPPlugin.getGame(), Integer.parseInt(args[1]));

		if(args[0].equalsIgnoreCase("dom"))
			next = new Domination(PvPPlugin.getGame(), Integer.parseInt(args[1]));

		if(args[0].equalsIgnoreCase("inf"))
			next = new Infected(PvPPlugin.getGame());

		if(next == null)
			return false;

		PvPVoteState.definedNext = next;

		return true;
	}

	@Override
	public List<String> onSubTabComplete(CommandSender sender, String label, String[] args)
	{
		return null;
	}
}
