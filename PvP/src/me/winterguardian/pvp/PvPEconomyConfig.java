package me.winterguardian.pvp;

import me.winterguardian.core.game.Config;
import me.winterguardian.pvp.stats.Bonus;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;

/**
 * Holds the amount of point each parts of the game give
 * <p>
 * Created on 2020-06-27.
 *
 * @author Alexander Winter
 */
public class PvPEconomyConfig extends Config
{
	private int soloVictory, soloSecond, soloThird, teamVictory, teamDefeat, humanVictory, infectedVictory, participation, kill, death, assist, flagCaptured, zoneCaptured;

	public PvPEconomyConfig(File file)
	{
		super(file);
	}

	@Override
	protected void load(YamlConfiguration config)
	{
		for(Bonus bonus : Bonus.values())
			if(config.isInt("bonus." + bonus.name()))
				bonus.setValue(config.getInt("bonus." + bonus.name()));

		soloVictory = 500;
		soloSecond = 250;
		soloThird = 50;
		teamVictory = 400;
		teamDefeat = -100;
		humanVictory = 400;
		infectedVictory = 500;
		participation = 50;
		kill = 20;
		assist = 10;
		flagCaptured = 25;
		zoneCaptured = 15;

		if(config.isInt("game.soloVictory"))
			soloVictory = config.getInt("game.soloVictory");
		if(config.isInt("game.soloSecond"))
			soloSecond = config.getInt("game.soloSecond");
		if(config.isInt("game.soloThird"))
			soloThird = config.getInt("game.soloThird");
		if(config.isInt("game.teamVictory"))
			teamVictory = config.getInt("game.teamVictory");
		if(config.isInt("game.teamDefeat"))
			teamDefeat = config.getInt("game.teamDefeat");
		if(config.isInt("game.humanVictory"))
			humanVictory = config.getInt("game.humanVictory");
		if(config.isInt("game.infectedVictory"))
			infectedVictory = config.getInt("game.infectedVictory");
		if(config.isInt("game.participation"))
			participation = config.getInt("game.participation");
		if(config.isInt("game.kill"))
			kill = config.getInt("game.kill");
		if(config.isInt("game.death"))
			death = config.getInt("game.death");
		if(config.isInt("game.assist"))
			assist = config.getInt("game.assist");
		if(config.isInt("game.flagCaptured"))
			flagCaptured = config.getInt("game.flagCaptured");
		if(config.isInt("game.zoneCaptured"))
			zoneCaptured = config.getInt("game.zoneCaptured");
	}

	public int getSoloVictory()
	{
		return soloVictory;
	}

	public void setSoloVictory(int soloVictory)
	{
		this.soloVictory = soloVictory;
	}

	public int getSoloSecond()
	{
		return soloSecond;
	}

	public void setSoloSecond(int soloSecond)
	{
		this.soloSecond = soloSecond;
	}

	public int getSoloThird()
	{
		return soloThird;
	}

	public void setSoloThird(int soloThird)
	{
		this.soloThird = soloThird;
	}

	public int getTeamVictory()
	{
		return teamVictory;
	}

	public void setTeamVictory(int teamVictory)
	{
		this.teamVictory = teamVictory;
	}

	public int getTeamDefeat()
	{
		return teamDefeat;
	}

	public void setTeamDefeat(int teamDefeat)
	{
		this.teamDefeat = teamDefeat;
	}

	public int getHumanVictory()
	{
		return humanVictory;
	}

	public void setHumanVictory(int humanVictory)
	{
		this.humanVictory = humanVictory;
	}

	public int getInfectedVictory()
	{
		return infectedVictory;
	}

	public void setInfectedVictory(int infectedVictory)
	{
		this.infectedVictory = infectedVictory;
	}

	public int getParticipation()
	{
		return participation;
	}

	public void setParticipation(int participation)
	{
		this.participation = participation;
	}

	public int getKill()
	{
		return kill;
	}

	public void setKill(int kill)
	{
		this.kill = kill;
	}

	public int getDeath()
	{
		return death;
	}

	public void setDeath(int death)
	{
		this.death = death;
	}

	public int getAssist()
	{
		return assist;
	}

	public void setAssist(int assist)
	{
		this.assist = assist;
	}

	public int getFlagCaptured()
	{
		return flagCaptured;
	}

	public void setFlagCaptured(int flagCaptured)
	{
		this.flagCaptured = flagCaptured;
	}

	public int getZoneCaptured()
	{
		return zoneCaptured;
	}

	public void setZoneCaptured(int zoneCaptured)
	{
		this.zoneCaptured = zoneCaptured;
	}
}
