package me.winterguardian.hub;

import org.bukkit.entity.Player;

public class TimeTask implements Runnable
{
	@Override
	public void run()
	{
		for(Player p : Hub.getPlugin().getPlayers())
		{
			long ticksOfToday = System.currentTimeMillis() % 86_400_000 / 3600 - 5000;
			if(ticksOfToday < 0)
				ticksOfToday += 24_000;
			p.setPlayerTime(ticksOfToday, false);
		}
	}
}
