package me.winterguardian.pvp.game.team;


import me.winterguardian.core.message.Message;
import me.winterguardian.pvp.GameStuff;
import me.winterguardian.pvp.PvP;
import me.winterguardian.pvp.PvPMessage;
import me.winterguardian.pvp.TeamColor;
import me.winterguardian.pvp.game.PvPPlayerData;
import org.bukkit.entity.Player;

import java.util.HashMap;

/**
 *
 * Created by Alexander Winter on 2015-12-12.
 */
public class TeamDeathMatch extends TeamGame
{
	private GameStuff stuff;
	private int teams;

	public TeamDeathMatch(PvP game, int teams)
	{
		super(game);
		this.stuff = new GameStuff("tdm");
		this.stuff.load();
		this.teams = teams;
	}

	@Override
	public int getTeamCount()
	{
		return teams;
	}

	@Override
	public int getScore(TeamColor color)
	{
		HashMap<TeamColor, Integer> kills = new HashMap<>();

		for(TeamColor team : getTeams())
			kills.put(team, 0);

		for(PvPPlayerData data : getPlayerDatas())
			if(kills.containsKey(data.getTeam()))
				kills.put(data.getTeam(), kills.get(data.getTeam()) + data.getKills());

		return kills.get(color);
	}

	@Override
	public int getSecondFactor(TeamColor color)
	{
		HashMap<TeamColor, Integer> assists = new HashMap<>();

		for(TeamColor team : getTeams())
			assists.put(team, 0);

		for(PvPPlayerData data : getPlayerDatas())
			if(assists.containsKey(data.getTeam()))
				assists.put(data.getTeam(), assists.get(data.getTeam()) + data.getAssists());

		return assists.get(color);
	}

	@Override
	public GameStuff getNewStuff(Player player)
	{
		return this.stuff;
	}

	@Override
	public String getName()
	{
		return "Match à mort";
	}

	@Override
	public String getColoredName()
	{
		return "§9§l" + getName();
	}

	@Override
	public Message getStartMessage()
	{
		return PvPMessage.GAME_START_TDM;
	}

	@Override
	public Message getGuide()
	{
		return PvPMessage.GAME_GUIDE_TDM;
	}
}
