package me.winterguardian.pvp.game.solo;

import me.winterguardian.core.game.PlayerData;
import me.winterguardian.core.scoreboard.ScoreboardUtil;
import me.winterguardian.pvp.PvP;
import me.winterguardian.pvp.PvPMessage;
import me.winterguardian.pvp.TeamColor;
import me.winterguardian.pvp.game.PvPMatch;
import me.winterguardian.pvp.game.PvPPlayerData;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.scoreboard.NameTagVisibility;
import org.bukkit.scoreboard.Scoreboard;
import org.bukkit.scoreboard.Team;

import java.util.HashMap;

/**
 *
 * Created by Alexander Winter on 2015-12-07.
 */
public abstract class SoloGame extends PvPMatch
{
	private Scoreboard board;

	public SoloGame(PvP game)
	{
		super(game);
		this.board = Bukkit.getScoreboardManager().getNewScoreboard();
	}

	public abstract int getScore(PvPPlayerData data);
	public abstract int getSecondFactor(PvPPlayerData data);

	@Override
	public TeamColor getNewTeam(Player player)
	{
		return TeamColor.NONE;
	}

	@Override
	public void join(Player player)
	{
		super.join(player);
		player.setScoreboard(this.board);
		Team team = this.board.registerNewTeam(player.getName());
		team.setAllowFriendlyFire(false);
		team.setNameTagVisibility(NameTagVisibility.HIDE_FOR_OTHER_TEAMS);
		team.addEntry(player.getName());
	}

	@Override
	public void leave(Player player)
	{
		this.board.getTeam(player.getName()).unregister();

		super.leave(player);
	}

	@Override
	public boolean areEnemies(Player p1, Player p2)
	{
		return p1 != p2;
	}

	@Override
	public void finish()
	{
		super.finish();

		PvPPlayerData winner = getPlayerData(1);

		PvPMessage.GAME_SOLO_WIN.sayAll("<name>", winner.getPvPName(), "<score>", getScore(winner) + "", "<mode>", getName());

	}

	public PvPPlayerData getPlayerData(int position)
	{
		for(PvPPlayerData data : getPlayerDatas())
			if(getPosition(data) == position)
				return data;

		return null;
	}

	public int getPosition(PvPPlayerData data)
	{
		int position = 1;

		for(PvPPlayerData playerData : getPlayerDatas())
		{
			if(data == playerData)
				continue;
			if(getScore(playerData) > getScore(data))
				position++;
			else if(getScore(playerData) == getScore(data) && getSecondFactor(playerData) > getSecondFactor(data))
				position++;
		}
		return position;
	}

	@Override
	public void start()
	{
		super.start();

		for(Player player : getGame().getPlayers())
		{
			player.setScoreboard(this.board);
			Team team = this.board.registerNewTeam(player.getName());
			team.setAllowFriendlyFire(false);
			team.setNameTagVisibility(NameTagVisibility.HIDE_FOR_OTHER_TEAMS);
			team.addEntry(player.getName());
		}
	}

	@Override
	public void updateBoard()
	{
		HashMap<String, Integer> scores = new HashMap<>();

		for(PvPPlayerData data : getPlayerDatas())
			scores.put(data.getPvPName(), getScore(data));

		ScoreboardUtil.rankedSidebarDisplay(getGame().getPlayers(), getColoredName(), scores, this.board);
	}
}
