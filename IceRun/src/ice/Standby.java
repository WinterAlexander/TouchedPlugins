package ice;

import me.winterguardian.core.Core;
import me.winterguardian.core.game.state.State;
import me.winterguardian.core.scoreboard.ScoreboardUtil;
import me.winterguardian.core.util.PlayerUtil;
import org.bukkit.entity.Player;

public class Standby implements State
{
	public static void giveScoreboard(Player p)
	{
		IceRunStats stats = new IceRunStats(Core.getUserDatasManager().getUserData(p));
		
		ScoreboardUtil.unrankedSidebarDisplay(p, new String[]{"§b§lIceRun", "§aVictoires: " + stats.getVictories(), "§cParties jouées: " + stats.getGamesPlayed(), "§eScore: " + stats.getScore()});
	}
	
	
	public void join(Player p)
	{
		p.teleport(IceRun.getSettings().getLobby().getLocation());
		PlayerUtil.clearInventory(p);
		PlayerUtil.clearBoard(p);
		PlayerUtil.heal(p);
		PlayerUtil.prepare(p);
		giveScoreboard(p);
		
		if (IceRun.players.size() >= 2)
		{
			IceRun.status = new WaitingSession();
			IceRun.status.start();
		}
		else
			IceRunMessage.GAME_NOTENOUGHTPLAYERS.say(p);
	}

	public void leave(Player p)
	{
		PlayerUtil.clearInventory(p);
		PlayerUtil.clearBoard(p);
		PlayerUtil.heal(p);
		PlayerUtil.prepare(p);
		p.teleport(IceRun.getSettings().getExit().getLocation());
	}

	public void start()
	{
		if (IceRun.players.size() >= 2)
		{
			IceRun.status = new WaitingSession();
			IceRun.status.start();
		}
		else
		{
			IceRunMessage.GAME_NOTENOUGHTPLAYERS.say(IceRun.players);
			for(Player p : IceRun.players)
			{
				PlayerUtil.clearInventory(p);
				PlayerUtil.prepare(p);
				giveScoreboard(p);
			}
		}
		
	}

	@Override
	public void end()
	{
		
	}

	@Override
	public String getStatus()
	{
		return "§cIl n'y a pas assez de joueurs pour commencer.";
	}
}