package me.winterguardian.blockfarmers.command;

import me.winterguardian.blockfarmers.BlockFarmersGame;
import me.winterguardian.blockfarmers.BlockFarmersMessage;
import me.winterguardian.blockfarmers.FarmersStats;
import me.winterguardian.core.command.PlayerStatsAsyncSubCommand;
import me.winterguardian.core.playerstats.MappedData;
import me.winterguardian.core.util.TextUtil;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;

import java.util.UUID;

public class StatsSubCommand extends PlayerStatsAsyncSubCommand
{
	public StatsSubCommand(BlockFarmersGame game)
	{
		super(game.getPlugin());
	}
	
	@Override
	public void displayStats(CommandSender sender, UUID player, MappedData data)
	{
		FarmersStats stats = new FarmersStats(Bukkit.getOfflinePlayer(player), data);
		
		BlockFarmersMessage.COMMAND_STATS_HEADER.say(sender, "<player>", stats.getName());
		BlockFarmersMessage.COMMAND_STATS_VICTORIES.say(sender, "#", TextUtil.toString(stats.getVictories()));
		BlockFarmersMessage.COMMAND_STATS_GAMESPLAYED.say(sender, "#", TextUtil.toString(stats.getGamesPlayed()));
		BlockFarmersMessage.COMMAND_STATS_TILESFARMED.say(sender, "#", TextUtil.toString(stats.getTilesFarmed()));
		BlockFarmersMessage.COMMAND_STATS_SCORE.say(sender, "#", TextUtil.toString(stats.getScore()));
	}

}
