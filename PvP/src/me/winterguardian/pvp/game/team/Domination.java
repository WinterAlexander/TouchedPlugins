package me.winterguardian.pvp.game.team;

import me.winterguardian.core.message.Message;
import me.winterguardian.core.world.SerializableLocation;
import me.winterguardian.pvp.GameStuff;
import me.winterguardian.pvp.PvP;
import me.winterguardian.pvp.PvPMessage;
import me.winterguardian.pvp.TeamColor;
import me.winterguardian.pvp.game.PvPPlayerData;
import me.winterguardian.pvp.game.Zone;
import me.winterguardian.pvp.stats.Bonus;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 *
 * Created by Alexander Winter on 2015-12-14.
 */
public class Domination extends TeamGame
{
	private HashMap<TeamColor, Integer> score;
	private List<Zone> zones;
	private GameStuff stuff;
	private int teams;

	public Domination(PvP game, int teams)
	{
		super(game);
		this.stuff = new GameStuff("dom");
		this.stuff.load();
		this.teams = teams;
		this.score = new HashMap<>();
		this.zones = new ArrayList<>();
	}

	@Override
	public int getTeamCount()
	{
		return teams;
	}

	@Override
	public int getScore(TeamColor color)
	{
		if(!score.containsKey(color))
			return 0;
		return score.get(color);
	}

	@Override
	public int getSecondFactor(TeamColor color)
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
	public GameStuff getNewStuff(Player player, boolean gameStart)
	{
		return this.stuff;
	}

	@Override
	public String getName()
	{
		return "Domination";
	}

	@Override
	public String getColoredName()
	{
		return "§3§l" + getName();
	}

	@Override
	public void start()
	{
		super.start();
		register(new DOMListener(this));
		for(SerializableLocation loc : getArena().getZones())
		{
			Zone zone = new Zone(loc.getLocation());
			zone.place();
			zones.add(zone);
		}

	}

	@Override
	public void end()
	{
		for(Zone zone : this.zones)
			zone.dispose();
		super.end();
	}


	@Override
	public void run()
	{
		super.run();
		for(Zone zone : zones)
		{
			TeamColor dominant = null;
			int dominantPlayers = 0;
			int totalPlayers = 0;
			for(TeamColor team : getTeams())
			{
				int players = 0;
				for(Player player : getCurrentlyPlayingPlayers(team))
				{
					if(player.isDead())
						continue;

					if(!zone.getRegion().contains(player.getLocation()))
						continue;
					players++;
					totalPlayers++;
				}

				if(players > dominantPlayers)
				{
					dominantPlayers = players;
					dominant = team;
				}
			}

			TeamColor oldOwner = zone.getOwner();
			zone.capture(dominant, dominantPlayers * 2 - totalPlayers);

			if(oldOwner != zone.getOwner())
			{
				if(zone.getOwner() == TeamColor.NONE)
				{
					for(Player current : getCurrentlyPlayingPlayers(dominant))
						if(zone.getRegion().contains(current.getLocation()))
							getPlayerData(current).addBonus(Bonus.DOM_NEUTRALIZE);
				}
				else if(zone.getOwner() == dominant)
					for(Player current : getCurrentlyPlayingPlayers(dominant))
						if(zone.getRegion().contains(current.getLocation()))
						{
							getPlayerData(current).addBonus(Bonus.DOM_CAPTURE);
							getPlayerData(current).captureZone();
						}
			}

			this.score.put(zone.getOwner(), getScore(zone.getOwner()) + 1);

		}
		updateBoard();
	}

	public List<Zone> getZones()
	{
		return this.zones;
	}

	@Override
	public Message getStartMessage()
	{
		return PvPMessage.GAME_START_DOM;
	}

	@Override
	public Message getGuide()
	{
		return PvPMessage.GAME_GUIDE_DOM;
	}
}
