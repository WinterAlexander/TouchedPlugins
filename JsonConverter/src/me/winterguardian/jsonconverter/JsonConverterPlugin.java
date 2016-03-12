package me.winterguardian.jsonconverter;

import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;

public class JsonConverterPlugin extends JavaPlugin
{
	@Override
	public void onEnable()
	{
		Bukkit.getLogger().info("JsonTranslatorAPI has been loaded properly.");
	}
	
	@Override
	public void onDisable()
	{
		Bukkit.getLogger().info("JsonTranslatorAPI has been unloaded properly.");
	}
}
