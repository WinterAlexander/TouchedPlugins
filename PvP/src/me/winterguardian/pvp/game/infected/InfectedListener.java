package me.winterguardian.pvp.game.infected;

import me.winterguardian.core.shop.PlayerPurchaseEvent;
import me.winterguardian.pvp.TeamColor;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.HumanEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.FoodLevelChangeEvent;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.player.PlayerRespawnEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import java.util.HashMap;

/**
 * Undocumented :(
 * <p>
 * Created on 2020-04-26.
 *
 * @author Alexander Winter
 */
public class InfectedListener implements Listener
{
	private final Infected infected;

	private HashMap<Player, Boolean> justInfected = new HashMap<>();

	public InfectedListener(Infected infected)
	{
		this.infected = infected;
	}

	@EventHandler(priority = EventPriority.LOWEST, ignoreCancelled = true)
	public void onPlayerRespawnInfect(PlayerRespawnEvent event)
	{
		if(!infected.getGame().contains(event.getPlayer()))
			return;

		if(infected.getPlayerData(event.getPlayer()).getTeam() == TeamColor.HUMAN)
		{
			infected.infect(event.getPlayer(), false);
			justInfected.put(event.getPlayer(), true);
		}
	}

	@EventHandler(priority = EventPriority.MONITOR)
	public void onPlayerDeath(PlayerDeathEvent event)
	{
		Player p = event.getEntity();

		if(!infected.getGame().contains(p))
			return;

		p.getWorld().dropItemNaturally(p.getLocation(), new ItemStack(Material.ROTTEN_FLESH, 10));
	}

	@EventHandler(priority = EventPriority.HIGHEST, ignoreCancelled = true)
	public void onPlayerRespawnSetLoc(PlayerRespawnEvent event)
	{
		if(!infected.getGame().contains(event.getPlayer()))
			return;

		if(infected.getGame().getState() != infected)
			return;

		Player player = event.getPlayer();

		if(justInfected.get(player) == Boolean.TRUE)
		{
			event.setRespawnLocation(event.getPlayer().getLocation());
			justInfected.put(player, false);
		}

		if(infected.getPlayerData(player).getTeam() == TeamColor.INFECTED)
		{
			Bukkit.getScheduler().runTask(infected.getGame().getPlugin(), () -> {
				player.addPotionEffect(new PotionEffect(PotionEffectType.HUNGER, Integer.MAX_VALUE, 0, true, false));
			});
		}
	}

	@EventHandler(priority = EventPriority.HIGH, ignoreCancelled = true)
	public void onInfectedChangeArmor(InventoryClickEvent event)
	{
		for(HumanEntity humanEntity : event.getViewers())
		{
			if(!(humanEntity instanceof Player))
				continue;

			Player player = (Player)humanEntity;

			if(!infected.getGame().contains(player))
				continue;

			if(infected.getPlayerData(player).getTeam() != TeamColor.INFECTED)
				continue;

			int slot = event.getSlot();
			if(slot == 36 || slot == 37 || slot == 38 || slot == 39)
				event.setCancelled(true);
		}
	}

	@EventHandler(priority = EventPriority.HIGH, ignoreCancelled = true)
	public void onInfectedPunch(EntityDamageByEntityEvent event)
	{
		if(!(event.getDamager() instanceof Player))
			return;

		Player damager = (Player)event.getDamager();

		if(!infected.getGame().contains(damager))
			return;

		if(infected.getPlayerData(damager).getTeam() == TeamColor.INFECTED)
		{
			damager.addPotionEffect(new PotionEffect(PotionEffectType.INCREASE_DAMAGE, 10 * 20, 1, false, true));
			damager.addPotionEffect(new PotionEffect(PotionEffectType.REGENERATION, 5 * 20, 1, false, true));
		}
	}

	@EventHandler(priority = EventPriority.HIGHEST, ignoreCancelled = true)
	public void onHumanHunger(FoodLevelChangeEvent event)
	{
		if(!(event.getEntity() instanceof Player))
			return;

		Player player = (Player)event.getEntity();

		if(!infected.getGame().contains(player))
			return;

		if(infected.getPlayerData(player).getTeam() == TeamColor.HUMAN)
			event.setFoodLevel(20);
	}

	@EventHandler
	public void onInfectedPurchase(PlayerPurchaseEvent event)
	{
		Player player = event.getPlayer();

		if(infected.getGame().contains(player) && infected.getPlayerData(player).getTeam() == TeamColor.INFECTED)
			event.setCancelled(true);
	}
}
