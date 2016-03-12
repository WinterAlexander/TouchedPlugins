package ice;

import me.winterguardian.core.playerstats.MappedData;
import me.winterguardian.core.playerstats.PlayerStats;
import me.winterguardian.core.util.TextUtil;

import java.util.HashMap;

/**
 *
 * Created by 1541869 on 2015-11-25.
 */
public class IceRunStats extends PlayerStats
{
	public IceRunStats(MappedData data)
	{
		super(data);
	}

	public int getVictories()
	{
		return this.getContent().getInt("icerun.victories");
	}

	public void setVictories(int victories)
	{
		this.getContent().set("icerun.victories", victories);
	}

	public int getGamesPlayed()
	{
		return this.getContent().getInt("icerun.gamesplayed");
	}

	public void setGamesPlayed(int gamesPlayed)
	{
		this.getContent().set("icerun.gamesplayed", gamesPlayed);
	}

	public int getScore()
	{
		return this.getContent().getInt("icerun.score");
	}

	public void setScore(int score)
	{
		this.getContent().set("icerun.score", score);
	}

	public double getPercentageWin()
	{
		return Math.round((double)getVictories() / (double)(getGamesPlayed() + (getGamesPlayed() == 0 ? 1 : 0)) * 10_000.0D) / 100.0D;
	}

	public static HashMap<String, String> getTables()
	{
		return TextUtil.map("icerun->victories INTEGER, gamesplayed INTEGER, score INTEGER");
	}
}
