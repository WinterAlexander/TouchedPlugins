package ice.command;

import ice.IceRun;
import ice.IceRunMessage;
import ice.IceRunStats;
import me.winterguardian.core.command.PlayerStatsAsyncSubCommand;
import me.winterguardian.core.message.ErrorMessage;
import me.winterguardian.core.playerstats.MappedData;
import me.winterguardian.core.util.TextUtil;
import org.bukkit.command.CommandSender;

import java.util.Arrays;
import java.util.UUID;

public class StatsSubCommand extends PlayerStatsAsyncSubCommand
{
	public StatsSubCommand(IceRun icerun)
	{
		super(icerun, "stats", Arrays.asList("statistique", "statistiques", "score"), null, ErrorMessage.COMMAND_INVALID_PERMISSION.toString(), "/icerun stats [joueur]");
	}

	@Override
	public void displayStats(CommandSender sender, UUID id, MappedData data)
	{
		IceRunStats stats = new IceRunStats(data);
		
		sender.sendMessage(IceRunMessage.ICERUN_STATS + " : §b§l" + stats.getName());
		sender.sendMessage("§aVictoires: §c" + TextUtil.toString(stats.getVictories()) + " §f(" + stats.getPercentageWin() + "%)");
		sender.sendMessage("§eParties jouées: §c" + TextUtil.toString(stats.getGamesPlayed()));
		sender.sendMessage("§cScore: §c" + TextUtil.toString(stats.getScore()));
	}

}
