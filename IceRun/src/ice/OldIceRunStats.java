package ice;

import me.winterguardian.core.userdata.UserData;

public class OldIceRunStats extends UserData
{
	
	public OldIceRunStats(UserData userData)
	{
		super(userData);
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
		return this.getContent().getInt("icerun.games-played");
	}

	public void setGamesPlayed(int gamesPlayed)
	{
		this.getContent().set("icerun.games-played", gamesPlayed);
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
}
