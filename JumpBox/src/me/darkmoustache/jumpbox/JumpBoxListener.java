package me.darkmoustache.jumpbox;

import me.winterguardian.core.Core;
import me.winterguardian.core.util.FireworkUtil;
import me.winterguardian.core.util.PlayerUtil;
import me.winterguardian.core.world.LocationUtil;
import me.winterguardian.core.world.SerializableLocation;
import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.BlockFace;
import org.bukkit.block.Sign;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.block.SignChangeEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.EntityDamageEvent.DamageCause;
import org.bukkit.event.entity.FoodLevelChangeEvent;
import org.bukkit.event.player.PlayerDropItemEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.event.player.PlayerPickupItemEvent;
import org.bukkit.event.player.PlayerTeleportEvent;

public class JumpBoxListener implements Listener
{
	@SuppressWarnings("deprecation")
	@EventHandler
	public void onPlayerClickCheckpoint(PlayerInteractEvent e)
	{
		if(!e.getPlayer().getAllowFlight() && e.getPlayer().getGameMode() != GameMode.CREATIVE)
			if(JumpBox.getSettings().isInRegion(e.getPlayer().getLocation()))
				if(e.getAction() == Action.LEFT_CLICK_BLOCK || e.getAction() == Action.RIGHT_CLICK_BLOCK) 
					if ((e.getClickedBlock().getState() instanceof Sign))
					{
						Sign s = (Sign)e.getClickedBlock().getState();
					    	
						if (s.getLine(1).contains("Checkpoint") || s.getLine(1).contains("Départ"))
							if(LocationUtil.isTeleportable(e.getPlayer().getLocation()) || e.getPlayer().isOnGround())
							{
								JumpBox.getSettings().setLocation(e.getPlayer(), e.getPlayer().getLocation(), s.getLine(1).contains("Départ"));
								if(s.getLine(1).contains("Départ"))
									JumpBoxMessage.BEGIN_SET.say(e.getPlayer());  
								else
									JumpBoxMessage.CP_SET.say(e.getPlayer());   
								//p.getWorld().playSound(p.getLocation(),Sound.SOUND,1, 0);
							}
					}
		}
	
	@EventHandler
	public void onCreateCheckpoint(SignChangeEvent e)
	{
		Player p = e.getPlayer();

		if (p.hasPermission(JumpBox.STAFF))
		{
			if (JumpBox.getSettings().isInRegion(e.getPlayer().getLocation()))
	 		{
				if (e.getLine(0).equalsIgnoreCase("jb"))
				{
					e.setLine(0, "§f-------------");
					if(e.getLine(1).equalsIgnoreCase("begin"))
						e.setLine(1, "§8Départ");
					else
						e.setLine(1, "§8Checkpoint");
					
					e.setLine(2, "§f[§aCliquez§f]");
					e.setLine(3, "§f-------------");
					JumpBoxMessage.CP_CREATE.say(e.getPlayer());
				}
	 		}
		}
	}
	
	@EventHandler
	public void onPlayerFinish(PlayerInteractEvent e)
	{
		if(e.getAction() == Action.PHYSICAL && !e.getPlayer().getAllowFlight() && e.getPlayer().getGameMode() != GameMode.CREATIVE)
		{
			if(JumpBox.getSettings().isInRegion(e.getPlayer().getLocation()))
			{
				Material block = e.getClickedBlock().getType();
				Material blockunder = e.getClickedBlock().getRelative(BlockFace.DOWN).getType();
				Material blockunder2 = e.getClickedBlock().getRelative(BlockFace.DOWN).getRelative(BlockFace.DOWN).getType();
	
				if(!((block == Material.WOOD_PLATE && blockunder == Material.WOOD)
				|| (block == Material.STONE_PLATE && blockunder == Material.STONE)
				|| (block == Material.IRON_PLATE && blockunder == Material.IRON_BLOCK)
				|| (block == Material.GOLD_PLATE && blockunder == Material.GOLD_BLOCK)))
					return;
				
				int point = 0;
				
				JumpBoxStats stats = new JumpBoxStats(e.getPlayer(), Core.getUserDatasManager().getUserData(e.getPlayer()));
				
				boolean noCheckpoint = false;
				
				if (stats.isFakeCheckpoint() || stats.getLocation() == null)
				{
			       	noCheckpoint = true;
			        JumpBoxMessage.NO_CP.say(e.getPlayer());
				}
				
				if (block == Material.WOOD_PLATE && blockunder == Material.WOOD)
				{
					point = JumpBox.getSettings().getEasyReward();
					for(Player current : Bukkit.getOnlinePlayers())
			        	if (current.getWorld().equals(JumpBox.getSettings().getWorld()))
			        		if(JumpBox.getSettings().isInRegion(current.getLocation()))
			        			current.sendMessage(JumpBoxMessage.prefix + " §f" + e.getPlayer().getName() + " §7a terminé un Jump §afacile" + (noCheckpoint ? " §f§lsans checkpoints §7" : " §7") + "!");
					stats.setEasyFinished(stats.getEasyFinished() + 1);
				}
				else if (block == Material.STONE_PLATE && blockunder == Material.STONE)
				{
			        point = JumpBox.getSettings().getMediumReward();
			        
			        for(Player current : Bukkit.getOnlinePlayers())
			        	if(JumpBox.getSettings().isInRegion(current.getLocation()))
			        		current.sendMessage(JumpBoxMessage.prefix + " §f" + e.getPlayer().getName() + " §7a terminé un Jump §emoyen" + (noCheckpoint ? " §f§lsans checkpoints §7" : " §7") + "!");
			        stats.setMediumFinished(stats.getMediumFinished() + 1);
				}
				else if (block == Material.GOLD_PLATE && blockunder == Material.GOLD_BLOCK && blockunder2 == Material.GOLD_BLOCK)
				{
					point = 100_000;
					for(Player p : Bukkit.getOnlinePlayers())
						p.sendMessage(JumpBoxMessage.prefix + " §f" + e.getPlayer().getName() + " §7a terminé le Jump §c§lR§6§la§e§li§a§ln§b§lb§9§lo§5§lw" + (noCheckpoint ? " §f§lsans checkpoints §7" : " §7") + "!");
					stats.setExpertFinished(stats.getExpertFinished() + 1);
				}
				else if (block == Material.GOLD_PLATE && blockunder == Material.GOLD_BLOCK)
				{
					point = JumpBox.getSettings().getExpertReward();
					for(Player p : Bukkit.getOnlinePlayers())
						p.sendMessage(JumpBoxMessage.prefix + " §f" + e.getPlayer().getName() + " §7a terminé le Jump §cexpert" + (noCheckpoint ? " §f§lsans checkpoints §7" : " §7") + "!");
					stats.setExpertFinished(stats.getExpertFinished() + 1);
				}
				else if (block == Material.IRON_PLATE && blockunder == Material.IRON_BLOCK)
				{
			        point = JumpBox.getSettings().getHardReward();
			        for(Player current : Bukkit.getOnlinePlayers())
			        	if(JumpBox.getSettings().isInRegion(current.getLocation()))
			        	current.sendMessage(JumpBoxMessage.prefix + " §f" + e.getPlayer().getName() + " §7a terminé un Jump §6difficile" + (noCheckpoint ? " §f§lsans checkpoints §7" : " §7") + "!");
			        stats.setHardFinished(stats.getHardFinished() + 1);
				}
				
				if(stats.getLastJump() != null)
					if(stats.getLastJump().toString().equals(new SerializableLocation(e.getClickedBlock().getLocation()).toString()))
					{
						point /= 5;
						JumpBoxMessage.WASLASTJUMP.say(e.getPlayer());
					}
				
				if(noCheckpoint)
					point *= JumpBox.getSettings().getNoCheckpointBonus();
				stats.setLastJumpLocation(e.getClickedBlock().getLocation());
				
				
				stats.addJumpBoxPoints(point); 
				
				stats.setLocation(null);
				stats.setFakeCheckpoint(false);
				
				e.getPlayer().teleport(JumpBox.getSettings().getSpawn());
				FireworkUtil.generateRandom(e.getPlayer().getLocation());
			}
		}
	}
	
	@EventHandler
	public void onPlayerFall(PlayerMoveEvent event) 
	{
		if(JumpBox.getSettings().isInRegion(event.getFrom()) && !JumpBox.getSettings().isInRegion(event.getTo()))
		{
			if(!event.getPlayer().getAllowFlight() && event.getPlayer().getGameMode() != GameMode.CREATIVE)
			{
				//p.getWorld().playSound(p.getLocation(),Sound.SOUND,1, 0);
				Location checkpoint = JumpBox.getSettings().getLocation(event.getPlayer());
				if(checkpoint != null)
					event.getPlayer().teleport(checkpoint);
				else
					event.getPlayer().teleport(JumpBox.getSettings().getSpawn());
			}
		}
	 }
	
	@EventHandler(priority = EventPriority.LOWEST, ignoreCancelled = true)
	public void onPlayerTeleportL(PlayerTeleportEvent event)
	{
		if(JumpBox.getSettings().isInRegion(event.getFrom()) && !JumpBox.getSettings().isInRegion(event.getTo()))
			JumpBox.getPlugin().leave(event.getPlayer());
	}
	
	@EventHandler(priority = EventPriority.HIGHEST, ignoreCancelled = true)
	public void onPlayerTeleportH(PlayerTeleportEvent event)
	{
		if(!JumpBox.getSettings().isInRegion(event.getFrom()) && JumpBox.getSettings().isInRegion(event.getTo()))
			JumpBox.getPlugin().join(event.getPlayer());
	}
	
	@EventHandler(priority = EventPriority.HIGHEST, ignoreCancelled = true)
	public void onPlayerMoveH(PlayerMoveEvent event)
	{
		if(!JumpBox.getSettings().isInRegion(event.getFrom()) && JumpBox.getSettings().isInRegion(event.getTo()))
			JumpBox.getPlugin().join(event.getPlayer());
	}
	
	@EventHandler
	public void onFoodLevelChange(FoodLevelChangeEvent event)
	{
		if(!(event.getEntity() instanceof Player))
			return;
		if(JumpBox.getSettings().isInRegion(event.getEntity().getLocation()))
		{
			event.setCancelled(true);
			event.setFoodLevel(20);
		}
	}
	
	@EventHandler
	public void onEntityDamage(EntityDamageEvent event)
	{
		if(!(event.getEntity() instanceof Player))
			return;
		
		if(JumpBox.getSettings().isInRegion(event.getEntity().getLocation()))
			if(event.getCause() != DamageCause.CONTACT)
				event.setCancelled(true);
			else
				event.setDamage(0);
	}
	
	@EventHandler
	public void onPlayerPickupItem(PlayerPickupItemEvent event)
	{
		if(JumpBox.getSettings().isInRegion(event.getPlayer().getLocation()))
			if(event.getPlayer().getGameMode() != GameMode.CREATIVE)
				event.setCancelled(true);
	}
	
	@EventHandler
	public void onPlayerDropItem(PlayerDropItemEvent event)
	{
		if(JumpBox.getSettings().isInRegion(event.getPlayer().getLocation()))
			if(event.getPlayer().getGameMode() != GameMode.CREATIVE)
			{
				event.getItemDrop().remove();
				PlayerUtil.clearInventory(event.getPlayer());
			}
	}
	
	@EventHandler
	public void onPlayerBreak(BlockBreakEvent event)
	{
		if(JumpBox.getSettings().isInRegion(event.getPlayer().getLocation()))
			if(event.getPlayer().getGameMode() != GameMode.CREATIVE)
				if(!event.getPlayer().hasPermission(JumpBox.STAFF))
					event.setCancelled(true);
	}
	
	@EventHandler
	public void onPlayerPlace(BlockPlaceEvent event)
	{
		if(JumpBox.getSettings().isInRegion(event.getPlayer().getLocation()))
			if(event.getPlayer().getGameMode() != GameMode.CREATIVE)
				if(!event.getPlayer().hasPermission(JumpBox.STAFF))
					event.setCancelled(true);
	}
}
