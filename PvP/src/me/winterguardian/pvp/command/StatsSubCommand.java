package me.winterguardian.pvp.command;

import me.winterguardian.core.command.PlayerStatsAsyncSubCommand;
import me.winterguardian.core.playerstats.MappedData;
import me.winterguardian.core.util.TextUtil;
import me.winterguardian.pvp.PvP;
import me.winterguardian.pvp.PvPMessage;
import me.winterguardian.pvp.stats.PvPStats;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;

import java.util.UUID;

public class StatsSubCommand extends PlayerStatsAsyncSubCommand
{
	public StatsSubCommand(PvP game)
	{
		super(game.getPlugin());
	}

	@Override
	public void displayStats(CommandSender sender, UUID id, MappedData data)
	{
	    PvPStats stats = new PvPStats(Bukkit.getOfflinePlayer(id), data);

	    PvPMessage.COMMAND_STATS_HEADER.say(sender, "<player>", stats.getName());

		PvPMessage.COMMAND_STATS_LEVELEXPRATIO.say(sender,
				"<level>", TextUtil.toString(stats.getDisplayLevel()),
				"<exp>", stats.getProgression() + "%",
				"<ratio>", stats.getRoundedRatio() + "");

	    PvPMessage.COMMAND_STATS_KILLSDEATHSASSISTS.say(sender, 
	    		"<kills>", TextUtil.toString(stats.getKills()), 
	    		"<deaths>", TextUtil.toString(stats.getDeaths()), 
	    		"<assists>", TextUtil.toString(stats.getAssists()));
	    
	    PvPMessage.COMMAND_STATS_VICTORIESGAMES.say(sender, 
	    		"<victories>", TextUtil.toString(stats.getVictories()), 
	    		"<gamesplayed>", TextUtil.toString(stats.getGamesPlayed()));
	    
	    PvPMessage.COMMAND_STATS_FLAGSZONES.say(sender, 
	    		"<flags>", TextUtil.toString(stats.getCapturedFlags()), 
	    		"<zones>", TextUtil.toString(stats.getCapturedZones()));
	    
	    PvPMessage.COMMAND_STATS_BESTSTREAKBESTGAME.say(sender, 
	    		"<killstreak>", TextUtil.toString(stats.getBestKillingSpree()), 
	    		"<kills>", TextUtil.toString(stats.getBestGameKills()));
	    
	    PvPMessage.COMMAND_STATS_SCOREPOINTSTIME.say(sender, 
	    		"<score>", TextUtil.toString(stats.getScore()), 
	    		"<points>", TextUtil.toString(stats.getPoints()), 
	    		"<time>", TextUtil.toString(Math.round(stats.getTimePlayed() / 1000.0 / 60.0 / 60.0 * 10.0) / 10.0));
	}
}
