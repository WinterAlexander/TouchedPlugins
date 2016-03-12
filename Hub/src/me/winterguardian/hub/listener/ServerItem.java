package me.winterguardian.hub.listener;

import me.winterguardian.core.Core;
import me.winterguardian.core.inventorygui.GUIItem;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;

/**
 *
 * Created by Alexander Winter on 2016-01-15.
 */
public abstract class ServerItem extends GUIItem
{
	private String server;

	public ServerItem(int slot, String server)
	{
		super(slot);
		this.server = server;
	}

	@Override
	public void click(Player player, ClickType click)
	{
		Core.getBungeeMessager().sendToServer(player, server);
	}
}
