package me.winterguardian.pvp.command;

import java.util.Arrays;
import java.util.List;

import me.winterguardian.core.command.CloseStateGameSubCommand;
import me.winterguardian.core.command.CommandSplitter;
import me.winterguardian.core.command.JoinGameSubCommand;
import me.winterguardian.core.command.LeaveGameSubCommand;
import me.winterguardian.core.command.OpenStateGameSubCommand;
import me.winterguardian.pvp.PvP;
import me.winterguardian.pvp.PvPPlugin;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.permissions.Permission;

public class PvPCommand extends CommandSplitter
{
	private PvP game;
	
	public PvPCommand(PvP game)
	{
		this.game = game;
	    this.getSubCommands().add(new JoinGameSubCommand(game));
	    this.getSubCommands().add(new LeaveGameSubCommand(game));
	    
	    this.getSubCommands().add(new OpenStateGameSubCommand(game, PvPPlugin.STAFF));
	    this.getSubCommands().add(new CloseStateGameSubCommand(game, PvPPlugin.STAFF));
	    
	    this.getSubCommands().add(new ArenaSubCommand());
	    this.getSubCommands().add(new StuffSubCommand());
		this.getSubCommands().add(new KitSubCommand());
		this.getSubCommands().add(new StatsSubCommand(game));
		this.getSubCommands().add(new SetupSubCommand(game));
		this.getSubCommands().add(new VoteSubCommand(game));
		this.getSubCommands().add(new StartSubCommand(game));
		this.getSubCommands().add(new SetNextGameSubCommand(game));
	}

	@Override
	public List<String> getAliases()
	{
		return Arrays.asList("pvp", "skpvp");
	}
	
	@Override
	public String getDescription()
	{
		return "Commande principale pour l'utilisation et la gestion du jeu.";
	}
	
	@Override
	public String getName()
	{
		return "sekaipvp";
	}
	
	@Override
	public Permission getPermission()
	{
		return null;
	}
	
	@Override
	public boolean index(CommandSender sender, Command cmd, String label)
	{
		if (!game.contains((Player)sender))
			((Player)sender).performCommand("pvp joindre");
	    else
	        ((Player)sender).performCommand("pvp quitter");
	      return true;
	}
	
	@Override
	public List<Permission> getOtherPermissions()
	{
		return Arrays.asList(PvPPlugin.DOUBLE_VOTE, PvPPlugin.STAFF, PvPPlugin.ALL);
	}
}
