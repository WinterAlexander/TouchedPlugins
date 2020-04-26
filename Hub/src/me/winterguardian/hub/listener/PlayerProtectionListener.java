package me.winterguardian.hub.listener;


import me.winterguardian.core.util.PlayerUtil;
import me.winterguardian.hub.Hub;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.entity.EntityCombustEvent;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.FoodLevelChangeEvent;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.player.PlayerDropItemEvent;
import org.bukkit.event.player.PlayerPickupItemEvent;

public class PlayerProtectionListener implements Listener
{
	@EventHandler
	public void onFoodLevelChange(FoodLevelChangeEvent event)
	{
		if(event.getEntity() instanceof Player)
			if(Hub.getPlugin().contains((Player) event.getEntity()))
				event.setCancelled(true);
	}
	
	@EventHandler
	public void onPlayerBreak(BlockBreakEvent event)
	{
		if(event.getPlayer().hasPermission(Hub.RANK_ADMIN))
			return;
		if(Hub.getPlugin().contains(event.getPlayer()))
			event.setCancelled(true);
	}
	
	@EventHandler
	public void onPlayerPlace(BlockPlaceEvent event)
	{
		if(event.getPlayer().hasPermission(Hub.RANK_ADMIN))
			return;
		if(Hub.getPlugin().contains(event.getPlayer()))
			event.setCancelled(true);
	}
	
	@EventHandler
	public void onPlayerPickupItem(PlayerPickupItemEvent event)
	{
		if(event.getPlayer().hasPermission(Hub.RANK_ADMIN))
			return;
		
		if(Hub.getPlugin().contains(event.getPlayer()))
		{
			event.setCancelled(true);
			PlayerUtil.clearInventory(event.getPlayer());
		}
	}
	
	@EventHandler
	public void onPlayerDropItem(PlayerDropItemEvent event)
	{
		if(event.getPlayer().hasPermission(Hub.RANK_ADMIN))
			return;
		
		if(Hub.getPlugin().contains(event.getPlayer()))
		{
			event.setCancelled(true);
			PlayerUtil.clearInventory(event.getPlayer());
		}
	}
	
	@EventHandler
	public void onEntityDamage(EntityDamageEvent event)
	{
		if(event.getEntity() instanceof Player)
			if(Hub.getPlugin().contains((Player) event.getEntity()))
				if(event.getDamage() != 0)
					event.setCancelled(true);
	}
	
	@EventHandler
	public void onEntityDamage(EntityDamageByEntityEvent event)
	{
		if(event.getDamager() instanceof Player)
			if(Hub.getPlugin().contains((Player) event.getDamager()))
				event.setCancelled(true);
	}
	
	@EventHandler
	public void InventoryClickEvent(InventoryClickEvent event)
	{
		if(event.getWhoClicked().hasPermission(Hub.RANK_ADMIN))
			return;
		
		if(event.getWhoClicked() != null && event.getWhoClicked() instanceof Player)
			if(Hub.getPlugin().contains((Player) event.getWhoClicked()))
				event.setCancelled(true);
	}
	
	
	@EventHandler
	public void onEntityCombust(EntityCombustEvent event)
	{
		if(event.getEntity() instanceof Player)
			if(Hub.getPlugin().contains((Player) event.getEntity()))
				event.setCancelled(true);
	}
}
