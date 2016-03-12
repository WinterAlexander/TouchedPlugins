package ice.listener;

import ice.IceRun;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.event.player.PlayerTeleportEvent;

public class JoinLeaveListener implements Listener
{
	@EventHandler
	public void onPlayerTeleport(PlayerTeleportEvent e)
	{
		if(IceRun.getSettings().isGameReady())
			if(!IceRun.getPlugin().contains(e.getPlayer()))
			{
				if(IceRun.getSettings().getRegion().contains(e.getTo()) && !IceRun.getSettings().getRegion().contains(e.getFrom()) && !e.getPlayer().hasPermission(IceRun.STAFF))
				{
					e.setCancelled(true);
					IceRun.getPlugin().join(e.getPlayer());
				}
			}
			else if(!IceRun.getSettings().getRegion().contains(e.getTo()) && IceRun.getSettings().getRegion().contains(e.getFrom()))
			{
				e.setCancelled(true);
				IceRun.getPlugin().leave(e.getPlayer());
			}
	}
	
	@EventHandler
	public void onPlayerDeath(PlayerDeathEvent e)
	{
		if(IceRun.getPlugin().contains(e.getEntity()))
			IceRun.getPlugin().leave(e.getEntity());
	}

	@EventHandler
	public void onPlayerLeave(PlayerQuitEvent e)
	{
		if(IceRun.getPlugin().contains(e.getPlayer()))
			IceRun.getPlugin().leave(e.getPlayer());
	}
}