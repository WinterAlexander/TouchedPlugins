package me.winterguardian.mobracers.item;

import me.winterguardian.mobracers.state.game.GameState;
import me.winterguardian.mobracers.vehicle.Vehicle;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

public abstract class Item
{
	public abstract String getName();
	public abstract ItemType getType();
	public abstract ItemResult onUse(Player player, Vehicle vehicle, GameState game);
	public abstract ItemStack getItemStack();
	public abstract void cancel();
}
