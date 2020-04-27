package me.winterguardian.pvp.game;

/**
 * Undocumented :(
 * <p>
 * Created on 2020-04-26.
 *
 * @author Alexander Winter
 */
public enum GameOutcome
{
	FIRST,
	SECOND,
	THIRD,
	LOWER,

	TEAM_WIN,
	TEAM_LOSE,

	WON_AS_HUMAN,
	LOST_AS_HUMAN,
	WON_AS_INFECTED,
	LOST_AS_INFECTED


	;

	public boolean isBad()
	{
		return this == LOWER || this == TEAM_LOSE || this == LOST_AS_HUMAN || this == LOST_AS_INFECTED;
	}
}
