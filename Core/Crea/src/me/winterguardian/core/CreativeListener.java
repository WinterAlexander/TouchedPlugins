package me.winterguardian.core;

import me.winterguardian.core.json.JsonUtil;
import me.winterguardian.core.util.TabUtil;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.ProjectileLaunchEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.weather.WeatherChangeEvent;

/**
 *
 * Created by Alexander Winter on 2016-01-23.
 */
public class CreativeListener implements Listener
{
	private String TAB_HEADER = JsonUtil.toJson("§d§lCréatif §fde §f§lTouched§6§lCraft"), TAB_FOOTER = JsonUtil.toJson("§e§l/hub §fpour retourner au hub");

	@EventHandler
	public void onPlayerJoin(PlayerJoinEvent event)
	{
		TabUtil.sendInfos(event.getPlayer(), TAB_HEADER, TAB_FOOTER);
	}

	@EventHandler
	public void onProjectileLaunch(ProjectileLaunchEvent event)
	{
		if(!(event.getEntity().getShooter() instanceof Player) || !((Player)event.getEntity().getShooter()).isOp())
			event.setCancelled(true);
	}

	@EventHandler
	public void onRainStart(WeatherChangeEvent event)
	{
		if(event.toWeatherState())
			event.setCancelled(true);
	}
}
