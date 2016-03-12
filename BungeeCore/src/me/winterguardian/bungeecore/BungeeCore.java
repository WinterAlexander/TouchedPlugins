package me.winterguardian.bungeecore;

import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.plugin.Plugin;

/**
 * Created by Alexander Winter on 2015-11-17.
 */
public class BungeeCore extends Plugin
{
	@Override
	public void onLoad()
	{
		super.onLoad();
	}

	@Override
	public void onEnable()
	{
		super.onEnable();
		ProxyServer.getInstance().getPluginManager().registerCommand(this, new ExecuteCommand());
		ProxyServer.getInstance().getPluginManager().registerListener(this, new ProtectionListener());

	}

	@Override
	public void onDisable()
	{
		super.onDisable();
	}
}
