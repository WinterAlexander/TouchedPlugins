package me.winterguardian.duel;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.FoodLevelChangeEvent;
import org.bukkit.event.player.PlayerDropItemEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.event.player.PlayerPickupItemEvent;
import org.bukkit.event.player.PlayerTeleportEvent;

public class ProtectionListener implements Listener
{
	@EventHandler
	public void onPlayerDropItem(PlayerDropItemEvent event)
	{
		if(Duel.getInstance().getSettings().isReady())
			if(Duel.getInstance().contains(event.getPlayer()))
				event.setCancelled(true);
	}
	
	@EventHandler
	public void onPlayerPickupItem(PlayerPickupItemEvent event)
	{
		if(Duel.getInstance().getSettings().isReady())
			if(Duel.getInstance().contains(event.getPlayer()))
				event.setCancelled(true);
	}
	
	@EventHandler
	public void onPlayerBreak(BlockBreakEvent event)
	{
		if(Duel.getInstance().getSettings().isReady())
			if(Duel.getInstance().getSettings().isInRegion(event.getBlock()))
				if(!event.getPlayer().hasPermission(Duel.MODERATION) && Duel.getInstance().contains(event.getPlayer()))
					event.setCancelled(true);
	}
	
	@EventHandler
	public void onPlayerPlace(BlockPlaceEvent event)
	{
		if(Duel.getInstance().getSettings().isReady())
			if(Duel.getInstance().getSettings().isInRegion(event.getBlock()))
				if(!event.getPlayer().hasPermission(Duel.MODERATION) && Duel.getInstance().contains(event.getPlayer()))
					event.setCancelled(true);
	}
	
	@EventHandler
	public void onFoodLevelChangeEvent(FoodLevelChangeEvent event)
	{
		if(Duel.getInstance().getSettings().isReady())
			if(event.getEntity() instanceof Player)
				if(Duel.getInstance().contains((Player) event.getEntity()))
					event.setCancelled(true);
	}
	
	@EventHandler
	public void onPlayerDamage(EntityDamageEvent event)
	{
		if(Duel.getInstance().getSettings().isReady())
			if(event.getEntity() instanceof Player)
				if(Duel.getInstance().contains((Player) event.getEntity()))
					if(!Duel.getInstance().getGame().canBattle((Player) event.getEntity()))
						event.setCancelled(true);
	}
	
	@EventHandler
	public void onPlayerMove(PlayerMoveEvent event)
	{
		if(!Duel.getInstance().getSettings().isReady())
			return;
		
		if(Duel.getInstance().getSettings().isInRegion(event.getTo().getBlock()) 
				&& !Duel.getInstance().getSettings().isInRegion(event.getFrom().getBlock()))
		{
			event.getPlayer().setPlayerListName("§6" + event.getPlayer().getName());
			Duel.getInstance().getGame().giveBoardAndTab(event.getPlayer());
		}
		else if(Duel.getInstance().getSettings().isInRegion(event.getFrom().getBlock())
				&& !Duel.getInstance().getSettings().isInRegion(event.getTo().getBlock()))
		{
			event.getPlayer().setPlayerListName(null);
			Duel.getInstance().leave(event.getPlayer());
		}	
	}
	
	@EventHandler
	public void onPlayerTeleport(PlayerTeleportEvent event)
	{
		if(!Duel.getInstance().getSettings().isReady())
			return;
		
		if(Duel.getInstance().getSettings().isInRegion(event.getTo().getBlock()) 
				&& !Duel.getInstance().getSettings().isInRegion(event.getFrom().getBlock()))
		{
			event.getPlayer().setPlayerListName("§6" + event.getPlayer().getName());
			Duel.getInstance().getGame().giveBoardAndTab(event.getPlayer());
		}
		else if(Duel.getInstance().getSettings().isInRegion(event.getFrom().getBlock())
				&& !Duel.getInstance().getSettings().isInRegion(event.getTo().getBlock()))
		{
			event.getPlayer().setPlayerListName(null);
			Duel.getInstance().leave(event.getPlayer());
		}
			
	}
}
