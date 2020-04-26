package me.winterguardian.mobracers.vehicle;

import me.winterguardian.mobracers.stats.CoursePurchase;
import org.bukkit.entity.Player;

public abstract class PurchasableVehicle extends Vehicle
{
	public abstract CoursePurchase getPurchase();
	
	@Override
	public boolean canChoose(Player p)
	{
		return getPurchase().hasPurchased(p);
	}
}
