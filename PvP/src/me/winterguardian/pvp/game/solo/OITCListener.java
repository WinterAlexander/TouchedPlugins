package me.winterguardian.pvp.game.solo;

import me.winterguardian.core.shop.PlayerPurchaseEvent;
import me.winterguardian.pvp.game.PvPPlayerData;
import org.bukkit.Material;
import org.bukkit.entity.Arrow;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.player.PlayerPickupItemEvent;
import org.bukkit.event.player.PlayerRespawnEvent;
import org.bukkit.inventory.ItemStack;

/**
 *
 * Created by Alexander Winter on 2015-12-09.
 */
public class OITCListener implements Listener
{
	private OneInTheChamber game;

	public OITCListener(OneInTheChamber game)
	{
		this.game = game;
	}

	@EventHandler
	public void onEntityDamageByEntity(EntityDamageByEntityEvent event)
	{
		if(!(event.getEntity() instanceof Player) || !game.getGame().contains((Player)event.getEntity()))
			return;

		if(event.getDamager() instanceof Arrow)
			event.setDamage(1024d);
	}

	@EventHandler(priority = EventPriority.LOW)
	public void onPlayerDeath(PlayerDeathEvent event)
	{
		PvPPlayerData victimData = game.getPlayerData(event.getEntity());

		if(victimData == null || !victimData.isPlaying())
			return;

		PvPPlayerData damagerData = game.getPlayerData(victimData.getLastDamager());

		if(damagerData == null || !damagerData.isPlaying())
			return;

		damagerData.getPlayer().getInventory().addItem(new ItemStack(Material.ARROW, 1));
	}

	@EventHandler
	public void onPlayerPurchase(PlayerPurchaseEvent event)
	{
		if(game.getGame().contains(event.getPlayer()))
			event.setCancelled(true);
	}

	@EventHandler(priority = EventPriority.HIGHEST)
	public void onPlayerRespawn(PlayerRespawnEvent event)
	{
		if(game.getGame().contains(event.getPlayer()))
			game.getNewStuff(event.getPlayer(), false).give(event.getPlayer());
	}

	@EventHandler(priority = EventPriority.HIGHEST)
	public void onPlayerPickupItem(PlayerPickupItemEvent event)
	{
		if(game.getGame().contains(event.getPlayer()))
			event.setCancelled(true);
	}
}
