package me.winterguardian.pvp.command;

import java.util.Arrays;
import java.util.List;

import me.winterguardian.pvp.game.PvPMatch;
import me.winterguardian.pvp.game.solo.FreeForAll;
import me.winterguardian.pvp.game.solo.OneInTheChamber;
import me.winterguardian.pvp.game.team.TeamDeathMatch;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import me.winterguardian.core.command.SubCommand;
import me.winterguardian.core.message.ErrorMessage;
import me.winterguardian.pvp.PvPArena;
import me.winterguardian.pvp.PvPMessage;
import me.winterguardian.pvp.PvPPlugin;
import me.winterguardian.pvp.TeamColor;

public class ArenaSubCommand extends SubCommand
{

	public ArenaSubCommand()
	{
		super("arena", Arrays.asList("map", "carte"), PvPPlugin.STAFF, ErrorMessage.COMMAND_INVALID_PERMISSION, ErrorMessage.COMMAND_INVALID_ARGUMENT);
	}

	@Override
	public boolean onSubCommand(CommandSender sender, String label, String[] args)
	{
		if(args.length == 0)
			args = new String[]{"help"};
		
		if(args[0].equalsIgnoreCase("help"))
		{
			return true;
		}
		
		if(args[0].equalsIgnoreCase("list"))
		{
			PvPMessage.COMMAND_ARENA_LIST.say(sender);
			return true;
		}
		
		if(args.length < 2)
			return false;
		
		PvPArena arena = new PvPArena(args[1]);
		
		if(args[0].equalsIgnoreCase("create"))
		{
			arena.save();
			PvPMessage.COMMAND_ARENA_CREATE.say(sender);
			return true;
		}
		
		if(!arena.exists())
		{
			PvPMessage.COMMAND_ARENA_INVALIDARENA.say(sender);
			return true;
		}
		
		arena.load();
		
		if(args[0].equalsIgnoreCase("info"))
		{
			sender.sendMessage("Nom: " + arena.getName());
			sender.sendMessage("Auteur: " + arena.getAuthor());
			sender.sendMessage("Ready: " + arena.isReady());
			sender.sendMessage("Zones: " + arena.getZones().size());
			return true;
		}
		
		if(args.length < 3)
			return false;
		
		TeamColor color = args.length < 4 ? null : TeamColor.valueOf(args[3].toUpperCase());
		
		if(color == null)
			color = TeamColor.NONE;
		
		if(args[0].equalsIgnoreCase("add"))
		{
			if(args[2].equalsIgnoreCase("point"))
			{
				arena.addSpawnPoint(((Player)sender).getLocation(), color);
				arena.save();
				PvPMessage.COMMAND_ARENA_ADD.say(sender, "<type>", "Point de spawn", "<color>", color.getName());
				return true;
			}
			
			if(args[2].equalsIgnoreCase("flag"))
			{
				arena.addFlag(((Player)sender).getLocation(), color);
				arena.save();
				PvPMessage.COMMAND_ARENA_ADD.say(sender, "<type>", "Drapeau", "<color>", color.getName());
				return true;
			}
			
			if(args[2].equalsIgnoreCase("zone"))
			{
				arena.addZone(((Player)sender).getLocation());
				arena.save();
				PvPMessage.COMMAND_ARENA_ADD.say(sender, "<type>", "Zone", "<color>", color.getName());
				return true;
			}
		}
		
		if(args[0].equalsIgnoreCase("reset"))
		{
			if(args[2].equalsIgnoreCase("point"))
			{
				if(args[3].equalsIgnoreCase("*") || args[3].equalsIgnoreCase("all"))
					arena.resetPoints();
				else
					arena.resetPoints(color);
				arena.save();
				PvPMessage.COMMAND_ARENA_RESET.say(sender, "<type>", "point de spawn", "<color>", args[3]);
				return true;
			}
			
			if(args[2].equalsIgnoreCase("flag"))
			{
				if(args[3].equalsIgnoreCase("*") || args[3].equalsIgnoreCase("all"))
					arena.resetFlags();
				else
					arena.resetFlags(color);
				arena.save();
				PvPMessage.COMMAND_ARENA_RESET.say(sender, "<type>", "drapeau", "<color>", args[3]);
				return true;
			}
			
			if(args[2].equalsIgnoreCase("zone"))
			{
				arena.resetZones();
				arena.save();
				PvPMessage.COMMAND_ARENA_RESET.say(sender, "<type>", "zone", "<color>", args[3]);
				return true;
			}
		}
		
		return false;
	}

	@Override
	public List<String> onSubTabComplete(CommandSender sender, String label, String[] args)
	{
		return null;
	}

}
