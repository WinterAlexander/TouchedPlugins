package me.winterguardian.pvp.game.team;

import me.winterguardian.core.scoreboard.ScoreboardUtil;
import me.winterguardian.core.util.TextUtil;
import me.winterguardian.pvp.PvP;
import me.winterguardian.pvp.PvPMessage;
import me.winterguardian.pvp.TeamColor;
import me.winterguardian.pvp.game.GameOutcome;
import me.winterguardian.pvp.game.PvPMatch;
import me.winterguardian.pvp.game.PvPPlayerData;
import me.winterguardian.pvp.stats.PvPStats;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.scoreboard.NameTagVisibility;
import org.bukkit.scoreboard.Scoreboard;
import org.bukkit.scoreboard.Team;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Random;
import java.util.Set;

/**
 *
 * Created by Alexander Winter on 2015-12-07.
 */
public abstract class TeamGame extends PvPMatch
{
	protected Scoreboard board;
	private HashMap<TeamColor, Team> boardTeams;

	public abstract int getTeamCount();
	public abstract int getScore(TeamColor color);
	public abstract int getSecondFactor(TeamColor color);

	public TeamGame(PvP game)
	{
		super(game);
		this.board = Bukkit.getScoreboardManager().getNewScoreboard();
		this.boardTeams = new HashMap<>();
	}

	@Override
	public void join(Player p)
	{
		super.join(p);
		p.setScoreboard(this.board);
		this.boardTeams.get(getPlayerData(p).getTeam()).addEntry(p.getName());
	}

	@Override
	public void leave(Player p)
	{
		this.boardTeams.get(getPlayerData(p).getTeam()).removeEntry(p.getName());

		super.leave(p);
	}

	@Override
	public void start()
	{
		super.start();
		for(TeamColor color : getTeams())
		{
			Team team = this.board.registerNewTeam(color.name());
			team.setAllowFriendlyFire(false);
			team.setDisplayName(color.toString());
			team.setNameTagVisibility(NameTagVisibility.HIDE_FOR_OTHER_TEAMS);
			team.setPrefix(color.getBoardPrefix());

			this.boardTeams.put(color, team);
		}
		for(Player p : getGame().getPlayers())
		{
			p.setScoreboard(this.board);
			this.boardTeams.get(getPlayerData(p).getTeam()).addEntry(p.getName());
		}
		updateBoard();
	}

	@Override
	public void end()
	{
		for(Player p : getGame().getPlayers())
			this.boardTeams.get(getPlayerData(p).getTeam()).removeEntry(p.getName());

		super.end();
	}

	@Override
	public void finish()
	{
		super.finish();
		if(getTeamCount() == 2)
			PvPMessage.GAME_TEAM2_WIN.sayAll("<team>", getTeam(1).toString(),
					"<score>", getScore(getTeam(1)) + "",
					"<second>", getTeam(2).toString(),
					"<score_second>", getScore(getTeam(2)) + "",
					"<mode>", getName());
		else
			PvPMessage.GAME_TEAMMORE_WIN.sayAll("<team>", getTeam(1).toString(),
					"<team_count>", getTeamCount() + "",
					"<mode>", getName());
	}


	@Override
	public TeamColor getNewTeam(Player player, boolean gameStart)
	{
		List<TeamColor> potentials = new ArrayList<>();

		for(TeamColor current : getTeams())
		{
			if(potentials.size() == 0 || getCurrentlyPlayingPlayers(potentials.get(0)).size() > getCurrentlyPlayingPlayers(current).size())
				potentials = new ArrayList<>(Collections.singletonList(current));
			else if(getCurrentlyPlayingPlayers(potentials.get(0)).size() == getCurrentlyPlayingPlayers(current).size())
				potentials.add(current);
		}

		TeamColor team = potentials.get(new Random().nextInt(potentials.size()));
		PvPMessage.GAME_JOINTEAM.sayPlayers("<player>", PvPStats.get(player.getUniqueId()).getPvPName(), "<color>", team.toString());
		return team;
	}

	public List<Player> getPlayers(TeamColor color)
	{
		List<Player> players = new ArrayList<>();

		for(PvPPlayerData playerData : getPlayerDatas())
			if(playerData.getTeam() == color)
				players.add(playerData.getPlayer());

		return players;
	}

	public List<Player> getCurrentlyPlayingPlayers(TeamColor color)
	{
		List<Player> players = new ArrayList<>();

		for(PvPPlayerData playerData : getPlayerDatas())
			if(playerData.getTeam() == color && playerData.isPlaying())
				players.add(playerData.getPlayer());

		return players;
	}


	public TeamColor getTeam(int position)
	{
		if(position <= 0 || position > getTeamCount())
			return null;

		List<TeamColor> betters = new ArrayList<>();

		for(int i = 0; i < position; i++)
		{
			TeamColor best = null;

			for(TeamColor team : getTeams())
				if(!betters.contains(team))
					if(best == null || getScore(team) > getScore(best))
					{
						best = team;
					}
					else if(getScore(team) == getScore(best) && getSecondFactor(team) > getSecondFactor(best))
					{
						best = team;
					}

			if(i == position - 1)
				return best;
			else
				betters.add(best);
		}

		return null;
	}

	@Override
	public boolean areEnemies(Player p1, Player p2)
	{
		return getPlayerData(p1).getTeam() != getPlayerData(p2).getTeam();
	}

	public Set<TeamColor> getTeams()
	{
		HashSet<TeamColor> set = new HashSet<>(getArena().getTeams());

		while(set.size() > getTeamCount())
			set.remove(set.iterator().next());

		return set;
	}

	@Override
	public void updateBoard()
	{
		if(getTeamCount() <= 5)
		{
			String[] data = new String[16];

			data[0] = getColoredName();

			String lastSpace = "";

			for(int i = 0; i < getTeamCount(); i++)
			{
				int pos = (i * 3) + 1;
				data[pos] = getTeam(i + 1).getBukkitColor() + TextUtil.capitalize(getTeam(i + 1).getNamePlural());
				String score = getScore(getTeam(i + 1)) + "";

				boolean change;

				do
				{
					change = false;
					for(String current : data)
						if(current != null && current.equals(score))
							change = true;

					if(change)
						score += " ";
				}
				while(change);


				data[pos + 1] = score;
				lastSpace = data[pos + 2] = lastSpace + " ";
			}

			ScoreboardUtil.unrankedSidebarDisplay(getGame().getPlayers(), data, this.board);
	}
		else
		{
			HashMap<String, Integer> map = new HashMap<>();

			for(int i = 1; i <= getTeamCount(); i++)
				map.put(getTeam(i).getBukkitColor() + TextUtil.capitalize(getTeam(i).getNamePlural()), getScore(getTeam(i)));

			ScoreboardUtil.rankedSidebarDisplay(getGame().getPlayers(), getColoredName(), map, this.board);
		}
	}

	public int getMinimum()
	{
		return getTeamCount();
	}

	@Override
	public GameOutcome getOutcome(Player player)
	{
		return getTeam(1) == getPlayerData(player).getTeam() ? GameOutcome.TEAM_WIN : GameOutcome.TEAM_LOSE;
	}
}
