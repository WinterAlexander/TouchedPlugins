package me.winterguardian.bungeecore;

import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.event.PluginMessageEvent;
import net.md_5.bungee.api.plugin.Listener;
import net.md_5.bungee.event.EventHandler;

/**
 *
 * Created by Alexander Winter on 2015-11-19.
 */
public class ProtectionListener implements Listener
{
	@EventHandler
	public void onPluginMessage(PluginMessageEvent event)
	{
		if(!event.getTag().equalsIgnoreCase("BungeeCord"))
			return;

		if(event.getSender() instanceof ProxiedPlayer)
			event.setCancelled(true);
	}
}
