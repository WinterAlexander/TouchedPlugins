package ice.listener;

import ice.IceRunMessage;
import ice.IceRun;

import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockFadeEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.EntityDamageEvent.DamageCause;
import org.bukkit.event.entity.EntitySpawnEvent;
import org.bukkit.event.entity.FoodLevelChangeEvent;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;
import org.bukkit.event.player.PlayerDropItemEvent;
import org.bukkit.event.player.PlayerInteractEntityEvent;
import org.bukkit.event.player.PlayerPickupItemEvent;

public class ProtectionListener implements Listener
{
	@EventHandler
	public void PlayerCommand(PlayerCommandPreprocessEvent event)
	{
		if(IceRun.getPlugin().contains(event.getPlayer()))
		{
			String cmd;
			if (event.getMessage().contains(" "))
				cmd = event.getMessage().split(" ")[0].replace("/", "");
			
			else
				cmd = event.getMessage().replace("/", "");
	
			for(String allowedCommand : IceRun.getSettings().getAllowedCommands())
				if(allowedCommand.equalsIgnoreCase(cmd))
					return;
			
			event.setCancelled(true);
			IceRunMessage.GAME_NOCOMMAND.say(event.getPlayer());
		}
	}
	
	@EventHandler
	public void onItemDrop(PlayerDropItemEvent e)
	{
		if (IceRun.getPlugin().contains(e.getPlayer()))
			e.setCancelled(true);
	}
	
	@EventHandler
	public void onItemPickup(PlayerPickupItemEvent e)
	{
		if (IceRun.getPlugin().contains(e.getPlayer()))
			e.setCancelled(true);
	}
	
	
	@EventHandler
	public void onEntityDamage(EntityDamageEvent e)
	{
		if(e.getEntity() instanceof Player && IceRun.getPlugin().contains((Player) e.getEntity()))
			if(e.getCause() != DamageCause.PROJECTILE)
				e.setCancelled(true);
	}
	
	@EventHandler
	public void onFoodLevelChange(FoodLevelChangeEvent e)
	{
		if (e.getEntity() instanceof Player && IceRun.players.contains(e.getEntity()))
		{
			e.setFoodLevel(20);
			e.setCancelled(true);
		}
	}
	
	@EventHandler
	public void onPlayerInteractEntity(PlayerInteractEntityEvent e)
	{
		if (IceRun.players.contains(e.getPlayer()))
			e.setCancelled(true);
	}

	@EventHandler
	public void onBlockBreak(BlockBreakEvent event)
	{
		if (IceRun.players.contains(event.getPlayer()))
			event.setCancelled(true);
	}

	@EventHandler
	public void onPlayerPlace(BlockPlaceEvent event)
	{
		if (IceRun.players.contains(event.getPlayer()))
			event.setCancelled(true);
	}
	
	@EventHandler
	public void onBlockFade(BlockFadeEvent event)
	{
		if(IceRun.getSettings().getRegion() != null && IceRun.getSettings().getRegion().contains(event.getBlock().getLocation()))
			event.setCancelled(true);
	}
	
	@EventHandler
	public void onEntitySpawn(EntitySpawnEvent event)
	{
		if(event.getEntityType() == EntityType.CHICKEN)
			if(IceRun.getSettings().getRegion() != null && IceRun.getSettings().getRegion().contains(event.getLocation()))
				event.setCancelled(true);
	}
}
