package ice.listener;

import ice.IceRun;
import ice.IceRunGame;
import ice.IceRunMessage;
import me.winterguardian.core.world.LocationUtil;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.block.Block;
import org.bukkit.entity.Egg;
import org.bukkit.entity.Player;
import org.bukkit.entity.Snowball;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.ProjectileHitEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.util.Vector;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class GameListener implements Listener
{
	private HashMap<Player, Long> lastUse;

	public GameListener()
	{
		this.lastUse = new HashMap<>();
	}

	@EventHandler
	public void onEntityDamageByEntity(EntityDamageByEntityEvent e)
	{
		if (((e.getEntity() instanceof Player)) && (IceRun.getPlugin().contains((Player) e.getEntity())))
			if(e.getDamager() instanceof Snowball)
			{
				e.setDamage(0.0);
				e.setCancelled(false);
			}
			else if(e.getDamager() instanceof Egg)
			{
				e.setDamage(0.0);
				e.setCancelled(false);
				if(((Player)e.getEntity()).hasPotionEffect(PotionEffectType.BLINDNESS))
					((Player)e.getEntity()).removePotionEffect(PotionEffectType.BLINDNESS);
				
				((Player)e.getEntity()).addPotionEffect(new PotionEffect(PotionEffectType.BLINDNESS, 80, 0, false, false));
				((Player)e.getEntity()).playSound(((Player)e.getEntity()).getLocation(), Sound.CHICKEN_IDLE, 10, 1);
			}
			else
				e.setCancelled(true);
	}
	
	@EventHandler
	public void onProjectileHit(ProjectileHitEvent e)
	{
		if ((e.getEntity() instanceof Snowball))
		{
			if (IceRun.players.contains(e.getEntity().getShooter()) && IceRun.status instanceof IceRunGame)
			{
				if (((IceRunGame)IceRun.status).canBreak())
				{
					List<Block> potential = new ArrayList<Block>();
					
					for(int x = -1; x < 2; x++)
						for(int y = -1; y < 2; y++)
							for(int z = -1; z < 2; z++)
								if(e.getEntity().getLocation().getBlock().getRelative(x, y, z).getState().getType() == Material.ICE
								|| e.getEntity().getLocation().getBlock().getRelative(x, y, z).getState().getType() == Material.PACKED_ICE)
									potential.add(e.getEntity().getLocation().getBlock().getRelative(x, y, z));

					
					Block hit = null;
					
					for(Block block : potential)
						if(hit == null)
							hit = block;
						else if(LocationUtil.distanceFromCenter(block, e.getEntity().getLocation()) < LocationUtil.distanceFromCenter(hit, e.getEntity().getLocation()))
							hit = block;

					if (hit != null)
					{
						if(hit.getType() == Material.PACKED_ICE)
							((IceRunGame)IceRun.status).addPackedIceBlock(hit.getLocation());
						
						if(hit.getType() == Material.ICE)
							if(!((IceRunGame)IceRun.status).isInPackedIceRegenList(hit.getLocation()))
								((IceRunGame)IceRun.status).addIceBlock(hit.getLocation());
						
						hit.setType(Material.AIR);
						((Player)e.getEntity().getShooter()).playSound(e.getEntity().getLocation(), Sound.ORB_PICKUP, 10f, 1f);
					}
				}
			}
		}
	}
	
	@EventHandler
	public void onPlayerInteract(PlayerInteractEvent e)
	{
		
		final Player p = e.getPlayer();
	
		if(!IceRun.getPlugin().contains(p))
			return;
		
		if(e.getItem() == null)
			return;
		
		if(!(IceRun.status instanceof IceRunGame) || !((IceRunGame)IceRun.status).canBreak())
		{
			e.setCancelled(true);
			Bukkit.getScheduler().runTaskLater(IceRun.getPlugin(), new Runnable()
			{
				@Override
				public void run()
				{
					p.updateInventory();
				}
			}, 2);
			return;
		}
			
		if(e.getItem().getType() != Material.SUGAR && e.getItem().getType() != Material.GLOWSTONE_DUST)
		{
			if(e.getItem().getType() != Material.SNOW_BALL && e.getItem().getType() != Material.EGG)
				e.setCancelled(true);
			return;
		}
		
		if(!((IceRunGame)IceRun.status).canBreak())
		{
			//IRMessage.GAME_NOBONUS.say(p);
			return;
		}
		
		if(!((IceRunGame)IceRun.status).isAlive(p))
		{
			//IRMessage.GAME_NOBONUS.say(p);
			return;
		}
		
		if(e.getAction() != Action.RIGHT_CLICK_AIR && e.getAction() != Action.RIGHT_CLICK_BLOCK)
			return;

		if(lastUse.containsKey(p) && lastUse.get(p) + 5000 > System.currentTimeMillis())
		{
			int seconds = (int)((this.lastUse.get(p) - System.currentTimeMillis() + 5000) / 1000 + 1);

			if(seconds == 1)
				IceRunMessage.GAME_COOLDOWNBONUS_ONESECOND.say(p);
			else
				IceRunMessage.GAME_COOLDOWNBONUS.say(p, "<timer>", "" + seconds);
			return;
		}

		switch(e.getItem().getType())
		{
		case SUGAR:
			p.addPotionEffect(new PotionEffect(PotionEffectType.SPEED, 100, 1, false, true));
			break;
		case GLOWSTONE_DUST:
			p.setVelocity(new Vector(0, 1.2, 0));
			break;
		}

		lastUse.put(p, System.currentTimeMillis());
		
		if(e.getItem().getAmount() > 1)
			e.getItem().setAmount(e.getItem().getAmount() - 1);
		else
			p.setItemInHand(null);
	}
}