package me.winterguardian.core;

import org.bukkit.Bukkit;
import org.bukkit.event.HandlerList;
import org.bukkit.plugin.java.JavaPlugin;

public class CoreLauncher extends JavaPlugin
{
	@Override
	public void onEnable()
	{
		Core.getCustomEntityManager().enable(this);
		Core.getBungeeMessager().enable(this);
		Core.getBungeeMessager().setSafe(false);
		Core.getGameplayManager().enable(this);

		Bukkit.getPluginManager().registerEvents(new CreativeListener(), this);
	}
	
	@Override
	public void onDisable()
	{
		HandlerList.unregisterAll(this);
		Core.disable(this);
	}
}
