package me.winterguardian.pvp.game.team;

import me.winterguardian.pvp.game.PvPPlayerData;
import me.winterguardian.pvp.game.Zone;
import me.winterguardian.pvp.stats.Bonus;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;

/**
 *
 * Created by Alexander Winter on 2015-12-14.
 */
public class DOMListener implements Listener
{
	private Domination game;

	public DOMListener(Domination game)
	{
		this.game = game;
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


		Zone victimZone = null, damagerZone = null;
		for(Zone zone : game.getZones())
		{
			if(zone.getRegion().contains(event.getEntity().getLocation()))
				victimZone = zone;

			if(zone.getRegion().contains(damagerData.getPlayer().getLocation()))
				damagerZone = zone;
		}

		if(victimZone != null && victimZone.getOwner() == victimData.getTeam())
			damagerData.addBonus(Bonus.DOM_OFFENSE);

		if(damagerZone != null && damagerZone.getOwner() == damagerData.getTeam())
			damagerData.addBonus(Bonus.DOM_DEFENSE);

	}
}
