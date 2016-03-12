package me.winterguardian.pvp.game;

import me.winterguardian.core.shop.PlayerPurchaseEvent;
import me.winterguardian.pvp.PvP;
import me.winterguardian.pvp.PvPMessage;
import me.winterguardian.pvp.game.solo.OneInTheChamber;
import me.winterguardian.pvp.game.solo.Switch;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityExplodeEvent;
import org.bukkit.event.hanging.HangingBreakByEntityEvent;
import org.bukkit.event.player.PlayerDropItemEvent;
import org.bukkit.event.player.PlayerInteractAtEntityEvent;
import org.bukkit.event.player.PlayerInteractEntityEvent;
import org.bukkit.event.player.PlayerInteractEvent;

/**
 *
 * Created by Alexander Winter on 2015-12-10.
 */
public class LobbyListener implements Listener
{
	private PvP game;

	public LobbyListener(PvP game)
	{
		this.game = game;
	}

	@EventHandler(priority = EventPriority.LOW)
	public void onPlayerPurchase(PlayerPurchaseEvent event)
	{
		if(!game.contains(event.getPlayer()))
			return;

		if(game.getState() instanceof PvPStandbyState || (game.getState() instanceof PvPVoteState && (((PvPVoteState)game.getState()).getNextGame() instanceof OneInTheChamber) || ((PvPVoteState)game.getState()).getNextGame() instanceof Switch))
		{
			event.setErrorMessage(PvPMessage.PURCHASE_CANTBUYNOW);
			event.setCancelled(true);
		}
	}

	@EventHandler
	public void onPlayerDropItem(final PlayerDropItemEvent event)
	{
		if(!game.contains(event.getPlayer()))
			return;

		if(!(game.getState() instanceof PvPStandbyState) && !(game.getState() instanceof PvPVoteState))
			return;


		event.getItemDrop().setPickupDelay(Integer.MAX_VALUE);
		Bukkit.getScheduler().runTask(game.getPlugin(), new Runnable()
		{
			@Override
			public void run()
			{
				event.getItemDrop().remove();
			}
		});
	}

	@EventHandler
	public void onPlayerInteract(PlayerInteractEvent event)
	{
		if(!game.contains(event.getPlayer()))
			return;

		if(!(game.getState() instanceof PvPStandbyState) && !(game.getState() instanceof PvPVoteState))
			return;

		event.setCancelled(true);
	}

	@EventHandler
	public void onPlayerInteractEntity(PlayerInteractEntityEvent event)
	{
		if(!game.contains(event.getPlayer()))
			return;

		if(!(game.getState() instanceof PvPStandbyState) && !(game.getState() instanceof PvPVoteState))
			return;

		event.setCancelled(true);
	}

	@EventHandler
	public void onPlayerInteractAtEntity(PlayerInteractAtEntityEvent event)
	{
		if(!game.contains(event.getPlayer()))
			return;

		if(!(game.getState() instanceof PvPStandbyState) && !(game.getState() instanceof PvPVoteState))
			return;

		event.setCancelled(true);
	}

	@EventHandler
	public void onHangingBreakByEntity(HangingBreakByEntityEvent event)
	{
		if(!(event.getRemover() instanceof Player))
			return;

		if(!game.contains((Player)event.getRemover()))
			return;

		if(!(game.getState() instanceof PvPStandbyState) && !(game.getState() instanceof PvPVoteState))
			return;

		event.setCancelled(true);
	}

	@EventHandler(priority = EventPriority.HIGHEST, ignoreCancelled = true)
	public void onEntityExplode(EntityExplodeEvent event)
	{
		event.blockList().clear();
	}
}
