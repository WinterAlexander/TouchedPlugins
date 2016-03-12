package me.winterguardian.pvp.stats;


import java.util.HashMap;

/**
 *
 * Created by Alexander Winter on 2015-12-19.
 */
public abstract class Top
{
	public abstract int getScore(PvPStats data);

	private HashMap<Integer, PvPStats> top;
	private String name;

	public Top(String name)
	{
		this.top = new HashMap<>();
		this.name = name;
	}

	public void init()
	{

	}

	public String getName()
	{
		return name;
	}
}
