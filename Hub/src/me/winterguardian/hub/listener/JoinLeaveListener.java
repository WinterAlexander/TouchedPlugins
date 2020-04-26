package me.winterguardian.hub.listener;

import me.winterguardian.core.Core;
import me.winterguardian.core.playerstats.PlayerStats;
import me.winterguardian.core.util.FireworkUtil;
import me.winterguardian.hub.Hub;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.event.player.PlayerTeleportEvent;

public class JoinLeaveListener implements Listener
{

	@EventHandler
	public void onPlayerJoin(final PlayerJoinEvent event)
	{
		if(!Core.getUserDatasManager().isEnabled())
			return;

		Bukkit.getScheduler().runTaskLater(Hub.getPlugin(), new Runnable()
		{
			@Override
			public void run()
			{
				if(!event.getPlayer().isOnline())
					return;

				if(!Core.getUserDatasManager().isLoaded(event.getPlayer()))
				{
					Bukkit.getScheduler().runTaskLater(Hub.getPlugin(), this, 1);
					return;
				}

				if(new PlayerStats(Core.getUserDatasManager().getUserData(event.getPlayer())).isNew())
				{
					if(Hub.getPlugin().getNewPlayersSpawn() != null)
						event.getPlayer().teleport(Hub.getPlugin().getNewPlayersSpawn());

					FireworkUtil.generateRandom(event.getPlayer().getLocation());
				}
				else if(Hub.getPlugin().getHub() != null)
					event.getPlayer().teleport(Hub.getPlugin().getHub());

				Hub.getPlugin().join(event.getPlayer());
			}
		}, 1);
	}
	
	@EventHandler(priority = EventPriority.LOWEST, ignoreCancelled = true)
	public void onPlayerLeave(PlayerTeleportEvent event)
	{
		if(Hub.getPlugin().contains(event.getFrom()) && !Hub.getPlugin().contains(event.getTo()))
			Hub.getPlugin().leave(event.getPlayer());
	}
	
	@EventHandler(priority = EventPriority.HIGHEST, ignoreCancelled = true)
	public void onPlayerJoin(PlayerTeleportEvent event)
	{
		if(!Hub.getPlugin().contains(event.getFrom()) && Hub.getPlugin().contains(event.getTo()))
			joinAfter(event.getPlayer());
	}
	
	@EventHandler(priority = EventPriority.NORMAL, ignoreCancelled = true)
	public void onPlayerMove(PlayerMoveEvent event)
	{
		if(Hub.getPlugin().contains(event.getFrom()) && !Hub.getPlugin().contains(event.getTo()))
		{
			if(Hub.getPlugin().getHub() != null && !event.getPlayer().hasPermission(Hub.RANK_STAFF))
				event.getPlayer().teleport(Hub.getPlugin().getHub());
			else
				Hub.getPlugin().leave(event.getPlayer());
		}
		
		if(!Hub.getPlugin().contains(event.getFrom()) && Hub.getPlugin().contains(event.getTo()))
			joinAfter(event.getPlayer());
	}

	private void joinAfter(final Player player)
	{
		Bukkit.getScheduler().runTask(Hub.getPlugin(), new Runnable()
		{
			@Override
			public void run()
			{
				if(Hub.getPlugin().contains(player))
					Hub.getPlugin().join(player);
			}
		});
	}

}
