package me.winterguardian.hub.listener;

import me.winterguardian.hub.Hub;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBurnEvent;
import org.bukkit.event.block.BlockFadeEvent;
import org.bukkit.event.block.BlockGrowEvent;
import org.bukkit.event.block.BlockSpreadEvent;
import org.bukkit.event.block.LeavesDecayEvent;

public class RegionProtectionListener implements Listener
{
	@EventHandler
	public void onBlockBurn(BlockBurnEvent event)
	{
		if(Hub.getPlugin().contains(event.getBlock().getLocation()))
			event.setCancelled(true);
	}
	
	@EventHandler
	public void onBlockGrow(BlockGrowEvent event)
	{
		if(Hub.getPlugin().contains(event.getBlock().getLocation()))
			event.setCancelled(true);
	}
	
	@EventHandler
	public void onBlockFade(BlockFadeEvent event)
	{
		if(Hub.getPlugin().contains(event.getBlock().getLocation()))
			event.setCancelled(true);
	}
	
	@EventHandler
	public void onBlockSpread(BlockSpreadEvent event)
	{
		if(Hub.getPlugin().contains(event.getBlock().getLocation()))
			event.setCancelled(true);
	}
	
	@EventHandler
	public void onLeavesDecay(LeavesDecayEvent event)
	{
		if(Hub.getPlugin().contains(event.getBlock().getLocation()))
			event.setCancelled(true);
	}
}
