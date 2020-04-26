package me.winterguardian.mobracers.vehicle;

import me.winterguardian.mobracers.stats.CourseAchievement;
import org.bukkit.entity.Player;

public abstract class WinnableVehicle extends Vehicle
{
	public abstract CourseAchievement getWinAchievement();
	
	@Override
	public boolean canChoose(Player p)
	{
		return getWinAchievement().isComplete(p);
	}
}
