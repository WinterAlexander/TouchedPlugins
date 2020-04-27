package me.winterguardian.pvp.stats;

import me.winterguardian.core.Core;
import me.winterguardian.core.playerstats.MappedData;
import me.winterguardian.core.playerstats.PlayerStats;
import me.winterguardian.core.util.MathUtil;
import me.winterguardian.core.util.TextUtil;
import me.winterguardian.pvp.PvPMessage;
import me.winterguardian.pvp.PvPPlugin;
import me.winterguardian.pvp.game.GameOutcome;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;

import java.util.HashMap;
import java.util.List;
import java.util.UUID;

public class PvPStats extends PlayerStats
{
	private OfflinePlayer player;

	public PvPStats(OfflinePlayer player, MappedData data)
	{
		super(data);
		this.player = player;
	}

	public OfflinePlayer getPlayer()
	{
		return player;
	}
	
	public int getKills()
	{
		return getContent().getInt("pvp.kills");
	}
	
	public void setKills(int kills)
	{
		getContent().set("pvp.kills", kills);
	}
	
	public int getDeaths()
	{
		return getContent().getInt("pvp.deaths");
	}
	
	public void setDeaths(int deaths)
	{
		getContent().set("pvp.deaths", deaths);
	}
	
	public int getAssists()
	{
		return getContent().getInt("pvp.assists");
	}
	
	public void setAssists(int assists)
	{
		getContent().set("pvp.assists", assists);
	}
	
	public int getVictories()
	{
		return getContent().getInt("pvp.victories");
	}
	
	public void setVictories(int victories)
	{
		getContent().set("pvp.victories", victories);
	}
	
	public int getGamesPlayed()
	{
		return getContent().getInt("pvp.gamesplayed");
	}
	
	public void setGamesPlayed(int gamesPlayed)
	{
		getContent().set("pvp.gamesplayed", gamesPlayed);
	}
	
	public long getTimePlayed()
	{
		return getContent().getLong("pvp.timeplayed");
	}
	
	public void setTimePlayed(long timePlayed)
	{
		getContent().set("pvp.timeplayed", timePlayed);
	}

	public int getCapturedFlags()
	{
		return getContent().getInt("pvp.capturedflags");
	}
	
	public void setCapturedFlags(int flags)
	{
		getContent().set("pvp.capturedflags", flags);
	}
	
	public int getCapturedZones()
	{
		return getContent().getInt("pvp.capturedzones");
	}
	
	public void setCapturedZones(int flags)
	{
		getContent().set("pvp.capturedzones", flags);
	}
	
	public int getBestKillingSpree()
	{
		return getContent().getInt("pvp.bestkillingspree");
	}
	
	public void setBestKillingSpree(int killstreak)
	{
		getContent().set("pvp.bestkillingspree", killstreak);
	}
	
	public int getBestGameKills()
	{
		return getContent().getInt("pvp.bestgamekills");
	}
	
	public void setBestGameKills(int bestgamekills)
	{
		getContent().set("pvp.bestgamekills", bestgamekills);
	}
	
	public int getScore()
	{
		return getContent().getInt("pvp.score");
	}
	
	public void setScore(int score)
	{
		getContent().set("pvp.score", score);
	}
	
	public void gameSummary(GameOutcome outcome, int kills, int deaths, int assists, long playTime, int capturedFlags, int capturedZones, int killingSpree, List<Bonus> bonus)
	{
		if(playTime < 500000 && kills == 0 && deaths == 0 && assists == 0 && capturedFlags == 0 && capturedZones == 0 && outcome.isBad())
			return;

		Player player = this.getPlayer().getPlayer();
		int points = 0;
		
		int previousLevel = getDisplayLevel();
		
		PvPMessage.STATS_SUMMARY_SEPARATOR.sayIfOnline(getPlayer());
		PvPMessage.STATS_SUMMARY_TITLE.sayIfOnline(getPlayer());
			
		if(outcome == GameOutcome.FIRST)
		{
			PvPMessage.STATS_SUMMARY_FIRSTPLACE.sayIfOnline(getPlayer(), "#", "500");
			points += 500;
			setVictories(getVictories() + 1);
		}
		else if(outcome == GameOutcome.SECOND)
		{
			PvPMessage.STATS_SUMMARY_SECONDPLACE.sayIfOnline(getPlayer(), "#", "250");
			points += 250;
		}
		else if(outcome == GameOutcome.THIRD)
		{
			PvPMessage.STATS_SUMMARY_THIRDPLACE.sayIfOnline(getPlayer(), "#", "50");
			points += 50;
		}
		else if(outcome == GameOutcome.TEAM_WIN)
		{
			PvPMessage.STATS_SUMMARY_TEAMVICTORY.sayIfOnline(player, "#", "400");
			points += 400;
			setVictories(getVictories() + 1);
		}
		else if(outcome == GameOutcome.TEAM_LOSE)
		{
			PvPMessage.STATS_SUMMARY_TEAMLOSE.sayIfOnline(player, "#", "-100");
			points -= 100;
		}
		else if(outcome == GameOutcome.WON_AS_HUMAN)
		{
			PvPMessage.STATS_SUMMARY_INFWONASHUMAN.sayIfOnline(player, "#", "400");
			points += 400;
			setVictories(getVictories() + 1);
		}
		else if(outcome == GameOutcome.WON_AS_INFECTED)
		{
			PvPMessage.STATS_SUMMARY_INFWONASINFECTED.sayIfOnline(player, "#", "500");
			points += 500;
			setVictories(getVictories() + 1);
		}

		setGamesPlayed(getGamesPlayed() + 1);

		PvPMessage.STATS_SUMMARY_PARTICIPATION.sayIfOnline(player, "#", "50");
		points += 50;
		
		if(kills > 0)
		{
			int killPoints = kills * 20;
			PvPMessage.STATS_SUMMARY_KILLS.sayIfOnline(player, "<kills>", "" + kills, "#", "" + killPoints);
			points += killPoints;
			setKills(getKills() + kills);
		}
		
		if(deaths > 0)
		{
			int deathPoints = deaths * -10;
			PvPMessage.STATS_SUMMARY_DEATHS.sayIfOnline(player, "<deaths>", "" + deaths, "#", "" + deathPoints);
			points += deathPoints;
			setDeaths(getDeaths() + deaths);
		}
		
		if(assists > 0)
		{
			int assistsPoints = assists * 10;
			PvPMessage.STATS_SUMMARY_ASSISTS.sayIfOnline(player, "<assists>", "" + assists, "#", "" + assistsPoints);
			points += assistsPoints;
			setAssists(getAssists() + assists);
		}
		
		if(capturedFlags > 0)
		{
			int flagPoints = capturedFlags * 25;
			PvPMessage.STATS_SUMMARY_CAPTUREDFLAGS.sayIfOnline(player, "<flags>", "" + capturedFlags, "#", "" + flagPoints);
			points += flagPoints;
			setCapturedFlags(getCapturedFlags() + capturedFlags);
		}
		
		if(capturedZones > 0)
		{
			int zonePoints = capturedZones * 15;
			PvPMessage.STATS_SUMMARY_CAPTUREDZONES.sayIfOnline(player, "<zones>", "" + capturedZones, "#", "" + zonePoints);
			points += zonePoints;
			setCapturedZones(getCapturedZones() + capturedZones);
		}
		
		if(bonus.size() > 0)
		{
			int bonusPoints = 0;
			for(Bonus b : bonus)
				bonusPoints += b.getValue();
			
			PvPMessage.STATS_SUMMARY_BONUS.sayIfOnline(player, "<bonus>", "" + bonus.size(), "#", "" + bonusPoints);
			
			points += bonusPoints;
		}
		
		if(kills > this.getBestGameKills())
			this.setBestGameKills(kills);
		
		if(killingSpree > this.getBestKillingSpree())
			this.setBestKillingSpree(killingSpree);
		
		this.setTimePlayed(this.getTimePlayed() + playTime);
		
		PvPMessage.STATS_SUMMARY_TOTAL.sayIfOnline(player, "#", "" + points);
		PvPMessage.STATS_SUMMARY_RATIO.sayIfOnline(getPlayer(), "#", (kills <= 0 ? "0" : (deaths > 0 ? "" + MathUtil.round((double)kills / (double)deaths, 3) : PvPMessage.STATS_SUMMARY_RATIO_PERFECT.toString())));
		if(getDisplayLevel() > previousLevel)
			PvPMessage.STATS_SUMMARY_LEVELUP.sayIfOnline(getPlayer());
		
		setScore(getScore() + points);
		addPoints(points);

		PvPMessage.STATS_SUMMARY_SEPARATOR.sayIfOnline(getPlayer());
	}

	public String getPvPName()
	{
		String name = getName();
		
		if(getPlayer().isOnline() && PvPPlugin.getGame().getConfig().useDisplaynames())
			name = getPlayer().getPlayer().getDisplayName();
		
		return getPvPName(getDisplayLevel(), name);
	}

	public int getDisplayLevel()
	{
		return (int)getLevel() + (getProgression() == 100 ? 1 : 0);
	}

	public int getProgression()
	{
		return (int) MathUtil.round((getLevel() - (int)getLevel()) * 100, 0);
	}

	public double getRatio()
	{
		return (double)getKills() / (getDeaths() > 0 ? (double)getDeaths() : 1d);
	}
	
	public double getRoundedRatio()
	{
		return MathUtil.round(getRatio(), 3);
	}
	
	public double getLevel()
	{
		if(getKills() == 0)
			return 0;
		return -50 * Math.pow(1.0000341554255, -(3.5 * (getKills() + getAssists() / 2 + getVictories() * 10 + getCapturedFlags() * 3 + getCapturedZones() * 2) * Math.pow(getRatio(), 0.35) - 20000)) + 100;
	}
	
	public static String getPvPName(int level, String name)
	{
		return "§" + (level >= 100 ? "4" : (level >= 80 ? "c" : (level >= 60 ? "6" : (level >= 40 ? "e" : (level >= 20 ? "a" : (level >= 1 ? "3" : "8")))))) + "Niveau " + level + " §r" + name;
	}

	public static PvPStats get(UUID id)
	{
		OfflinePlayer player = Bukkit.getOfflinePlayer(id);
		return new PvPStats(player, Core.getUserDatasManager().getUserData(player));
	}

	public static HashMap<String, String> getTables()
	{
		return TextUtil.map("pvp->" +
				"kills INTEGER, " +
				"deaths INTEGER, " +
				"assists INTEGER, " +
				"victories INTEGER, " +
				"gamesplayed INTEGER, " +
				"timeplayed BIGINT, " +
				"capturedflags INTEGER, " +
				"capturedzones INTEGER, " +
				"bestkillingspree INTEGER, " +
				"bestgamekills INTEGER, " +
				"score INTEGER");
	}
}
