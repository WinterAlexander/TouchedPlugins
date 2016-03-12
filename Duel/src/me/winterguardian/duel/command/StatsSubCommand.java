package me.winterguardian.duel.command;

import me.winterguardian.core.command.PlayerStatsAsyncSubCommand;
import me.winterguardian.core.message.ErrorMessage;
import me.winterguardian.core.playerstats.MappedData;
import me.winterguardian.core.util.TextUtil;
import me.winterguardian.duel.Duel;
import me.winterguardian.duel.DuelMessage;
import me.winterguardian.duel.DuelStats;
import org.bukkit.command.CommandSender;

import java.util.Arrays;
import java.util.List;
import java.util.UUID;

public class StatsSubCommand extends PlayerStatsAsyncSubCommand
{
	public StatsSubCommand(Duel game)
	{
		super(game, "stats", Arrays.asList("stat", "statistique", "statistiques", "score", "skills"), null, ErrorMessage.COMMAND_INVALID_PERMISSION.toString(), "/duel stats [joueur]");
	}
	
	@Override
	public List<String> onSubTabComplete(CommandSender arg0, String arg1, String[] arg2)
	{
		return null;
	}

	@Override
	public void displayStats(CommandSender sender, UUID id, MappedData data)
	{
		DuelStats stats = new DuelStats(data);
		
		sender.sendMessage(DuelMessage.DUEL_STATS + " §f§l: §e§l" + stats.getName());
		sender.sendMessage("§aVictoires: §c" + TextUtil.toString(stats.getVictories()) + " §f(" + stats.getPercentageWin() + "%)");
		sender.sendMessage("§eParties jouées: §c" + TextUtil.toString(stats.getGamesPlayed()));
		sender.sendMessage("§cParties abandonnés: §c" + TextUtil.toString(stats.getSurrenders()));
	}
}
