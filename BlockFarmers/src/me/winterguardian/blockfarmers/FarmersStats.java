package me.winterguardian.blockfarmers;

import me.winterguardian.core.Core;
import me.winterguardian.core.playerstats.MappedData;
import me.winterguardian.core.playerstats.PlayerStats;
import me.winterguardian.core.util.TextUtil;
import org.bukkit.OfflinePlayer;

import java.util.HashMap;

public class FarmersStats extends PlayerStats
{
	private OfflinePlayer farmer;

	public FarmersStats(OfflinePlayer farmer, MappedData data)
	{
		super(data);
		this.farmer = farmer;
	}
	
	public int getScore()
	{
		return this.getContent().getInt("blockfarmers.score");
	}
	
	public void setScore(int score)
	{
		this.getContent().set("blockfarmers.score", score);
	}
	
	public void addGamePoints(int points)
	{
		super.addPoints(points);
		this.setScore(this.getScore() + points);
		if(this.farmer.isOnline())
			this.farmer.getPlayer().sendMessage("§aPoints +" + points);
	}
	
	public int getVictories()
	{
		return this.getContent().getInt("blockfarmers.victories");
	}

	public void setVictories(int victories)
	{
		this.getContent().set("blockfarmers.victories", victories);
	}

	public int getGamesPlayed()
	{
		return this.getContent().getInt("blockfarmers.gamesplayed");
	}

	public void setGamesPlayed(int gamesPlayed)
	{
		this.getContent().set("blockfarmers.gamesplayed", gamesPlayed);
	}
	
	public int getTilesFarmed()
	{
		return this.getContent().getInt("blockfarmers.tiles");
	}

	public void setTilesFarmed(int tiles)
	{
		this.getContent().set("blockfarmers.tiles", tiles);
	}
	
	public static HashMap<String, String> getTables()
    {
        return TextUtil.map("blockfarmers->score INTEGER, victories INTEGER, gamesplayed INTEGER, tiles INTEGER");
    }
}
