package me.winterguardian.soundsapi;

import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;

public class SoundsAPI extends JavaPlugin
{
	@Override
	public void onEnable()
	{
		Bukkit.getLogger().info("SoundsAPI have been loaded correctly.");
	}
}
