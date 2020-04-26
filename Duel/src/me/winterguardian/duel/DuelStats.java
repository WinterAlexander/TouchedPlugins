package me.winterguardian.duel;

import me.winterguardian.core.playerstats.MappedData;
import me.winterguardian.core.playerstats.PlayerStats;
import me.winterguardian.core.util.TextUtil;

import java.util.HashMap;

public class DuelStats extends PlayerStats
{
	
	public DuelStats(MappedData data)
	{
		super(data);
	}

	public int getVictories()
	{
		return this.getContent().getInt("duel.victories");
	}

	public void setVictories(int victories)
	{
		this.getContent().set("duel.victories", victories);
	}
	
	public void addVictories(int victories)
	{
		this.setVictories(this.getVictories() + victories);
	}

	public int getGamesPlayed()
	{
		return this.getContent().getInt("duel.gamesplayed");
	}

	public void setGamesPlayed(int gamesPlayed)
	{
		this.getContent().set("duel.gamesplayed", gamesPlayed);
	}
	
	public void addGamesPlayed(int gamesPlayed)
	{
		this.setGamesPlayed(this.getGamesPlayed() + gamesPlayed);
	}

	public int getSurrenders()
	{
		return this.getContent().getInt("duel.surrenders");
	}

	public void setSurrenders(int surrenders)
	{
		this.getContent().set("duel.surrenders", surrenders);
	}
	
	public void addSurrenders(int surrenders)
	{
		this.setSurrenders(this.getSurrenders() + surrenders);
	}
	
	public double getPercentageWin()
	{
		return Math.round((double)getVictories() / (double)(getGamesPlayed() + (getGamesPlayed() == 0 ? 1 : 0)) * 10_000.0D) / 100.0D;
	}

	public static HashMap<String, String> getTables()
	{
		return TextUtil.map("duel->victories INTEGER, gamesplayed INTEGER, surrenders INTEGER");
	}
}
